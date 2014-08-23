import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Web scraper for www.shopping.com
 *
 */
public class WebScraper {
	
	/**
	 * 
	 * @param args
	 * 		first arg is String keyword to search for
	 * 		second (optional) arg is int page number
	 * @throws IOException
	 * 
	 * main method does not return, but prints to the console either the
	 * total number of results if one argument supplied, or result objects
	 * for each result on the specified page if two arguments given
	 * 
	 */
	public static void main(String[] args) throws IOException {
		String base_url = "http://www.shopping.com/products";
		
		if(args.length > 2 || args.length < 1) {
			//bad input
			System.out.println("Scraper requires one or two arguments");
			System.exit(1);
		}
		
		//search for keyword
		Document doc = Jsoup.connect(base_url).data("KW", args[0]).get();
		
		if (doc.getElementById("noResults") != null){
			//no results matched query
			System.out.println("No results matched your query");
			System.exit(1);
		}
		
		String currentUrl = doc.location();
		if(args.length == 1){
			//Query type 1
			Elements totalResultContainers = doc.getElementsByClass("numTotalResults");
			//total results text has form "Results x - y of z"
			String[] totalWords = totalResultContainers.first().text().split(" ");
			//want last token
			String totalResult = totalWords[totalWords.length-1];
			System.out.println("Total number of results: " + totalResult);
			System.exit(0);
		}
		//else query type 2
		else {
			try {
				//this will check if second argument is integer
				Integer.parseInt(args[1]);
				//currently on first page of results
				doc = Jsoup.connect(currentUrl).data("PG", args[1]).get();
				
				if (doc.getElementById("noResults") != null){
					//no results matched query
					System.out.println("No results matched your query");
					System.exit(1);
				}

				//all results have class gridBox
				Elements results = doc.getElementsByClass("gridBox");
				for(Element result : results){
					//get name of result
					Elements productNames = result.getElementsByClass("productName");
					String name;
					if (productNames.first().hasAttr("title")){
						name = productNames.first().attr("title");
					}
					else {
						//when matching keywords in product names
						name = productNames.first().select("span").first().attr("title");
					}
					//get price of result
					String price = result.getElementsByClass("productPrice").first().text();

					//get shipping price
					String shippingPrice;
					if (!(result.getElementsByClass("taxShippingArea").isEmpty())){
						shippingPrice = result.getElementsByClass("taxShippingArea").first().text();
					}
					else if (!(result.getElementsByClass("freeShip").isEmpty())){
						//free shipping has different structure in dom
						shippingPrice = result.getElementsByClass("freeShip").first().text();
					}
					else {
						shippingPrice = "No shipping information";
					}
					
					//get vendor(s)
					String vendor;
					if(!(result.getElementsByClass("buyAtTxt").isEmpty())){
						//more than one store
						vendor = result.getElementsByClass("buyAtTxt").first().text();
					}
					else {
						//only one store, get specific name
						vendor = result.getElementsByClass("newMerchantName").first().text();
					}
					
					//create result object
					Result pageResult = new Result(name, price, shippingPrice, vendor);
					System.out.println(pageResult.toString());
					System.out.println("");
				}
				System.exit(0);
			}
			catch (NumberFormatException e){
				System.out.println("Please supply an integer as second argument");
				System.exit(1);
			}
		}
	}

}
