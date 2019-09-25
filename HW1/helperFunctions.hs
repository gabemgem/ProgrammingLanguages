module HelperFunctions(alphaRename) where

import PA1Helper
import qualified Data.Set as Set
import Data.List as List

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