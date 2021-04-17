package spellchecker;

import java.io.IOException;

public class Main {
	
	public static String suggest(String word) throws IOException {
		
		String dictionaryFileName = ("..\\Web Search Engine\\alphawords.txt");
		String inputWord = word;
		
		ISpellCorrector corrector = new SpellCorrector();
		
		corrector.useDictionary(dictionaryFileName);
		String suggestion = corrector.suggestSimilarWord(inputWord);
		if (suggestion == null) {
		    suggestion = "No similar word found";
		}
		else if (!suggestion.equals(word.toLowerCase()))
		System.out.println("Suggestion is: " + suggestion + "\n");
		return suggestion;
	}

}
