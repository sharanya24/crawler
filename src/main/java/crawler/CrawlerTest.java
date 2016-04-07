package crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerTest {

	static Properties prop = new Properties();
	InputStream io = null;

	public CrawlerTest() throws IOException {

		io = new FileInputStream("src/main/resources/resources/crawlProps.properties");
		if (io != null) {
			prop.load(io);// load properties file content

		}

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CrawlerTest ct = new CrawlerTest();

		Connection con = Jsoup.connect(prop.getProperty("basePath"));// load the
																		// url
		Document doc = con.get();
		Elements links = doc.select("a[href^=" + prop.getProperty("year") + "]").select("a[href$=thread]"); // month
																											// links
																											// of
																											// corresponding
																											// year
		Set<String> linkNames = new LinkedHashSet<String>();

		for (Element link : links) {
			linkNames.add(link.attr("href").replaceFirst("/thread", ""));// push
																			// month
																			// links
																			// into
																			// object
		}
		// extractData(linkNames);
		extractMailContent(linkNames);

	}

	public static void extractData(Set<String> link) {

		Iterator<String> iterate = link.iterator();

		while (iterate.hasNext()) {

			URL url;
			try {
				String month = iterate.next();
				url = new URL(prop.getProperty("basePath") + month);
				File file = new File("/home/sharanyak/Desktop/" + month + ".txt");
				FileUtils.copyURLToFile(url, file);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void extractMailContent(Set<String> linkNames) {

		Document doc = null;
		Connection con = null;
		Iterator<String> listIterator = linkNames.iterator();
		Set<String> sortedPagesLink = new LinkedHashSet<String>();
		try {
			// while(listIterator.hasNext()){
			String month = listIterator.next();
			System.out.println(month);
			con = Jsoup.connect(prop.getProperty("basePath") + month + "/date");
			doc = con.get();
			Elements pages = doc.select("table[id^=msglist]").select("th[class^=pages]").select("a[href]");// fetch
																											// all
																											// pages
																											// links
			for (Element pg : pages) {
				sortedPagesLink.add(pg.attr("href").substring(22));// get the
																	// links of
																	// page
			}
			// }
			System.out.println("size is " + sortedPagesLink.size());
			Iterator<String> it = sortedPagesLink.iterator();
			while (it.hasNext()) {
				System.out.println(it.next());
				doc = Jsoup.connect(prop.getProperty("basePath") + it.next()).get();
				Elements links = doc.select("td[class^=subject]").select("a[href]");
				Set<String> hrefVal = new LinkedHashSet<String>();

				for (Element link : links) {
					hrefVal.add(link.attr("href"));// message link value
				}

				Iterator<String> hrefIterator = hrefVal.iterator();
				int count = 0;
				while (hrefIterator.hasNext()) {
					count++;
					Document document = Jsoup
							.connect(prop.getProperty("basePath") +month+"/"+ hrefIterator.next()).get(); // load
																													// the
																													// url
					System.out.println(document.select("table[id=msgview]").select("tbody").text());
				}
				System.out.println("total count is" + count);

			}
			// System.out.println(sortedPagesLink.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}