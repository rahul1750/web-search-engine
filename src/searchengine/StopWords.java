package searchengine;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StopWords {
	public ArrayList<String> removeStopWords(String query) {
		
		ArrayList<String> allWords = null;
		try {
			Scanner s = new Scanner(new File("StopWords.txt"));
			ArrayList<String> stopwords = new ArrayList<>();
			while (s.hasNextLine()) {
				stopwords.add(s.nextLine());
			}
			allWords = Stream.of(query.replaceAll("[^A-Za-z0-9\\s]", "").toLowerCase().split(" "))
					.collect(Collectors.toCollection(ArrayList<String>::new));
		    allWords.removeAll(stopwords);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    return allWords;
	    
	}
}
