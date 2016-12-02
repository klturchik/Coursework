%% This homework is due Thursday June 2. There will be no extension. 
%%
%% Please follow carefully these instructions to submit your homework. Points
%% will be taken out for non compliance to these instructions.
%% Submit your homework in a file named after you, using the pattern LastName_FirstName.pl 
%% as in Doe_Jane.pl.
%% Include only the code you have written in the file. Do not include any definition for a 
%% maze. The maze below is there to help you understand the problem and test your answers. 
%% I'll use a different puzzle to test your answers.
%%
%% You are asked to write 5 predicate. Each predicate is worth 20 points. You can write
%% auxiliary predicates as you see fit, those must be included in your file (obviously) 
%% but those will not add to your grade.
%%
%%

%% This homework consists of writing a number of predicates to navigate and/or analyze a maze. 
%% Your predicates should not rely on the specific maze above, but should work with any maze
%% described using the predicate door/2 and trap/2. 
%% Feel free to create any helper predicate you see fit, but make sure that you name the
%% required predicates precisely as stated.
%% You can make the assumption that there is never more than one direct connection (door or trap)
%% two specific rooms. 



%% Write a predicate path(X, Y, P) that is true if P is the list of rooms to be traversed
%% to go from room X to room Y.

connected(X,Y) :- door(X,Y); door(Y,X); trap(X,Y).

path(X,Y,P) :-
		travel(X,Y,[X],Q), 
		reverse(Q,P).
       
travel(X,Y,P,[Y|P]) :- 
		connected(X,Y).
       
travel(X,Y,Visited,P) :-
		connected(X,Z),           
		Z \== Y,
		not(member(Z,Visited)),
		travel(Z,Y,[Z|Visited],P).


%% Write a predicate noway(X, Y) that is true if and only if there is no way to
%% go from room X to room Y.

noway(X, Y) :- not(path(X,Y,P)).


%% Write a predicate Redundant(X, Y) that is true if a door or trap from X to Y
%% could be removed from the maze without affecting the existence of a path
%% from any room from any other room. 

redundant(X,Y):- path(X,Y,A), !, path(X,Y,B), A \== B, !.


%% write a predicate isolated(X) that is true if the room X cannot be accessed
%% from any other room in the maze.

door2(X,Y) :- door(X,Y); door(Y,X).
trap2(X,Y) :- trap(Y,X).

isolated(X) :- not(door2(X,Y)), not(trap2(X,Y)).


%% Write a predicate deadlyTrap(X, Y) which is true if the room Y can be reached from the 
%% room X, but there is no way to get back.

deadlyTrap(X,Y) :- path(X,Y,P), !, noway(Y,X).


