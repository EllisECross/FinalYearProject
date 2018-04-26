import java.io.*;
import java.util.ArrayList;

public class CSVWriter {

	public static void writeToCSV(ArrayList<Review> reviews) {
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("AnalysedReviews.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String rowNames = "ReviewID,ProductID,Rating,Date,Review,ReviewTitle,Reviewer,Decpeitve,Suspicion\n";
		pw.write(rowNames);
		
		for (Review r: reviews) {
			 
		        StringBuilder row = new StringBuilder();
		        row.append(r.getReviewID());
		        row.append(',');
		        row.append(r.getProductID());
		        row.append(',');
		        row.append(r.getRating());
		        row.append(',');
		        row.append(r.getDateAsString());
		        row.append(',');
		        row.append(r.getReview());
		        row.append(',');
		        row.append(r.getReviewTitle());
		        row.append(',');
		        row.append(r.getReviewer());
		        row.append(',');
		        row.append(r.getDeceptive());
		        row.append(',');
		        row.append(r.getSuspicion());
		        row.append('\n');

		        pw.write(row.toString());

		        
		}
		
        pw.close();
		
	}
	
}
