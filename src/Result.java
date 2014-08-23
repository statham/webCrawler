/**
 * Class representing a single result from www.shopping.com
 */
public class Result {
	private String name;
	private String price;
	private String shippingPrice;
	private String vendor;
	
	public Result(String name, String price, String shippingPrice, String vendor){
		this.name = name;
		this.price = price;
		this.shippingPrice = shippingPrice;
		this.vendor = vendor;
	}
	
	/**
	 * implements the toString method
	 * @return String representing information about result
	 */
	public String toString(){
		String newLine = "\n";
		String name = "Product Name: " + this.name + newLine;
		String price = "Product Price: " + this.price + newLine;
		String shippingPrice = "Shipping Price: " + this.shippingPrice + newLine;
		String vendor = "Product Vendor: " + this.vendor;
		return name + price + shippingPrice + vendor;
	}
	
	/**
	 * implements the equals method
	 * @param an object to compare to
	 * @return boolean true if equal, else false
	 */
	public boolean equals(Object o){
		//is it the object?
		if(this == o){
			return true;
		}
		//are they the same type?
		if(!(o instanceof Result)){
			return false;
		}
		//check values
		Result resultO = (Result) o;
		if(this.name.equals(resultO.name) && this.price.equals(resultO.price) && this.shippingPrice.equals(resultO.shippingPrice) && this.vendor.equals(resultO.vendor)){
			return true;
		}
		return false;
	}
	
	/**
	 * implements the hashCode method for Result class
	 * @return int representing the hash of the result
	 */
	public int hashCode(){
		return 1001*this.name.hashCode() - 37*this.price.hashCode() + this.shippingPrice.hashCode()/this.vendor.hashCode();
	}
}
