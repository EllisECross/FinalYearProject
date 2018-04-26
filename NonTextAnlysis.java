import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NonTextAnlysis {

	//Removes all reviews that are not either 1 or 5 star
	public static ArrayList<Review> removeMiddleReviews(ArrayList<Review> reviews) {
		
		//Creates an arrayList of invalid reviews and removes them from the entire array of reviews
		
		ArrayList<Review> invalidReviews = new ArrayList<Review>();
		int count = 0;
		int count2 = 0;
		
		for (Review r : reviews) {
			if (r.getRating() != 1.0 && r.getRating() != 5.0) {
				count++;
				invalidReviews.add(r);
			} else {
				count2++;
			} 
		}
		
		
		for (Review r2 : invalidReviews) {
			reviews.remove(r2);
		}
		
		System.out.println("REMOVED " + count + " NON 1 OR 5 REVIEWS");
		System.out.println("Reviews remaining: " + count2);
		
		return reviews;
	}
	
	//Adds one to reviewer suspicion if the reviewer is anonymous or a one letter username
	public static ArrayList<Review> checkReviewer(ArrayList<Review> reviews) {
		
		for (Review r : reviews) {
			if (r.getReviewer().toLowerCase().equals("anonymous")) {
				r.setSuspicion(r.getSuspicion() + 1);
			}
			if (r.getReviewer().toLowerCase().equals("a traveler")) {
				r.setSuspicion(r.getSuspicion() + 1);
			}
			if (r.getReviewer().length() < 2) {
				r.setSuspicion(r.getSuspicion() + 1);
			}
		}
		
		return reviews;
	}
	
	// Creates a Map, the key being all the individual products and value being an arrayList of all reviews for that product
	public static Map<String,ArrayList<Review>> seperateProducts(ArrayList<Review> reviews) {
		
		List<String> products = new ArrayList<String>();
		
		for (Review r : reviews) {
			if (!products.contains(r.getProductID())) {
				products.add(r.getProductID());
			}
		}
		
		Map<String,ArrayList<Review>> productReviews = new HashMap<String,ArrayList<Review>>();
		
		for (String p: products) {
			
			ArrayList<Review> productReviewArr = new ArrayList<Review>();
			
			for (Review r : reviews) {
				if (p.equals(r.getProductID())) {
					productReviewArr.add(r);
				}
			}
			
			productReviews.put(p, productReviewArr);
			
		}
	
		return productReviews;
		
	}

	//Return an arrayList of all reviews which were done on the same product within 3 days of each other
	public static ArrayList<Review> checkDates(Map<String, ArrayList<Review>> productReviews, ArrayList<Review> arrReviews) {
		
		ArrayList<Review> reviews = new ArrayList<Review>();
		
		Date date = new Date();
		for (Map.Entry<String, ArrayList<Review>> entry : productReviews.entrySet()) {
		    ArrayList<Review> reviewArr = entry.getValue();
		    date = new Date();
		    
		    Review prevR;
		    for (Review r : reviewArr) {
				//Convert difference between dates into days
				int days = (int) TimeUnit.DAYS.convert((r.getDate().getTime() - date.getTime()), TimeUnit.MILLISECONDS);
				//Store previous review and date
				date = r.getDate();
				prevR = r;
				if (days < 3) {
					// if reviews are within 3 days of each other
					reviews.add(r);
					reviews.add(prevR);
				}
			} 
		}
		
		 Set<Review> reviewsSet = new LinkedHashSet<Review>(reviews);  
	     //clear the ArrayList so that we can copy all elements from LinkedHashSet
		 reviews.clear();  
	     //copying elements but without any duplicates
		 reviews.addAll(reviewsSet);
		 
		 for (Review r: reviews) {
				for (Review r2: arrReviews) {
					if (r == r2) {
						r2.setSuspicion(r2.getSuspicion() + 1);
					}	
				}	
			}
		
		return arrReviews;
	}
	
}