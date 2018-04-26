import java.util.Date;
import java.io.*;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;


public class TestNLP {

	public static void main(String[] args){
	
		String revText = "My husband and I satayed for two nights at the Hilton Chicago,and enjoyed every minute of it! The bedrooms are immaculate,and the linnens "
				+ "are very soft. We also appreciated the free wifi,as we could stay in touch with friends while staying in Chicago. The bathroom was quite spacious,"
				+ "and I loved the smell of the shampoo they provided-not like most hotel shampoos. Their service was amazing,and we absolutely loved the beautiful indoor pool. "
				+ "I would recommend staying here to anyone.";
		Date revDate = new Date(01-01-2012);
		
		Review testRev = new Review(0, "d_hilton", 5.0, revDate, revText, "TestTitle", "TestReviewer", 0);
		
		Properties props=new Properties();
	    props.setProperty("annotators","tokenize, ssplit, pos");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    Annotation annotation;  
	     
    	annotation = new Annotation(testRev.getReview());
    	
    	pipeline.annotate(annotation);    //System.out.println("LamoohAKA");
    	
    	PrintWriter pw;
    	
    	try {
			pw = new PrintWriter ( new BufferedWriter ( new FileWriter ( "outputme.txt", false )  )  ) ;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	
    	PrintWriter out;
		try {
			out = new PrintWriter("outputme.txt");
			pipeline.prettyPrint(annotation, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	
		// Store all the sentences
	    // A CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);

	    int tokenCount = 0;
	    HashMap<String, Integer> posCount = new HashMap<String, Integer>();
	    
	    for(CoreMap sentence: sentences) {
	    	
	    	// Traverse words in current sentence
	    	// A CoreLabel is a CoreMap with additional token-specific methods
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    		
	    		// this is the text of the token
		        String word = token.get(TextAnnotation.class);
		        
		        // this is the POS tag of the token
		        String pos = token.get(PartOfSpeechAnnotation.class);     
		        tokenCount++;
		        if (posCount.containsKey(pos)) {
		        	int prev = posCount.get(pos);
		        	posCount.put(pos, prev + 1);
		        } else {
		        	posCount.put(pos, 1);
		        }
		       
		        //System.out.println(word);
		        //System.out.println(pos);
	      	}
	    }
	    
	    for (String posTag: posCount.keySet()){

            String key =posTag;
            String value = posCount.get(posTag).toString();  
            System.out.println(key + ": " + value);  
            
	    } 
	
	}
}
