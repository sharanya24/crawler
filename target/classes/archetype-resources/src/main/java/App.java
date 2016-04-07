package $com.pramati.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
       Connection con= Jsoup.connect("http://mail-archives.apache.org/mod_mbox/maven-users/");
       Document doc=con.get();
       Element ele=doc.text("2014");
       System.out.println(ele);
    }
}
