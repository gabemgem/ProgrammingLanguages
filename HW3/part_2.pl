%% Gabe Maayan, Skylar Sang
%% Programming Languages Fall 2019 PA3
%% Written in ProLog

main :- 
read_line_to_codes(user_input,Cs), atom_codes(A, Cs), atomic_list_concat(L, ' ', A),
parseFind(L).


%Horrendous Parser Code

invalid :- write('Invalid String'), fail.
parseFind([Head | Tail]):-
((Head = 'Find') -> parseA(Tail); invalid).

parseA([Head | Tail]):-
((Head = 'a') -> parseSet(Tail); invalid).

parseSet([Head | Tail]):-
((Head = 'set') -> parseOf(Tail); invalid).

parseOf([Head | Tail]):-
((Head = 'of') -> parseFirstNum(Tail); invalid).

%use atom_number to set X to the integer version of Head
parseFirstNum([Head | Tail]):-
atom_number(Head, X), parseFirstType(X, Tail); invalid.

parseFirstType(FirstNum, [Head | Tail]) :-
(Head = 'even')-> Evens is FirstNum, Odds is 0, parseAnd(Evens, Odds, Tail);
(Head = 'odd')-> Odds is FirstNum, Evens is 0, parseAnd(Evens, Odds, Tail);
invalid.

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

parseSum(Evens, Odds, [Head | Tail]) :- 
atom_number(Head, X),
(Tail = [])->make_set(Evens, Odds, [], 'sum', X); invalid.

parseMul(Evens, Odds, [Head | Tail]) :-
atom_number(Head, X),
(Tail = [])->make_set(Evens, Odds, [], 'multiply', X); invalid.








%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% I think the stuff below this is decent

unique([]).
unique([X|Y]) :- \+ memberchk(X,Y), unique(Y).

evennum(N) :- 
between(0,128,N),
0 is N mod 2.

oddnum(N) :-
between(0,128,N),
1 is N mod 2.

correctAmounts([],E,O) :-
0 is E,
0 is O.

correctAmounts([X|Y],E,O) :-
evennum(X),
E2 is E - 1,
correctAmounts(Y,E2,O).

correctAmounts([X|Y],E,O) :-
oddnum(X),
O2 is O - 1,
correctAmounts(Y,E,O2).

add_list(X,Sum) :- sum_list(X,Sum).

product_list([],1).
product_list([X|Y],Product) :-
between(0,128,X),
product_list(Y,P2),
Product is X * P2.

make_set(0,0,S,M,G) :- 
M = 'multiply',
product_list(S,G2),
G2 = G,
write(S).

make_set(0,0,S,M,G) :- 
M = 'sum',
add_list(S,G2),
G2 = G,
write(S).

make_set(0,Odds,S,M,G) :-
Odds > 0,
oddnum(N),
[N|S] = S2,
unique(S2),
Odds2 is Odds - 1,
make_set(0,Odds2,S2,M,G).

%% params: num even numbers, num odd numbers, input set, method ('sum'/'multiply'), goal number
make_set(Evens,Odds,S,M,G) :-
Evens > 0,
evennum(N),
[N|S] = S2,
unique(S2),
Evens2 is Evens - 1,
make_set(Evens2,Odds,S2,M,G).