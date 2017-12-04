import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
	
	public static int revCount = 0;
	
	public static ArrayList<Review> readCSV(String csvFile) {
		
		String line = "";
		ArrayList<Review> arrReviews = new ArrayList<Review>();
	     
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

	    	line = br.readLine();
	    	
	    	while ((line = br.readLine()) != null) {
	    		Review review = createReview(line);
	    		arrReviews.add(review);
	    	}
	    	
	    } catch (IOException e) {
	        e.printStackTrace();    
	    }
	    
	    return arrReviews;
		
	}
	
	public static Review createReview(String line) {
		
		 String[] reviewSplit = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		 //String[] reviewSplit = line.split(cvsSplitBy);
		 
		 int revID = revCount++;
		 
		 String prodID = reviewSplit[4];
		 
		 //rating is set to -1 in case a rating wasn't provided
		 double rating = -1.0;
		 if (reviewSplit[6] != "") {
			 rating = Double.parseDouble(reviewSplit[6]);
		 }
		 
		 String Date = reviewSplit[5];
		 
		 String reviewText = reviewSplit[7];
		 
		 Review review = new Review(revID, prodID, rating, Date, reviewText);
		 return review;
	}
	
}
