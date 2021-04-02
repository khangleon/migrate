package vn.com.skyone.app2sky;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Test {

	public Test() {
		super();
	}

	public static void main(String[] args) throws IOException {
		long t1 = System.currentTimeMillis();
		System.out.println("=============================== Test ===============================");

		Document doc = Jsoup.connect("https://skyhub.suntech.com.vn:8888/hub/faces/login.xhtml").get();
		String title = doc.html();
		System.out.println(title);

		System.out.println("Done. " + Lib.elapsedTime(t1, System.currentTimeMillis()));
	}

}
