import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.nio.file.*;



public class GenerateFile {
	static Scanner in = new Scanner(System.in);
	public static void main(String[] args) throws IOException{
	
	String fileName=getName();
	try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))){
		
		int[] noOfBasketsAndMinsup=getNoOfBasketsAndMinsup();
		if(noOfBasketsAndMinsup[0]>0 && noOfBasketsAndMinsup[1]>0)
		{
			int[] items = getItemsPerBasket();
			if((items[0]>items[1]) && (items[0]>0) && (items[1]>0)) {
				try {
				writer.write(Integer.toString(noOfBasketsAndMinsup[0]) +" "+ Integer.toString(noOfBasketsAndMinsup[1]));
				
				writer.newLine();
				Random r= new Random();
				IntStream.rangeClosed(1,noOfBasketsAndMinsup[0])
						 .forEach(basket -> {
							 	
								try {
									writer.write(Integer.toString(basket));
								} catch (IOException e) {
									e.printStackTrace();
								}
								r.ints( (items[1] + r.nextInt(items[0]-items[1]+1)),  0, 100)
							     .forEach(item -> {
							    	
										try {
											 
											 writer.write(","+ Integer.toString(item));
											
										} catch (IOException e) {
											e.printStackTrace();
										}
								  													
							 	   });
								try {
									writer.newLine();
								} catch (IOException e) {
									e.printStackTrace();
								}
						  });
					System.out.println("File "+fileName+" created");		
					}
				catch (Exception e) {
					e.printStackTrace();
					}
			}
				else {
					writer.close();
					System.out.println("Maximum items should be higher than minimum items and both greater than 0");
					Files.deleteIfExists(Paths.get(fileName));
					System.out.println("File Deleted;  Try again ");

				}
		}
			else {
				writer.close();
				System.out.println(
						"Number of baskets and Minimum support must be greater than 0 \nfor the file to be generated properly");
				Files.deleteIfExists(Paths.get(fileName));
				System.out.println("File Deleted;  Try again ");
			}
	}	     
	catch (FileNotFoundException e) {
		e.printStackTrace();
	}
		
	}

 
	public static String getName() {
		System.out.println("Enter File name to be generated (ExampleFile.txt) :-");
		String file = in.nextLine();
		return file;
	}
	
	public static int[] getNoOfBasketsAndMinsup() {
		int[] noOfBasketsAndMinsup = new int[2];
		System.out.println("Enter number of baskets (Integer) :-");
		noOfBasketsAndMinsup[0] = in.nextInt();
		System.out.println("Enter min support (Integer) :-");
		noOfBasketsAndMinsup[1] = in.nextInt();
		return noOfBasketsAndMinsup;
	}
	
	public static int[] getItemsPerBasket() {
		int[] itemsbound = new int[2];
		System.out.println("Enter maximum number of items per basket (Integer) :-");
		itemsbound[0] = in.nextInt();
		System.out.println("Enter minimum number of items per basket (Integer) :-");
		itemsbound[1] = in.nextInt();
		return itemsbound;
	}
}
