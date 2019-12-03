%% Gabe Maayan, Skylar Sang
%% Programming Languages Fall 2019 PA3
%% Written in ProLog

% Accept Input str and split atoms to array 
main([S]) :- 
set_prolog_flag(prompt_alternatives_on,groundness),
atomic_list_concat(L, ' ', S),
parseFind(L).


% Brute-Force Parser Code
% Invalid String fail handler function
invalid :- write('Invalid String'), fail.
parseFind([Head | Tail]):-
((Head = 'Find') -> parseA(Tail); invalid).

parseA([Head | Tail]):-
((Head = 'a') -> parseSet(Tail); invalid).

parseSet([Head | Tail]):-
((Head = 'set') -> parseOf(Tail); invalid).

parseOf([Head | Tail]):-
((Head = 'of') -> parseFirstNum(Tail); invalid).

% Use atom_number to set X to get the integer version of Head
parseFirstNum([Head | Tail]):-
atom_number(Head, X) -> parseFirstType(X, Tail); invalid.

% Branch depending on first type
parseFirstType(FirstNum, [Head | Tail]) :-
(Head = 'even')-> Evens is FirstNum, Odds is 0, parseAnd(Evens, Odds, Tail);
(Head = 'odd')-> Odds is FirstNum, Evens is 0, parseAnd(Evens, Odds, Tail);
invalid.

%Check to see if both sets of numbers will be used, otherwise leave 2nd type as 0
parseAnd(Evens, Odds, [Head | Tail]) :-
((Head = 'and') -> parseSecondNum(Evens, Odds, Tail));
((Head = 'integers') -> parseThat(Evens, Odds, Tail)); 
invalid.

parseSecondNum(Evens, Odds, [Head | Tail]) :-
(Odds = 0, Evens > 0) -> atom_number(Head, X), parseSecondType(Evens, X, Tail);
(Odds > 0, Evens = 0) -> atom_number(Head, X), parseSecondType(X, Odds, Tail); invalid.

parseSecondType(Evens, Odds, [Head | Tail]) :-
(Head = 'even'; Head = 'odd') -> parseIntegers(Evens, Odds, Tail); invalid.

parseIntegers(Evens, Odds, [Head | Tail]) :-
(Head = 'integers') -> parseThat(Evens, Odds, Tail); invalid.

parseThat(Evens, Odds, [Head | Tail]) :-
(Head = 'that') -> parseOp(Evens, Odds, Tail); invalid.

parseOp(Evens, Odds, [Head | Tail]) :-
((Head = 'sum') -> parseSumTo(Evens, Odds, Tail));
((Head = 'multiply') -> parseMulTo(Evens, Odds, Tail));
invalid.

parseSumTo(Evens, Odds, [Head | Tail]) :-
(Head = 'to') -> parseSum(Evens, Odds, Tail); invalid.

parseMulTo(Evens, Odds, [Head | Tail]) :-
(Head = 'to') -> parseMul(Evens, Odds, Tail); invalid.

% Check to see if this is end of sentence, then perform set operations
parseSum(Evens, Odds, [Head | Tail]) :- 
atom_number(Head, X),
(Tail = [])->make_set(Evens, Odds, [], 'sum', X); invalid.

parseMul(Evens, Odds, [Head | Tail]) :-
atom_number(Head, X),
(Tail = [])->make_set(Evens, Odds, [], 'multiply', X); invalid.


%% Checks that every element in a list is unique
unique([]).
unique([X|Y]) :- \+ memberchk(X,Y), unique(Y).

%% Binds all even numbers between 0 and 128 to N
evennum(N) :- 
between(0,128,N),
0 is N mod 2.

%% Binds all odd numbers between 0 and 128 to N
oddnum(N) :-
between(0,128,N),
1 is N mod 2.

%% Unnecessary rename of a builtin function to add elements of a list
add_list(X,Sum) :- sum_list(X,Sum).

%% Multiplies together all elements of a list
product_list([],1).
product_list([X|Y],Product) :-
between(0,128,X),
product_list(Y,P2),
Product is X * P2.

%% Prints out a list in the method specified by the homework
output_list([]).

output_list([H|[]]) :-
write(H).

output_list([H|T]) :-
write(H),
write(','),
output_list(T).


%% make_set is the main function that finds an interesting set from the
%% parse data
%% params: num even numbers, num odd numbers, input set, 
%% method ('sum'/'multiply'), goal number

make_set(0,0,S,M,G) :- 
M = 'multiply',
product_list(S,G2),
G2 = G,
output_list(S).

make_set(0,0,S,M,G) :- 
M = 'sum',
add_list(S,G2),
G2 = G,
output_list(S).

make_set(0,0,S,M,G) :-
write('No Solution').

make_set(0,Odds,S,M,G) :-
Odds > 0,
oddnum(N),
[N|S] = S2,
unique(S2),
Odds2 is Odds - 1,
make_set(0,Odds2,S2,M,G).

make_set(Evens,Odds,S,M,G) :-
Evens > 0,
evennum(N),
[N|S] = S2,
unique(S2),
Evens2 is Evens - 1,
make_set(Evens2,Odds,S2,M,G).