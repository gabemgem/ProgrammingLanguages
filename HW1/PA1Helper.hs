module PA1Helper(runProgram,Lexp(..),alphaRename,betaReduce,etaConvert) where

import System.Directory
import System.Environment
import Control.Monad
import Text.Parsec
import Text.Parsec.String
import Text.Parsec.Expr
import Text.Parsec.Token
import Text.Parsec.Language
import Text.Parsec.Char
import qualified Data.Set as Set
import Data.List as List
import Data.Map (Map)
import qualified Data.Map as Map

-- Haskell representation of lambda expression
data Lexp = Atom String | Lambda String Lexp | Apply Lexp Lexp deriving Eq

-- Allow for Lexp datatype to be printed like the Oz representation of a lambda expression
instance Show Lexp  where 
    show (Atom v) = v
    show (Lambda exp1 exp2) = "\\" ++ exp1 ++ "." ++ (show exp2) 
    show (Apply exp1 exp2) = "(" ++ (show exp1) ++ " " ++ (show exp2) ++ ")" 


-- Reserved keywords in Oz
-- P. 841 Table C.8, "Concepts, Techniques, and Models of Computer Programming", 
-- Van Roy, Haridi
ozKeywords = ["andthen","at","attr","break"
              ,"case","catch","choice","class"
              ,"collect","cond","continue"
              ,"declare","default","define"
              ,"dis","div","do","else"
              ,"elsecase","elseif","elseof"
              ,"end","export","fail","false"
              ,"feat","finally","for","from"
              ,"fun","functor","if","import"
              ,"in","lazy","local","lock"
              ,"meth","mod","not","of","or"
              ,"orelse","prepare","proc"
              ,"prop","raise","require"
              ,"return","self","skip","then"
              ,"thread","true","try","unit"
              ] 

-- Sparse language definition to define a proper Oz identifier
-- An atom is defined as follows:
-- 1. sequence of alphanumeric chars starting with a lowercase letter, 
--   excluding language keywords
-- 2. arbitrary printable chars enclosed in single quotes, 
--   excluding "'", "\", and "NUL"
-- lDef defines an atom as only 1.
-- P. 825,"Concepts, Techniques, and Models of Computer Programming", 
-- Van Roy, Haridi
lDef = emptyDef { identStart = lower
                , identLetter = alphaNum
                , reservedNames = ozKeywords
                } 
-- Obtains helper functions for parsing 
TokenParser{ parens = m_parens
           , identifier = m_identifier
           , reserved = m_reserved
           , brackets   = m_brackets
           } = makeTokenParser lDef

-- Below is code to parse Oz lambda expressions and represent them in Haskell
atom = do
  var <- m_identifier
  return (Atom var)

lambargs = do
  var1 <- m_identifier
  char '.'
  var2 <- start
  return (Lambda var1 var2)

lamb = do
  char '\\'
  p <- lambargs
  return p

appargs = do
    var1 <- start
    spaces
    var2 <- start
    return (Apply var1 var2)

app = do
  p <- m_parens appargs 
  return p

start = atom <|> lamb <|> app

-- Use previously defined parser to parse a given String
parseLExpr :: String -> Either ParseError Lexp 
parseLExpr input = parse start "" input

-- Gracefully handle parse errors, proceeding to the next expression in the file and printing a helpful message
handler :: (Lexp -> Lexp) -> String -> Int -> String -> IO ()
handler reducer outFile n str  = case parseLExpr str of
    Left err -> do
      putStrLn ("Parse error for expression " ++ show n ++ ": " ++ show err)
      writeFile outFile "Error"
    Right lexp -> outputPrinter outFile n lexp (reducer lexp)

doDebugPrint = True

