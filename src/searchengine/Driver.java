package searchengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import spellchecker.Main;

public class Driver {
	public static void main(String arg[]) throws IOException {

		Scanner s = new Scanner(System.in);
		System.out.println("Welcome to Search Engine\n");
		String input = "";
		String correctedWords = "";
		String choice = "";
		String nextResults = "";
		int pageNumber = 1;
		int numResults = 0;
		int multiplier = 0;
		do {
			System.out.println("Please type word(s) or phrase(s) that you want to search: \n");
			input = s.nextLine();
			System.out.println("\nHow many result(s) would you like to see? \n");
			numResults = s.nextInt();
			s.nextLine();
			// spell checker and word suggestion
			String[] words = input.split(" ");
			StringBuilder sb = new StringBuilder();
			System.out.println("\nSpellchecking...\n");
			for (int i = 0; i < words.length; i++) {
				if (!words[i].matches(".*\\d.*")) {
					correctedWords = Main.suggest(words[i].replaceAll("[^A-Za-z]", ""));
					sb.append(correctedWords + " ");
				} else {
					sb.append(words[i] + " ");
				}
			}
			input = sb.toString();
			do {
				// remove stop words
				StopWords sw = new StopWords();
				ArrayList<String> inputRemoveSW = sw.removeStopWords(input);
				// remove unnecessary part of words by stemming
				Stemmer st = new Stemmer();
				ArrayList<String> inputStem = st.wordsStemming(inputRemoveSW);
//				for (String in : inputStem) {
//					System.out.println(in);
//				}
				// get input words frequency
				FreqList f = new FreqList();
				f.getFreqList(inputStem);
				// sort results
				LinkedHashMap<String, String> sortedResults = f.sortList(numResults, multiplier);
				if (FreqList.hasResults) {
					System.out.println(sortedResults.size() + " related results: \n");
					// print search results in pairs
					sortedResults.entrySet().forEach(entry -> {
						System.out.println(entry.getKey() + "\n" + entry.getValue().replace(".txt", "") + "\n");
					});
					System.out.println("You are currently at page " + pageNumber + "\n");
					System.out.println("Would you like to see the next page? Y/N\n");
					nextResults = s.nextLine();
					System.out.println();
					if (nextResults.toLowerCase().equals("y")) {
						multiplier++;
						pageNumber++;
					}
				} else {
					System.out.println("No more results found\n");
					nextResults = "N";
				}
			} while (nextResults.toLowerCase().equals("y"));
			System.out.println("To continue search type 1. Otherwise, type 2 to EXIT\n");
			choice = s.nextLine();
			System.out.println();
			if (choice.equals("1")) {
				pageNumber = 1;
				multiplier = 0;
				FreqList.hasResults = true;
			}
		} while (choice.equals("1"));
		s.close();
		System.out.println("Thank you for using Search Engine!");
		
	}
}
