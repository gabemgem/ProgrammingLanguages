s(s(find,a,set,of,I,that,Op,to,N)) -> op(Op,I,N).
i(N,even) -> even(N).
i(N,odd) -> odd(N).
i(N1,and,N2) -> both(N1,N2).
both(N1,N2) -> even(N1),odd(N2).

op(sum,I,N) -> sum(i(I),number(N)).
op(multiply,I,N) -> mul(i(I),number(N)).
even(N) :- number(N).
odd(N) :- number(N).



goodSet(Set,Target,Operation) :-

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% I think the stuff below this is decent
number(N) :- between(0,128,N).

unique([]).
unique([X|Y]) :- \+ memberchk(X,Y), unique(Y).

evennum(N) :- 
number(N),
0 is N mod 2.

oddnum(N) :-
number(N),
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
product_list(Y,P2),
Product is X * P2.

interestingSet(S) :- 
unique(S),
correctAmounts(Set,E,O),
goodSet(Set, Target, Operation).