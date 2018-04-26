import java.text.SimpleDateFormat;
import java.util.Date;

public class Review implements Comparable<Review>{

	// Unique ID to identify review
	private final int revID;
	// Product ID
	private final String prodID;
	// Rating = 0-5 stars
	private final double rating;
	// Date of review
	private final Date Date;
	// Text content of review
	private final String review;
	// Title of review
	private final String reviewTitle;
	// Reviewer username
	private final String reviewer;
	// Level of suspicion of review (initially set to 0 for all reviews)
	private double suspicion;
	// Used for training
	private final int deceptive;

	public Review(int revID, String prodID, double rating, Date Date, String review, String reviewTitle, String reviewer, int deceptive) {
		this.revID = revID;
		this.prodID = prodID;
		this.rating = rating;
		this.Date = Date;
		this.review = review;
		this.reviewTitle = reviewTitle;
		this.reviewer = reviewer;
		this.deceptive = deceptive;
		this.suspicion = 0;
		
	}
	
	public int getReviewID() {
		return this.revID;
	}
	
	public String getProductID() {
		return this.prodID;
	}
	
	public double getRating() {
		return this.rating;
	}
	
	public Date getDate() {
		return this.Date;
	}
	
	public String getReview() {
		return this.review;
	}
	
	public String getDateAsString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(this.Date);
	}
	
	public String getInfo() {
		String s;
		
		s = "ReviewID: " + this.getReviewID();
		s = s + "\nProductID: " + this.getProductID();
		s = s + "\nRating: " + this.getRating();
		s = s + "\nDate: " + this.getDateAsString();
		s = s + "\nReview: " + this.getReview();
		
		return s;
	}

	public double getSuspicion() {
		return suspicion;
	}

	public void setSuspicion(double suspicion) {
		this.suspicion = suspicion;
	}

	public String getReviewer() {
		return reviewer;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}
	
	public int getDeceptive() {
		return this.deceptive;
	}

	@Override
	public int compareTo(Review r) {
		return getDate().compareTo(r.getDate());
	}
	
}
