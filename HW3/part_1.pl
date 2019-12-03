main :-
set_prolog_flag(prompt_alternatives_on,groundness),
between(0,10000,N1),
between(0,10000,N2),
between(0,10000,N3),
between(0,10000,N4),
interestingSet(N1,N2,N3,N4),
output_list([N1,N2,N3,N4]).

%% Checks that every element in a list is unique and allowed
unique([]).
unique([X|Y]) :- 
\+ memberchk(X,Y),
X =\= 1,
X =\= 3,
X =\= 8,
X =\= 120,
unique(Y).

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
unique([N1,N2,N3,N4]),
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