-- Pretty printer for outputting inputted lambda expressions along with
-- their reduced expressions. Integer used to distiguish between test cases.
-- Note - the stdout printing is for your convenience only; we will be
-- grading the contents of the output file
outputPrinter :: String -> Int -> Lexp -> Lexp -> IO ()
outputPrinter outFile n lexp lexp' = do
    appendFile outFile ((show lexp') ++ "\n")
    when doDebugPrint $ do
      putStrLn ("Input  " ++ (show n) ++ ": " ++ (show lexp))
      putStrLn ("Result " ++ (show n) ++ ": " ++ (show lexp'))
      putStrLn ""

-- Given input/output f iles and a function for reducing lambda expressions,
-- reduce all valid lambda expressions in the file and output results.
runProgram :: String -> String -> (Lexp -> Lexp) -> IO ()
runProgram inFile outFile reducer = do
    exists <- doesFileExist outFile
    when exists $ removeFile outFile
    fcontents <- readFile inFile
    let inList = lines fcontents
    sequence_ (zipWith (handler reducer outFile) [1..] inList)




--------------------------------------ALPHA RENAMING--------------------------------------

posStrings = ["a","b","c","d","e","f","g"
              ,"h","i","j","k","l","m","n"
              ,"o","p","q","r","s","t","u"
              ,"v","w","x","y","z"]

alphaRename :: Lexp -> Lexp
alphaRename lexp@(Atom x) = lexp
alphaRename lexp@(Lambda x y) = rexp where
    rexp = Lambda x (alphaRename y)
alphaRename lexp@(Apply e1 e2) = rexp where
    sTotal = alphaSearch lexp Set.empty
    se1 = alphaSearch e1 Set.empty
    se2 = alphaSearch e2 Set.empty
    sIntersect = Set.intersection se1 se2
    rexp = if sIntersect == Set.empty
        then lexp
        else rexp2 where
            newStrings = alphaPicker posStrings sTotal (Set.size sIntersect)
            rexp2 = Apply (alphaHelper0 ((Set.toList sIntersect), newStrings, e1)) e2

alphaSearch :: Lexp -> Set.Set String -> Set.Set String
alphaSearch lexp@(Atom str) s = newS where
    newS = Set.insert str s
alphaSearch lexp@(Lambda x y) s = newS where
    s1 = Set.insert x s
    newS = alphaSearch y s1
alphaSearch lexp@(Apply x y) s = newS where
    s1 = alphaSearch x s
    newS = alphaSearch y s1

alphaPicker :: [String] -> Set.Set String -> Int -> [String]
alphaPicker posStrings@(h:t) takenStrings num = newString where
    newString = if Set.member h takenStrings
        then alphaPicker t takenStrings num
        else if num == 1
            then [h]
            else h:(alphaPicker t (Set.deleteMin takenStrings) (num - 1) )

-- Tuple is ([stringsToRename], [newStrings], Lexp)
alphaHelper0 :: ([String], [String], Lexp) -> Lexp
alphaHelper0 ([], [], lexp) = lexp
alphaHelper0 (h1:t1, b@(h2:t2), lexp) = rexp where
    rexp1 = alphaHelper0 (t1, t2, lexp)
    rexp = aHLookButDontReplace (h1, h2, rexp1)

aHLookButDontReplace :: (String, String, Lexp) -> Lexp
aHLookButDontReplace (a, b, v@(Atom currentString)) = v
aHLookButDontReplace (a, b, t@(Lambda x y)) = rexp where
    rexp = if x == a
        then Lambda b (aHLookAndReplace (a, b, y))
        else Lambda x (aHLookButDontReplace (a, b, y))
aHLookButDontReplace (a, b, t@(Apply x y)) = Apply (aHLookButDontReplace (a, b, x)) (aHLookButDontReplace (a, b, y))

aHLookAndReplace :: (String, String, Lexp) -> Lexp
aHLookAndReplace (a, b, v@(Atom currentString)) = Atom (aHReplace (a, b, currentString))
aHLookAndReplace (a, b, t@(Lambda x y)) = rexp where
    rexp = if x == a
        then t
        else Lambda x (aHLookAndReplace (a, b, y))
aHLookAndReplace (a, b, t@(Apply x y)) = Apply (aHLookAndReplace (a, b, x)) (aHLookAndReplace (a, b, y))

-- Tuple structure is (stringToReplace, replacementString, currentString)
aHReplace :: (String, String, String) -> String
aHReplace (a, b, c) = if c == a
    then b
    else c


--------------------------------------BETA REDUCTION--------------------------------------


--betaReduction for outermost Apply (call for beta reduction here)
--should always look for Apply within Lambda statemets
betaReduce :: Lexp -> Lexp
betaReduce lexp@(Lambda e1 e2) = rexp where
    rexp = Lambda e1 (betaReduce (alphaRename e2))

betaReduce lexp@(Apply e1 e2) = rexp where
    rexp1 = replace e1 e2
    rexp = if rexp1 == lexp
        then rexp1
        else betaReduce (alphaRename rexp1)
betaReduce lexp@(Atom v) = lexp


--takes letter string from Lambda and search function to replace token
--should always look for Lambda (inside of Apply)
replace :: Lexp -> Lexp  -> Lexp
-- beta reduce second lexp
replace a@(Atom v) lexp2 = Apply a (betaReduce (alphaRename lexp2))
-- beta reduce elements of apply
replace lexp1@(Apply x y) lexp2 = Apply (betaReduce (alphaRename lexp1)) (betaReduce (alphaRename lexp2))
-- Interesting case
replace lexp@(Lambda a1 a2) lexp2 = rexp where
    rexp = search a2 a1 lexp2




-- if expression is an atom, replace with apply expression if token matches
-- input is lexp from the lambda, atom from lambda, lexp to substitute
search :: Lexp -> String -> Lexp -> Lexp
search lexp@(Atom v) str lexp2 = 
    if v == str
        then lexp2
        else Atom v

-- return if searching lambda function, return lambda with reduced arg2
search lexp@(Lambda a1 a2) str lexp2 = Lambda a1 (search a2 str lexp2)

-- return 
search lexp@(Apply a1 a2) str lexp2 = rexp where
    rexp = Apply (search a1 str lexp2) (search a2 str lexp2)


--------------------------------------ETA CONVERSION--------------------------------------


etaConvert :: Lexp -> Lexp
etaConvert lexp@(Lambda a1 a2) = let
    rexp = etaRedundant (Atom a1) a2 in
    if rexp == lexp
        then rexp
        else etaConvert (alphaRename rexp)

etaConvert lexp@(Apply a1 a2) = rexp where
    rexp = Apply (etaConvert a1) (etaConvert a2)

etaConvert lexp@(Atom v) = lexp

etaRedundant :: Lexp -> Lexp -> Lexp
etaRedundant lexp@(Atom v) lexp2@(Apply a1 a2) =
    if lexp == a2
        then if etaCheckForX a1 v
          then a1
          else Lambda v lexp2
        else Lambda v lexp2

etaRedundant lexp@(Atom v) lexp2 = Lambda v lexp2

etaCheckForX :: Lexp -> String -> Bool
etaCheckForX lexp@(Atom v) str = 
  if v == str
    then False
    else True
etaCheckForX lexp@(Lambda x y) str =
  if x == str
    then True
    else etaCheckForX y str
etaCheckForX lexp@(Apply x y) str = 
  etaCheckForX x str && etaCheckForX y str
--Function to take Lambda and Lexp, add Lambda letter and Lexp to map
--Function to take exp from lambda
    -- If apply, replace again
    -- If lambda, do nothing and look into expression
    -- If atom is seen, check to see if  