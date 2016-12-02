-- #1 Logical Or
or1 :: Bool -> Bool -> Bool
or1 True True = True
or1 False False = False
or1 x y = True

{-Examples: 
	- or1 True True = True
	- or1 True False = True
	- or1 False True = True
	- or1 False False = False
-}


-- #2 Left and Right
left :: (a,b) -> a
left (x,_) = x

right :: (a,b) -> b
right (_,y) = y

{-Examples: 
	- left (2,5) = 2
	- right (3,7) = 7
-}


-- #3 Split
split :: [a] -> ([a], [a]) 
split xs | size <= 1 = (xs, []) 
         | size > 1 = (take half xs, drop half xs)
   where size = length xs 
         half = size `div` 2

{-Examples: 
	- split [1,3,4,9,11,12] = ([1,3,4],[9,11,12])
	- split [1,2,12,3,13,11,7,14,5]	= ([1,2,12,3],[13,11,7,14,5])
-}


-- #4 Merge
merge :: Ord a => [a] -> [a] -> [a]
merge xs [] = xs
merge [] ys = ys
merge (x:xs) (y:ys) 
         | (x <= y)  = x:(merge xs (y:ys)) 
         | otherwise = y:(merge (x:xs) ys)

{-Examples: 
	- merge [1,3,7,8] [2,4,5,6] = [1,2,3,4,5,6,7,8]
  	- merge [1,2,5,7,9,11,17] [3,4,6,8,13] = [1,2,3,4,5,6,7,8,9,11,13,17]
-}


-- #5 Merge Sort
mergesort :: Ord a => [a] -> [a]
mergesort [] = []
mergesort [x] = [x]
mergesort xs = merge (mergesort (fst)) (mergesort (snd))
  where
    (fst,snd) = split xs

{-Examples: 
	- mergesort [7,4,5,2,8,1,10,3,6,9] = [1,2,3,4,5,6,7,8,9,10]
	- mergesort [100,99..1] = [1..100]
-}


-- #6 Towers of Hanoi
type Peg = String
type Move = (Peg, Peg)
hanoi :: Integer -> Peg -> Peg -> Peg -> [Move]

hanoi 0 _ _ _ = []
hanoi n a b c = hanoi (n-1) a c b ++ [(a,c)] ++ hanoi (n-1) b a c

{-Example:
	- hanoi 3 "l" "m" "r" = [("l","r"),("l","m"),("r","m"),("l","r"),("m","l"),("m","r"),("l","r")]
-}