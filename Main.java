import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static ArrayList<Review> arrReviews;
	
	public static void main(String[] args) {

		arrReviews = CSVReader.readCSV("D:/University/Year 3/Project/Datasets/Set3/HotelDS1000.csv");
		
		for (Review r : arrReviews) {
			System.out.println("ReviewID: " + r.getReviewID());
			System.out.println("ProductID: " + r.getProductID());
			System.out.println("Rating: " + r.getRating());
			System.out.println("Date: " + r.getDate());
		    System.out.println("Review: " + r.getReview());
		    System.out.println("");
		}
		
	}
	
}
