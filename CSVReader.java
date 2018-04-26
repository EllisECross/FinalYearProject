import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVReader {
	
	public final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
	public static int revCount;
	public static ArrayList<Review> arrReviews = new ArrayList<Review>();
	
	public static ArrayList<Review> readCSV(String csvFile) {
		
		arrReviews.clear();
		revCount = 0;
		String line = "";
	     
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

	    	line = br.readLine();
	    	
	    	while ((line = br.readLine()) != null) {
	    		Review review = createReview(line);
	    		arrReviews.add(review);
	    	}
	    	
	    } catch (IOException e) {
	        e.printStackTrace();    
	    }
	    System.out.println("Number of reviews processed: " + arrReviews.size());
	    arrayCleanUp();
	    
	    return arrReviews;
		
	}
	
	private static Review createReview(String line) {
		
		 String[] reviewSplit = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		 
		 int revID = revCount++;
		 
		 String prodID = reviewSplit[4];
		 
		 //rating is set to -1 in case a rating wasn't provided
		 double rating;
		 try {
			 rating = Double.parseDouble(reviewSplit[6]);
		 } catch (Exception e) {
			 rating = -1.0;
		 }
		 
		 String dateStr = reviewSplit[5];
		 
		 Date date;
		 try {
			 date = df.parse(dateStr);
		 } catch (Exception e) {
			 date = new Date(0,0,0);
		 }
		 
		 String reviewText = reviewSplit[7];
		 
		 String reviewTitle = "";
		 try {
			 reviewTitle = reviewSplit[8];
		 } catch (Exception e) {
			 reviewTitle = "";
		 }
		 
		 String reviewer = "";
		 try {
			 reviewer = reviewSplit[9];;
		 } catch (Exception e) {
			 reviewer = "";
		 }
		 
		 int deceptive;
		 try {
			 deceptive = Integer.parseInt(reviewSplit[10]);
		 } catch (Exception e) {
			 deceptive = -1;
		 }
		 
		 Review review = new Review(revID, prodID, rating, date, reviewText, reviewTitle, reviewer, deceptive);
		 return review;
	}
	
	private static void arrayCleanUp() {
		
		ArrayList<Review> invalidReviews = new ArrayList<Review>();
		int count = 0;
		
		for (Review r : arrReviews) {
			if (r.getRating() == -1.0) {
				count++;
				invalidReviews.add(r);
			} else {
				Date testDate = new Date(0,0,0);
				if (r.getDate().equals(testDate)) {
					count++;
					invalidReviews.add(r);
				}
			}
		}
		
		for (Review r2 : invalidReviews) {
			arrReviews.remove(r2);
		}
		System.out.println("REMOVED " + count + " INVALID REVIEWS");
		
	}
	
}
