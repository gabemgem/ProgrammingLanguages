

possibleInd(D) :- 
integer(D),
between(0,10000,D),
D =\= 1,
D =\= 3,
D =\= 8,
D =\= 120.

possible(D1,D2,D3,D4) :-
possibleInd(D1),
possibleInd(D2),
possibleInd(D3),
possibleInd(D4),
D1 =\= D2,
D1 =\= D3,
D1 =\= D4,
D2 =\= D3,
D2 =\= D4,
D3 =\= D4.


square(N) :-
Int is rationalize(sqrt(N)),
integer(Int).

inSet(A,B) :-
C is (A*B)+1,
square(C).

testSet(N1,N2,N3,N4) :-
inSet(N1,N2),
inSet(N1,N3),
inSet(N1,N4),
inSet(N2,N3),
inSet(N2,N4),
inSet(N3,N4).

interestingSet(N1,N2,N3,N4) :-
possible(N1,N2,N3,N4),
inSet(N1,N2),
inSet(N1,N3),
inSet(N1,N4),
inSet(N2,N3),
inSet(N2,N4),
inSet(N3,N4).

main(W,X,Y,Z) :-
between(0,10000,N1),
between(0,10000,N2),
between(0,10000,N3),
between(0,10000,N4),
interestingSet(N1,N2,N3,N4),
W is N1,
X is N2,
Y is N3,
Z is N4.