import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.*;

public class FIM {

	int minsup;
	BitSet b1;
	BitSet b2;
	String fileName;
	List<FrequentItem> unique_items;
	static Scanner sc = new Scanner(System.in);

	public static String getInputFileName() {
		System.out.println("Enter file to be taken as input:-");
		
		String file = sc.nextLine();
		
		return file;
	}
	public static String createOutputFileName(String filename) {
		String outputfile = "Output_of_"+filename.substring(0,filename.indexOf("."))+".txt";
		return outputfile;
	}

	// method to read parameters of file ( bucket and min sup)
	public int[] no_of_baskets_and_minsup(BufferedReader br) throws IOException {
		String line1 = br.readLine();
		return new int[] { Integer.parseInt(line1.split(" ")[0]), Integer.parseInt(line1.split(" ")[1]) };
	}

	public int hashPair1(int a, int b) {
		int h1 = (a ^ b) % 107 ;
		return (h1);
	}

	public int hashPair2(int a, int b) {
		int h2 = (a | b) % 107;
		return (h2);
	}

	public int hashCandidate1(String str) {
		String[] items = str.split(",");
		int hashValue = Integer.parseInt(items[0]);

		for (int i = 1; i < items.length; i++) {
			hashValue = hashValue ^ Integer.parseInt(items[i]);
		}
		return hashValue % 107;
	}

	public int hashCandidate2(String str) {
		String[] items = str.split(",");
		int hashValue = Integer.parseInt(items[0]);

		for (int i = 1; i < items.length; i++) {
			hashValue = hashValue | Integer.parseInt(items[i]);
		}
		return hashValue % 107;
	}

	public int hashBasket1(String str, int item) {
		String[] items = str.split(",");
		int hashValue = item;

		for (int i = 0; i < items.length; i++) {
			hashValue = hashValue ^ Integer.parseInt(items[i]);
		}
		return hashValue % 107;
	}

	public int hashBasket2(String str, int item) {
		String[] items = str.split(",");
		int hashValue = item;

		for (int i = 0; i < items.length; i++) {
			hashValue = hashValue | Integer.parseInt(items[i]);
		}
		return hashValue % 107;
	}

	public List<FrequentItem> checkSupport(List<FrequentItem> items) {
		int size = items.size();
		for (int i = 0; i < size; i++) {
			if (items.get(i).count < minsup) {
				items.remove(i);
				size--;
				i--;
			}
		}
		return items;
	}

	// to add FrequentItem Object or candidate to FreqItems List
	public void countFrequentItem(FrequentItem item) {
		int j = 0;
		for (FrequentItem f : unique_items) {
			if (f.itemSet == item.itemSet) {
				f.count++;
				j = 1;
				break;
			} else
				j = 0;
		}
		// item does not exist in list . add to list
		if (j == 0) {
			item.count = 1;
			unique_items.add(item);
		}
	}

	// to add FrequentItem Object or candidate to FreqItems List if only string
	// is passed
	public void countFrequentItem(String item, int pass) {
		int j = 0;
		for (FrequentItem f : unique_items) {
			if (f.itemSet == item) {
				f.count++;
				j = 1;
				break;
			} else
				j = 0;
		}
		// item does not exist in list . add to list
		if (j == 0) {

			FrequentItem newItem = new FrequentItem();
			newItem.itemSet = item;
			newItem.length = pass;
			newItem.count = 1;
			unique_items.add(newItem);
		}
	}

	public void addCandidatesToList(Map<String, Integer> candidates, int pass) {
		for (String s : candidates.keySet()) {
			if (candidates.get(s) >= minsup) {
				FrequentItem f = new FrequentItem();
				f.itemSet = s;
				f.count = candidates.get(s);
				f.length = pass;
				unique_items.add(f);
			}
		}
	}

