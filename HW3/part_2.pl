%s(s(find,a,set,of,I,that,Op,to,N)) -> op(Op,I,N).
%i(N,even) -> even(N).
%i(N,odd) -> odd(N).
%i(N1,and,N2) -> both(N1,N2).
%both(N1,N2) -> even(N1),odd(N2).

%op(sum,I,N) -> sum(i(I),number(N)).
%op(multiply,I,N) -> mul(i(I),number(N)).
%even(N) :- number(N).
%odd(N) :- number(N).

main :- 
read_line_to_codes(user_input,Cs), atom_codes(A, Cs), atomic_list_concat(L, ' ', A),
parseFind(L).


%Horrendous Parser Code
parseFind([Head | Tail]):-
write(Tail), nl,
write(Head),
((Head = 'Find') -> parseA(Tail); fail).

parseA([Head | Tail]):-
write(Head),
((Head = 'a') -> parseSet(Tail); fail).

parseSet([Head | Tail]):-
write(Head),
((Head = 'set') -> parseOf(Tail); fail).

parseOf([Head | Tail]):-
write(Head),
((Head = 'of') -> parseFirstNum(Tail); fail).

%use atom_number to set X to the integer version of Head
parseFirstNum([Head | Tail]):-
write(Head),
atom_number(Head, X), parseFirstType(X, Tail); fail.


%%%%%%%%%%%%%%% PROBLEM AREA%%%%%%%%%%%%%%%%%%%%%%%
% Prints each word in list, but not sure if I can just assign SecondNum to 0 like that
% Further functions use the format of Fn(even#s, odd#s, rest_of_input)
parseFirstType(FirstNum, [Head | Tail]) :-
write(Head),
(Head = 'even')-> Evens is FirstNum, Odds is 0, parseAnd(Evens, Odds, Tail);
(Head = 'odd')-> Odds is FirstNum, Evens is 0, parseAnd(Evens, Odds, Tail);
fail.

parseAnd(Evens, Odds, [Head, Tail]) :-
write(Head), 
((Head = 'and') -> parseSecondNum(Evens, Odds, Tail));
((Head = 'integers') -> parseThat(Evens, Odds, Tail));
fail.



parseIntegers(Evens, Odds, [Head, Tail]) :-
write(Head),
(Head = 'integers') -> parseThat(Evens, Odds, Tail); fail.

parseThat(Evens, Odds, [Head, Tail]) :-
write(Head), write(Tail),
(Head = 'that') -> parseOp(Evens, Odds, Tail); fail.

parseOp(Evens, Odds, [Head, Tail]) :-
write(Head),
((Head = 'sum') -> parseSumTo(Evens, Odds, Tail));
((Head = 'multiply') -> parseMulTo(Evens, Odds, Tail));
fail.

parseSumTo(Evens, Odds, [Head, Tail]) :-
write(Head),
(Head = 'to') -> parseSum(Evens, Odds, Tail); fail.

parseMulTo(Evens, Odds, [Head, Tail]) :-
write(Head),
(Head = 'to') -> parseMul(Evens, Odds, Tail); fail.

%end of horrendous parsing, still need to check if the command continues past this last number, but thats a later problem
parseSum(Evens, Odds, Head) :-
write(Head).

parseMul(Evens, Odds, Head) :-
write(Head).







%goodSet(Set,Target,Operation) :-

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

%interestingSet(S) :- 
%unique(S),
%correctAmounts(Set,E,O),
%goodSet(Set, Target, Operation).