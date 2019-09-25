module Reduction(betaReduce, etaConvert) where

import PA1Helper
import HelperFunctions (alphaRename)
import Data.Map (Map)
import qualified Data.Map as Map


--betaReduction for outermost Apply (call for beta reduction here)
--should always look for Apply within Lambda statemets
betaReduce :: Lexp -> Lexp
betaReduce lexp@(Lambda e1 e2) = rexp where
    rexp = Lambda e1 (betaReduce e2)

betaReduce lexp@(Apply e1 e2) = let
    rexp = replace e1 e2 in
    if rexp == lexp
        then rexp
        else betaReduce (alphaRename rexp)
betaReduce lexp@(Atom v) = lexp


--takes letter string from Lambda and search function to replace token
--should always look for Lambda (inside of Apply)
replace :: Lexp -> Lexp  -> Lexp
replace lexp@(Lambda a1 a2) lexp2 = rexp where
    rexp = search a2 a1 lexp2

replace lexp@(Apply a1 a2) lexp2 = Apply (betaReduce lexp) lexp2

--Return the original apply?
replace lexp lexp2 = Apply lexp lexp2




--if expression is an atom, replace with apply expression if token matches
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

etaConvert :: Lexp -> Lexp
etaConvert lexp@(Lambda a1 a2) = let
    rexp = etaRedundant (Atom a1) a2 in
    if rexp == lexp
        then rexp
        else etaConvert (alphaRename rexp)

etaConvert lexp@(Apply a1 a2) = rexp where
    rexp = Apply (etaConvert a1) (etaConvert a2)

etaConvert lexp = lexp

etaRedundant :: Lexp -> Lexp -> Lexp
etaRedundant lexp@(Atom v) lexp2@(Apply a1 a2) =
    if lexp == a2
        then a1
        else Lambda v lexp2

etaRedundant lexp@(Atom v) lexp2 = Lambda v lexp2


--Function to take Lambda and Lexp, add Lambda letter and Lexp to map
--Function to take exp from lambda
    -- If apply, replace again
    -- If lambda, do nothing and look into expression
    -- If atom is seen, check to see if  