	public void pass1(String file) throws Exception {
		unique_items = new ArrayList<FrequentItem>();
		try {
			for (int x = 0; x < 100; x++) {
				FrequentItem f = new FrequentItem();
				f.itemSet = "" + x;
				f.length = 1;
				unique_items.add(f);
			}

			int[] hash1 = new int[107];
			int[] hash2 = new int[107];

			BufferedReader inputbr = new BufferedReader(new FileReader(file));
			int res[] = no_of_baskets_and_minsup(inputbr);
			int baskets = res[0];
			minsup = res[1];
			System.out.println("Number of baskets:" + baskets);
			System.out.println("Min support is:" + minsup);

			// pass 1 logic
			String basket = null;

			int h1, h2;
			while ((basket = inputbr.readLine()) != null) { // for each basket

				String[] temp = basket.split(",");
				int tempLength = temp.length;
				for (int i = 1; i < tempLength; i++) { // for each item in
														// basket
					int t = Integer.parseInt(temp[i]);
					if (unique_items.get(t) != null || unique_items.get(t).itemSet != "") {
						FrequentItem f = unique_items.get(t);
						f.count++;
					} else {
						FrequentItem f = new FrequentItem();
						f.itemSet = "" + t;
						f.length = 1;
						f.count++;
						unique_items.set(t, f);
					}
				}

				// For each pair of items in a basket..
				// hash the pair to a bucket;
				// add 1 to the count of that bucket;
				for (int i = 1; i < tempLength; i++) {
					for (int j = (i + 1); j < tempLength; j++) {
						h1 = hashPair1(Integer.parseInt(temp[i]), Integer.parseInt(temp[j]));
						h2 = hashPair2(Integer.parseInt(temp[i]), Integer.parseInt(temp[j]));
						hash1[h1]++;
						hash2[h2]++;
					}
				}
				
			}
			inputbr.close();
			unique_items = checkSupport(unique_items);
			
		
			
			b1 = new BitSet(hash1.length);
			b2 = new BitSet(hash2.length);

			for (int j = 0; j < hash2.length; j++) {
				if (hash2[j] >= minsup) {
					b2.set(j);
				}
				if (hash1[j] >= minsup) {
					b1.set(j);	
				}
			}
			//System.out.println("Pass 1 done");
		} catch (Exception ex) {
			System.out.println("Error in Phase 1 :" + ex.getMessage() + "\n\n" + ex.getStackTrace());
		}
	}


