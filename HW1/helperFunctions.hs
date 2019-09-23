module HelperFunctions(Lexp(..)) where

import PA1Helper

-- Tuple structure is ((atomToReplace, replacementAtom), Lexp)
alphaHelper :: (a, b, c) ->  Lexp
alphaHelper (a, b, v@(Atom currentAtom)) = if currentAtom == a 
	then Atom b
	else Atom currentAtom
alphaHelper (a, b, t@(Lambda x y)) = Lambda alphaHelper (a, b, x) alphaHelper (a, b, y)
alphaHelper (a, b, t@(Apply x y)) = Apply alphaHelper (a, b, x) alphaHelper (a, b, y)