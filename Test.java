import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Test {
	
	public static void main(String[] args) {


		ArrayList<Review> arrReviewTest = CSVReader.readCSV("D:/University/Year 3/Project/Datasets/Set4/Set4Train.csv");
		ArrayList<Review> analysedReviews = TextAnalysis.analyse(arrReviewTest);
		CSVWriter.writeToCSV(analysedReviews);
		
		/*
		System.out.println("Test CSV Reader:");
		testReader();
		System.out.println();
		
		System.out.println("Test Non Text Analysis:");
		testNonTextAnalysis();
		System.out.println();
		
		System.out.println("Test Text Train:");
		testTextTrain();
		System.out.println();
		
		System.out.println("Test Text Analysis:");
		testTextAnalysis();
		System.out.println();
		
		System.out.println("Test Full System:");
		testFullSystem();
		System.out.println();
		*/
	}
	
	

	public static void testReader() {
		
		long startTime = System.nanoTime();
		
		ArrayList<Review> arrReviews = CSVReader.readCSV("D:/University/Year 3/Project/Datasets/Set3/HotelDS.csv");
		
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		double seconds = (double)totalTime / 1000000000.0;
		System.out.println(seconds);
		
	}

	private static void testNonTextAnalysis() {
		
		ArrayList<Review> arrReviews = CSVReader.readCSV("D:/University/Year 3/Project/Datasets/Set3/HotelDS.csv");
		
		long startTime = System.nanoTime();
		arrReviews = NonTextAnlysis.removeMiddleReviews(arrReviews);
		arrReviews = NonTextAnlysis.checkReviewer(arrReviews);
		Collections.sort(arrReviews);
		
		Map<String,ArrayList<Review>> productReviews = NonTextAnlysis.seperateProducts(arrReviews);
		
		arrReviews = NonTextAnlysis.checkDates(productReviews, arrReviews);
	
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		double seconds = (double)totalTime / 1000000000.0;
		System.out.println(seconds);
		
	}
	
	private static void testTextTrain() {
		
		ArrayList<Review> arrReviewTrain = CSVReader.readCSV("D:/University/Year 3/Project/Datasets/Set4/Set4Train.csv");
		
		long startTime = System.nanoTime();
		TextTrainer.train(arrReviewTrain);
		
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		double seconds = (double)totalTime / 1000000000.0;
		System.out.println(seconds);

	}

	private static void testTextAnalysis() {
		
		ArrayList<Review> arrReviewTest = CSVReader.readCSV("D:/University/Year 3/Project/Datasets/Set4/Set4Train.csv");
		
		long startTime = System.nanoTime();
		ArrayList<Review> analysedReviews = TextAnalysis.analyse(arrReviewTest);
		
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		double seconds = (double)totalTime / 1000000000.0;
		System.out.println(seconds);
	}
	
	private static void testFullSystem() {
		
		long startTime = System.nanoTime();
		
		ArrayList<Review> arrReviewTrain = CSVReader.readCSV("D:/University/Year 3/Project/Datasets/Set4/Set4Train.csv");
		TextTrainer.train(arrReviewTrain);
		
		ArrayList<Review> arrReviewTest = CSVReader.readCSV("D:/University/Year 3/Project/Datasets/Set4/Set4Train.csv");
		arrReviewTest = NonTextAnlysis.removeMiddleReviews(arrReviewTest);
		arrReviewTest = NonTextAnlysis.checkReviewer(arrReviewTest);
		Collections.sort(arrReviewTest);
		
		Map<String,ArrayList<Review>> productReviews = NonTextAnlysis.seperateProducts(arrReviewTest);
		
		arrReviewTest = NonTextAnlysis.checkDates(productReviews, arrReviewTest);
		
		ArrayList<Review> analysedReviews = TextAnalysis.analyse(arrReviewTest);
		CSVWriter.writeToCSV(analysedReviews);
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		double seconds = (double)totalTime / 1000000000.0;
		System.out.println(seconds);
		
		
	}
	
	
	
}