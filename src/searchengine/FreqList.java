package searchengine;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FreqList {

	HashMap<String, Integer> freqList;
	LinkedHashMap<String, String> originList;
	protected static boolean hasResults = true;

	public FreqList() {
		this.freqList = new HashMap<>();
		this.originList = new LinkedHashMap<>();
	}

	public void getFreqList(ArrayList<String> query) throws FileNotFoundException {

		File folder = new File("..\\Web Search Engine\\Web Pages");
		File[] listOfFiles = folder.listFiles();
		//Read in all page files
		for (File file : listOfFiles) {
			if (file.isFile()) {
				int occurrence = 0;
				TST<Integer> st = new TST<Integer>();
				//Convert content of file into a string
				StringBuilder sb = new StringBuilder();
				Scanner s = new Scanner(new FileReader(file));
				while (s.hasNextLine()) {
					sb.append(s.nextLine());
				}
				String text = sb.toString();
				//Extract words
				StringTokenizer stk = new StringTokenizer(text);
				while (stk.hasMoreTokens()) {
					String token = stk.nextToken().toLowerCase();
					//Build a TST
					if (st.contains(token)) {
						st.put(token, st.get(token) + 1);
					} else {
						st.put(token, 1);
					}
				}
				//Sum occurrences of each word from input
				for (String word : query) {
					if (st.contains(word)) {
						occurrence += st.get(word);
					}
				}
				//Store summation of occurrences and corresponding file name in hash table
				freqList.put(file.getName(), occurrence);
//				System.out.println(freqList);
			}
		}
//		freqList.entrySet().forEach(entry -> {
//			System.out.println(entry.getKey() + "\n" + entry.getValue() + "\n");
//		});
	}

	public LinkedHashMap<String, String> sortList(int numResult, int multiplier) throws FileNotFoundException {

		ArrayList<Integer> list = new ArrayList<>();
		LinkedHashMap<String, Integer> sortedFreqList = new LinkedHashMap<>();
		ArrayList<String> webpage = new ArrayList<>();
		ArrayList<String> url = new ArrayList<>();
		LinkedHashMap<String, String> completeList = new LinkedHashMap<>();
		//Create a new list to store occurrences
		for (Map.Entry<String, Integer> entry : freqList.entrySet()) {
			list.add(entry.getValue());
		}
		//Sort occurrences by descending order
		Collections.sort(list, Collections.reverseOrder());
		//Generate a new hash table to save all sorted results
		for (int num : list) {
			for (Entry<String, Integer> entry : freqList.entrySet()) {
				if (entry.getValue().equals(num)) {
					sortedFreqList.put(entry.getKey(), num);
				}
			}
//			System.out.println(sortedFreqList);
		}
//		sortedFreqList.entrySet().forEach(entry -> {
//			System.out.println(entry.getKey() + "\n" + entry.getValue() + "\n");
//		});
		//Get a sorted web page list
		for (String key : sortedFreqList.keySet()) {
			webpage.add(key);
		}
		//Get a URL list based on the web page list
		getOriginList();
		for (String page : webpage) {
			url.add(originList.get(page));
		}
		//Check if there is any other results available for user. Also generate a hash table with final page ranking.
		int upperBound = numResult * (multiplier + 1);
		if (upperBound <= webpage.size()) {
			for (int i = numResult * multiplier; i < upperBound; i++) {
				completeList.put(url.get(i), webpage.get(i));
			}
		} else {
			for (int i = numResult * multiplier; i < webpage.size(); i++) {
				completeList.put(url.get(i), webpage.get(i));
			}
		}
		if (completeList.isEmpty()) {
			hasResults = false;
		}
		return completeList;
	}

	private void getOriginList() throws FileNotFoundException {

		ArrayList<String> pageList = new ArrayList<>();
		ArrayList<String> urlList = new ArrayList<>();
		Scanner page = new Scanner(new File("Pages.txt"));
		//Create a list with page file names
		while (page.hasNextLine()) {
			pageList.add(page.nextLine());
		}
		Scanner URL = new Scanner(new File("URLs.txt"));
		//Create a list with URL
		while (URL.hasNextLine()) {
			urlList.add(URL.nextLine());
		}
		//Combine the page names and corresponding URLs
		Iterator<String> itr = urlList.listIterator();
		for (String p : pageList) {
			if (itr.hasNext()) {
				originList.put(p, itr.next());
			}
		}
	}
}
