main :-
set_prolog_flag(prompt_alternatives_on,groundness),
between(0,10000,N1),
between(0,10000,N2),
between(0,10000,N3),
between(0,10000,N4),
interestingSet(N1,N2,N3,N4),
output_list([N1,N2,N3,N4]).

%% Determines if an individual integer is possible to be in the set
possibleInd(D) :- 
integer(D),
between(0,10000,D),
D =\= 1,
D =\= 3,
D =\= 8,
D =\= 120.

%% Determines if 4 integers could be in the set
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

%% Returns true if the sqrt of N is an integer
square(N) :-
Int is rationalize(sqrt(N)),
integer(Int).

%% Tests whether the product of two integers is 1 less than a perfect square
inSet(A,B) :-
C is (A*B)+1,
square(C).

%% Sets up a possible set and tests each pair in the set for interestingness
interestingSet(N1,N2,N3,N4) :-
possible(N1,N2,N3,N4),
inSet(N1,N2),
inSet(N1,N3),
inSet(N1,N4),
inSet(N2,N3),
inSet(N2,N4),
inSet(N3,N4).

%% Prints a list in the method specified by the homework
output_list([]).

output_list([H|[]]) :-
write(H).

output_list([H|T]) :-
write(H),
write(','),
output_list(T).