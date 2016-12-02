/*Part 1: Prolog Predicates*/

/*

Exercise 1 (20 points):
Vectors can be represented as lists of numbers, for example a 3 dimensional vector
could be [1, 5, 2] or [2, -4, 8]. There are two very common operations on vectors 
that have the same number of dimensions: sum (aka translation) and dot product. 
The sum of 2 vectors is a vector whose elements are the sum of the corresponding 
elements of the two vectors; for example the sum of the two vectors above is [3, 1, 10]. 

Write a predicate vect_sum/3 that takes 2 lists of numbers of the same length 
and return their sum.

For example:
?- vect_sum([1, 5, 2], [2, -4, 8], V).
V = [3, 1, 10].
*/

/*
The predicate has 3 arguments X and Y, which are lists representing the vectors to be added together. 
S is a list representing the vector which is the sum of X and Y.

The predicate recursively calls itself (stepping through the list by passing the tails as arguments)
until the lists end, each time adding another set of corresponding elements of X and Y (the heads of 
the remaining parts of the lists) together and storing the result in the corresponding element of S.
*/

vect_sum([X],[Y],[S]) :- 	
	S is X + Y.
vect_sum([H1|T1],[H2|T2],[S1|S2]) :- 
	S1 is H1 + H2,
	vect_sum(T1, T2, S2), !.



/*
The dot product of 2 vectors is a scalar value equal to the sum of the products of 
the corresponding elements. Write a predicate dot_product/3 that calculates the 
dot product of two vectors. 

For example:
?- dot_product([1, 5, 2], [2, -4, 8], V).
V = -2.
*/

/*
The dot_product predicate calls two other helpers to calculate the dot product, as the problem
can be divided into two parts.  First we must find the product of the corresponding elements of
the two vectors, then we must add the products together.

The vect_mul predicate returns a list similar to the one returned by vect_sum, except each 
element of the list Z contains the product of the two correspondng elements from vectors 
X and Y instead of the sum.

The scalar predicates implement an accumulator which recursively calls itself to add 
up all of the elements in the list returned by vect_mul.  The final result is the dot product.
*/
		
dot_product(X,Y,P) :- 
	vect_mul(X,Y,Mult),
	scalar(Mult, P), !.

vect_mul([],[],[]).
vect_mul([X],[Y],[Z]) :- 
	Z is X * Y.
vect_mul([H1|T1],[H2|T2], [Z1|Z2]) :- 
	Z1 is H1 * H2,
	vect_mul(T1, T2, Z2).

scalar(Mult,P) :- scalar2(Mult,0,P).
scalar2([],X,X).
scalar2([H|T],X,P) :-
	Y is X + H,
	scalar2(T,Y,P). 
	
	

	
/*
Write the predicate list2set/2 that takes 2 list arguments and succeeds when 
the second argument is equal to the first argument except with no duplicates. 

For example:
?- list2set([1, a,  4, z, b, a, 1, b, z, q], P).
P = [1, a, 4, z, b, q].

Your implementation must not change the order of the elements of the list so 
that the goal below succeeds:
?- list2set([1, a,  4, z, b, a, 1, b, z, q], [1, a, 4, z, b, q]).
true.
*/

/*
The del_dupes predicate removes all of the duplicates in the set by recursively
calling itself (stepping through the elements in the list by passing the tails as arguments).

Each time the predicate checks to see if the current element (head) is the last occurence of 
that element in the list by checking to see if it is a member of the rest of the list (tail).  
IF the element IS a member of the tail than the predicate continues without changing much, but 
if the element is NOT a member of the tail then the current element is appended to the 'Set' list.  
In other words, the predicate steps through the list and places only the last occurence of each 
unique element in the 'Set' list. 

However, we want to maintain the same order as the original list.  To solve this problem the list2set 
predicate can first reverse the original list before passing it to the del_dupes predicate, thus all
of the last unique elements become the first unique elements.  Reversing the returned list again gives
us a list in the same order as the original, but without any duplicates.
*/	

list2set(X,Y) :-
	reverse(X,XR),
	del_dupes(XR,YR), 
	reverse(YR,Y),!.

del_dupes([],[]).
del_dupes([H|T], Set) :-
	member(H, T), 
	del_dupes(T, Set).
del_dupes([H|T], [H|Set]) :-
	\+ member(H, T), 
	del_dupes(T, Set).

	

/*
A travel agent knowledge base includes facts consisting of a functor that denotes a 
mode of transport, a departure city, an arrival city and a price. 

For example:
*/
bus(boston, new_york, 30).
bus(new_york, philadelphia, 25).
bus(philadelphia, new_york, 25).
bus(philadelphia, trenton, 20).
bus(trenton, philadelphia, 20).
bus(washington, richmond, 30).
bus(washington, charlotte, 80).
bus(washington, baltimore, 20).
bus(indianapolis, cleveland, 30).
bus(cleveland, indianapolis, 30).
train(boston, new_york, 50).
train(new_york, philadelphia, 40).
train(richmond, charlotte, 50).
train(philadelphia, washington, 80).
train(philadelphia, pittsburgh, 80).
train(pittsburgh, cincinnati, 60).
train(cincinnati, indianapolis, 30).
train(new_york, cleveland, 120).
plane(washington, boston, 300).
plane(washington, cincinnati, 400).
plane(washington, charlotte, 250).
plane(philadelphia, pittsburgh, 150).
plane(pittsburgh, cleveland, 200).
plane(pittsburgh, indianapolis, 300).
plane(baltimore, indianapolis, 500).