	public boolean pass2(List<FrequentItem> lstFreqItems, BitSet bit1, BitSet bit2, int pass) {
		try {
			// candidate itemsets for the next pass using lstFrequentItems
			// considering both bit vectors
			ArrayList<String> lstCandidate = new ArrayList<String>();
			
			// form candidates directly from lstFrequentItems
			for (int i = 0; i < lstFreqItems.size(); i++) {
				if (lstFreqItems.get(i).length == pass - 1) {
					for (int j = i + 1; j < lstFreqItems.size(); j++) {
						if (lstFreqItems.get(j).length == pass - 1) {
							// combining condition
							String item1 = lstFreqItems.get(i).itemSet;
							String item2 = lstFreqItems.get(j).itemSet;
						
							if (pass == 2) {
								int k = hashPair1(Integer.parseInt(item1), Integer.parseInt(item2));
								int l = hashPair2(Integer.parseInt(item1), Integer.parseInt(item2));

								// add to candidate or lstFreqItems
								if (bit1.get(k) && bit2.get(l)) {
									lstCandidate.add(item1 + "," + item2);
								}
								// pass 3 and above;
								// if first n characters are same , append the next
								// to make candidate set
							} else if (item1.substring(0, item1.lastIndexOf(','))
									.equals(item2.substring(0, item1.lastIndexOf(','))))
							
							{
								String newItem1 = item1.substring(item1.lastIndexOf(',') + 1);
								String newItem2 = item2.substring(item2.lastIndexOf(',') + 1);
								String candidateSet = item1.substring(0, item1.lastIndexOf(',') + 1);
								if (Integer.parseInt(newItem1) > Integer.parseInt(newItem2)) {
									candidateSet = candidateSet + newItem2 + "," + newItem1;
								} else {
									candidateSet = candidateSet + newItem1 + "," + newItem2;
								}

								int k = hashCandidate1(candidateSet);
								int l = hashCandidate2(candidateSet);
								if (bit1.get(k) && bit2.get(l)) {
									lstCandidate.add(candidateSet);
								}
							}
						}
					}
				}
			}
			//System.out.println(lstCandidate);
			System.out.println(pass + "  candidate generation done");
			int candidatelstSize = lstCandidate.size();
			//System.out.println(lstCandidate);
			System.out.println(candidatelstSize);
			// Exit condition for the algo

			if (candidatelstSize <= 0) {
				return false;
			}

			// count occurrence of each candidate
			BufferedReader inputReader = new BufferedReader(new FileReader(fileName));
			inputReader.readLine();
			String basket;
			int[] hash1 = new int[107];
			int[] hash2 = new int[107];

			Map<String, Integer> counts = new HashMap<String, Integer>();
			while ((basket = inputReader.readLine()) != null) {
				basket = basket.substring(basket.indexOf(',') + 1);
			
				List<Integer> basketItems = Arrays.asList(basket.split(","))
												  .stream()
												  .map(s -> Integer.parseInt(s))
												  .collect(Collectors.toList());

				int basketLength = basketItems.size();

				// check length condition on basket
				if (basketLength < pass) {
					continue;
				}

				for (String candidate : lstCandidate) {
					
					int[] candidateItems = Arrays.stream(candidate.split(","))
												 .mapToInt(Integer::parseInt)
												 .toArray();
				
					int candidateLength = candidateItems.length;
					int flag = 1;
					for (int i = 0; i < candidateLength; i++) {
						if (!basketItems.contains(candidateItems[i])) {
							flag = 0;
							break;
						}
					}
					if (flag == 1) {
						// add candidate to frequent items list
						// increment count for candidate

					
						if (counts.containsKey(candidate)) {
							counts.put(candidate, counts.get(candidate) + 1);
						} else {
							counts.put(candidate, 1);
						}
						// generate hash for next pass
						for (int j = 0; j < basketLength; j++) {
							if (basketItems.get(j) > candidateItems[candidateLength - 1])
							/*because candidates are generated in ascending order like {0,1} {1,2} {2,3}..
							 * Hence next next pairing for 3 candidate set can be formed with {0,1} only if basketitem being 
							 * checked(eg. 10)  is bigger than 1 in {0,1}   which gives eg {0,1,10)
							 */
							{
								int h1 = hashBasket1(candidate, basketItems.get(j));
								int h2 = hashBasket2(candidate, basketItems.get(j));
								hash1[h1]++;
								hash2[h2]++;
							}
						}
					}
				}
			}
			// add hash key to unique list
			
			addCandidatesToList(counts, pass);
			inputReader.close();

			b1 = new BitSet(hash1.length);
			b2 = new BitSet(hash1.length);
			for (int j = 0; j < hash2.length; j++)
			{
				if (hash2[j] >= minsup)
					b2.set(j);
				if (hash1[j] >= minsup) {
					b1.set(j);
				}
			}
			return true;
		} catch (Exception ex) {
			System.out.println("Error in Phase 2 :" + ex.getMessage() + "\n\n" + ex.getStackTrace());
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		String filename = getInputFileName();
		long t1 = System.currentTimeMillis();

		FIM obj = new FIM();
		obj.fileName = filename;
		int pass = 2;
		obj.pass1(filename); 
		while (obj.pass2(obj.unique_items, obj.b1, obj.b2, pass)) {
			//System.out.println("pass : " + pass);
			pass++;
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Time mili " + (t2 - t1));
		
		//Displaying Frequent Itemsets and their respective counts
		
	/*	System.out.println("Itemset :: ");
		for (int x = 0; x < obj.unique_items.size(); x++) {
			FrequentItem f = obj.unique_items.get(x);
			System.out.println(f.itemSet + "  Count : " + f.count);
		}
	*/
		
		//Printing output to a file
		BufferedWriter bw = new BufferedWriter(new FileWriter(createOutputFileName(filename)));
		for(FrequentItem f: obj.unique_items) {
			bw.write("{"+f.itemSet+"}" +" - "+f.count);
			bw.newLine();
			
	}
		bw.close();
}}