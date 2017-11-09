public class Review {

	// Unique ID to identify review
	private final int revID;
	// Product ID
	private final String prodID;
	// Rating = 0-5 stars
	private final int rating;
	// Date of review
	private final String Date;
	// Text content of review
	private final String review;
	
	

	public Review(int revID, String prodID, int rating, String Date, String review) {
		this.revID = revID;
		this.prodID = prodID;
		this.rating = rating;
		this.Date = Date;
		this.review = review;
	}
	
	public String getReview() {
		return this.review;
	}
	
}
