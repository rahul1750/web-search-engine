package searchengine;
import java.util.ArrayList;
import org.tartarus.snowball.ext.englishStemmer;

public class Stemmer {
	public ArrayList<String> wordsStemming(ArrayList<String> query) {
		
		ArrayList<String> wordsStemming = new ArrayList<>();
		for (String word : query) {
			englishStemmer es = new englishStemmer();
			es.setCurrent(word);
			es.stem();
			wordsStemming.add(es.getCurrent());
		}
		return wordsStemming;
		
	}
}
