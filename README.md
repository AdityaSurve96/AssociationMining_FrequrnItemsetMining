# AssociationMining_FrequrnItemsetMining

The implementation is an algorithm for mining frequent pairs from market basket data. 
The input to the algorithm will be n baskets, each containing m items. 
Each item is a natural number from the set {0, 1, 2, . . . , 99}. 
The support threshold t, where t ≥ 3(usually should be) , is also part of the input.


Your algorithm should output all frequent itemsets
(not just all pairs) {i1, i2, . . . , ik} ⊆ {0, 1, . . . , 99}^k whose support is at least t.

The algorithm should read the input as Space delimited text files,
where each basket is given as basket#, item1, item2, . . . , itemm.

Every line in the Input file indicates a basket of items. basket# indicates the basket number and basket numbers
are positive integers.

The application will then output the frequent itemsets in each line, in the form {i1, i2, . . . , ik} − Support
where “Support” indicates the support of itemset {i1, i2, . . . , ik}.


Prior to running thr algorithm , GenerateFile.java should be executed to generate the input file.
