package searchengine;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static org.jsoup.Jsoup.*;

public class Crawler {
	private static HashSet<String> urls = new HashSet<>();

	public static void getURLs(String url) {
		if (!urls.contains(url)) {
			Document document;
			try {
				document = connect(url).timeout(5000).get();
				//Get 500 URLs starting from designated website
				Elements links = document.select("a[href^=\"https://www.cbc.ca/news\"]");
				for (Element link : links) {
					if (urls.size() < 500) {
						urls.add(url);
						getURLs(link.attr("abs:href"));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void getContent() {
		try {
			FileWriter link = new FileWriter("URLs.txt");
			FileWriter page = new FileWriter("Pages.txt");
			//From the crawled URLs to get web page content
			for (String url : urls) {
				Document document;
				document = connect(url).timeout(5000).maxBodySize(0).get();
				//Save web page content by using page title as the file name
				String fileName = document.title().replace(" | CBC News", "").replaceAll("[^A-Za-z0-9\\s]", "")
						+ ".txt";
				page.write(fileName + System.getProperty("line.separator"));
				//Save files into a folder
				File f = new File("..\\Web Search Engine\\Web Pages\\" + fileName);
				f.getParentFile().mkdirs();
				FileWriter wp = new FileWriter(f);
				wp.write(document.body().text());
				link.write(url + System.getProperty("line.separator"));
				wp.close();
			}
			link.close();
			page.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		getURLs("https://www.cbc.ca/news");
		getContent();
	}
}