/*
Write a predicate itinerary/4 that takes two cities and returns a list of segments to go 
from the first city to the second and the price of the journey. Each segment should be 
represented by a term where the functor is the mode of travel and the two arguments are 
the departure and arrival city. Successive retries of the predicate should return all the 
possible itineraries between the two cities, without any duplicate nor any cycle. 
The same segment using different modes of transport are not considered duplicate, 
but the same city visited twice in the same itinerary would be considered a cycle.  

For example:
?- itinerary(boston, washington, I, P).
I = [bus(boston, new_york), bus(new_york, philadelphia), train(philadelphia, washington)],
P = 135 ;
I = [bus(boston, new_york), train(new_york, philadelphia), train(philadelphia, washington)],
P = 150 ;
I = [train(boston, new_york), bus(new_york, philadelphia), train(philadelphia, washington)],
P = 155 ;
I = [train(boston, new_york), train(new_york, philadelphia), train(philadelphia, washington)],
P = 170 ;
false.

But the itenerary
I = [bus(boston, new_york), bus(new_york, philadelphia), bus(philadelphia, trenton), bus(trenton, philadelphia)
,train(philadelphia, washington)]
contains a cycle and should not be returned.
*/

/*
The itinerary predicate accepts a departure city (X) and arrival city (Y) names and then calls the travel 
predicate which returns 2 lists.  The 'I' list contains data for each of the connections between cities 
that must be passed through to get from X to Y.  'Plist' contains the prices for each of the connections.

The travel predicate first calls the connection predicates to find a connection between two cities that is on the 
path between X and Y.  The connection predicate returns the data for the connections including the mode of 
transport, the two cities, and the price.

The second travel predicate then attempts to find another connection along the path from X to Y using the connection 
predicates.  IF another connection does NOT exist then we have reached the final destination and can exit the travel 
predicate.  IF another connection DOES exist then the the predicate checks to see if the second connecton backtracks 
to any city along the path that we have already established.  IF the connection DOES backtrack then the travel predicate 
will try a different connection, and if the connection does NOT backtrack then the arrival city is added to the 'Visited' 
list and the data found by the connection predicate is added to the 'I' and 'Plist' lists.

After the final destination is reached by the travel predicate the itinerary predicate calls the acc predicate, which is 
an accumulator that sums up all of the elements in the 'Plist' list to return the total price of the entire journey (P).				
*/

connection(X,Y,(bus(X,Y)),P) :- bus(X,Y,P).
connection(X,Y,(train(X,Y)),P) :- train(X,Y,P).
connection(X,Y,(plane(X,Y)),P) :- plane(X,Y,P).

itinerary(X,Y,I,P) :- 
	travel(X,Y,[X],I,Plist),
	acc(Plist,P).

travel(X,Y,_,[I],[Plist]) :- 
	connection(X,Y,I,Plist).

travel(X,Y,Visited,[D|A],[H|T]) :- 
	connection(X,Z,D,H),
	Z \== Y,
	\+ (member(Z,Visited)),
	travel(Z,Y,[Z|Visited],A,T).

acc(Plist,P) :- acc2(Plist,0,P).
acc2([],X,X).
acc2([H|T],X,P) :-
	Y is X + H,
	acc2(T,Y,P). 
	
	
	
							
/*
In class we have seen a DCG for a simple subset of English
that generates a parse tree. Here is a copy of these DCG
where the ability of handling pronouns has been removed.
*/
s(s(NP,VP)) --> np(NP), vp(VP).
np(np(Det,N)) --> det(Det), n(N).              
vp(vp(V,NP)) --> v(V), np(NP).
vp(vp(V)) --> v(V).
det(det(the)) --> [the]. 
det(det(a)) --> [a].              
n(n(woman)) --> [woman].       
n(n(man)) --> [man].
v(v(shoots)) --> [shoots].

/*
Augment the grammar to handle the adjectives good, fat and old so that your parser 
can parse the sentences:
[the, good, woman, shoots, the, fat, man]
and returns the parse tree 
s(np(det(the), ap(adj(good), n(woman))), vp(v(shoots), np(det(the), ap(adj(fat), n(man))))) 
or
[the, good, man, shoots]
returning the parse tree
s(np(det(the), ap(adj(good), n(man))), vp(v(shoots)))

For simplicity, you can assume a single attribute per noun phrase. 
For example the sentence
[a, fat, good, old, fat, man, shoots, the, woman]
does not need to be considered grammatical.
Remember that to parse a sentence, such as the one above you enter the Prolog query:
s(R, [a, fat, man, shoots, the, woman],[]).
R = s(np(det(a), ap(adj(fat), n(man))), vp(v(shoots), np(det(the), n(woman))))
*/

/*
There is already a rule allowing the np (noun phrase) list to include a det (determiner) followed 
by a noun (n).  The first rule I added allows the np list to alernatively include a determiner 
followed by an ap (adjective phrase) list.  I did not replace or alter the original np rule because
doing so would make the grammar unable to parse sentences that do not contain adjectives.
  
The second rule I added is for the ap (adjective phrase) list itself.  It must include an adjective 
(adj) followed by a noun (n).

The next three rules just list the possible adjectives (good, fat, and old).
*/

np(np(Det,Ap)) --> det(Det), ap(Ap).  
ap(ap(Adj,N)) --> adj(Adj), n(N). 
adj(adj(good)) --> [good].       
adj(adj(fat)) --> [fat].
adj(adj(old)) --> [old].
