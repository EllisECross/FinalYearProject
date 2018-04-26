import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class TextTrainer {
	
	public static void train(ArrayList<Review> reviews) {
		
		int revCountTrue = 0;
		int revCountDeceptive = 0;
		int tokensTrue = 0;
		int tokensDeceptive = 0;
		HashMap<String, Integer> posTagsTrue = new HashMap<String, Integer>();
		HashMap<String, Integer> posTagsDeceptive = new HashMap<String, Integer>();
		
		for (Review r : reviews) {
			Properties props=new Properties();
		    props.setProperty("annotators","tokenize, ssplit, pos");
		    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		    
		    Annotation annotation = new Annotation(r.getReview());
		    pipeline.annotate(annotation);
		    
		    // Store all the sentences
		    // A CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		    List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);

		    int tokenCountTrue = 0;
		    HashMap<String, Integer> posCountTrue = new HashMap<String, Integer>();
		    
		    int tokenCountDeceptive = 0;
		    HashMap<String, Integer> posCountDeceptive = new HashMap<String, Integer>();
		    
		    for(CoreMap sentence: sentences) {
		    	
		    	// Traverse words in current sentence
		    	// A CoreLabel is a CoreMap with additional token-specific methods
		    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		    		
		    		// this is the text of the token
			        String word = token.get(TextAnnotation.class);
			        
			        // this is the POS tag of the token
			        String pos = token.get(PartOfSpeechAnnotation.class);
			        
			        if (r.getDeceptive() == 0) {     
				        
			        	tokenCountDeceptive = tokenCountDeceptive + 1;
				        if (posCountDeceptive.containsKey(pos)) {
				        	int prev = posCountDeceptive.get(pos);
				        	posCountDeceptive.put(pos, prev + 1);
				        } else {
				        	posCountDeceptive.put(pos, 1);
				        }
				        
			        } else {
			        	
			        	tokenCountTrue = tokenCountTrue + 1;
				        if (posCountTrue.containsKey(pos)) {
				        	int prev = posCountTrue.get(pos);
				        	posCountTrue.put(pos, prev + 1);
				        } else {
				        	posCountTrue.put(pos, 1);
				        }
			        	
			        }
		    	}	
		    }
		    
		    for (String tag: posCountTrue.keySet()){
	            String key = tag;
	            int value = posCountTrue.get(tag);  
	            
	            if (posTagsTrue.containsKey(tag)) {
		        	int prev = posTagsTrue.get(tag);
		        	posTagsTrue.put(tag, prev + value);
		        } else {
		        	posTagsTrue.put(tag, 1);
		        }
	            
		    }
		    
		    for (String tag: posCountDeceptive.keySet()){
	            String key = tag;
	            int value = posCountDeceptive.get(tag);  
	            
	            if (posTagsDeceptive.containsKey(tag)) {
		        	int prev = posTagsDeceptive.get(tag);
		        	posTagsDeceptive.put(tag, prev + value);
		        } else {
		        	posTagsDeceptive.put(tag, 1);
		        }
	            
		    }
		
		    if (r.getDeceptive() == 0) {
		    	revCountDeceptive = revCountDeceptive + 1;
		    	tokensDeceptive = tokensDeceptive + tokenCountDeceptive;
		    } else {
		    	revCountTrue = revCountTrue + 1;
		    	tokensTrue = tokensTrue + tokenCountTrue;
		    }
		    
		}
		
		double tokensPerTrue = tokensTrue/revCountTrue;
		double tokensPerDeceptive = tokensDeceptive/revCountDeceptive;

		Set<String> allKeys = new HashSet(posTagsTrue.keySet());
		allKeys.addAll(posTagsDeceptive.keySet());
		
		System.out.println("Number of true reviews: " + revCountTrue);
		System.out.println("Number of deceptive reviews: " + revCountDeceptive);
		System.out.println("Number of tokens in true reviews: " + tokensTrue);
		System.out.println("Number of tokens in deceptive reviews: " + tokensDeceptive);
		System.out.println("Average tokens per true review: " + tokensPerTrue);
		System.out.println("Average tokens per deceptive review: " + tokensPerDeceptive);
		System.out.println("");
		
		System.out.println("True Reviews: ");
		
		try {
			File fout = new File("trainedValues.txt");
	    	FileOutputStream fos = new FileOutputStream(fout);
	    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	    	
	    	bw.write("TokenCount" + " " + tokensPerTrue + " " + tokensPerDeceptive);
			bw.newLine();
	    	
			for (String posTag: allKeys){
	
	            String key =posTag;
	            
	            int valueTrue = 0;
	            try {
	            	valueTrue = posTagsTrue.get(posTag); 
	            } catch (Exception e) {
	            	valueTrue = 0;
	            }
	            
	            int valueDeceptive = 0;
	            try {
	            	valueDeceptive = posTagsDeceptive.get(posTag); 
	            } catch (Exception e) {
	            	valueDeceptive = 0;
	            }
	            
	            double tagPerTrueReview = valueTrue/tokensPerTrue;
	            double tagPerDecpetiveReview = valueDeceptive/tokensPerDeceptive;
	            
	            
	            System.out.println(key + ": " + (tagPerTrueReview) + " to " + (tagPerDecpetiveReview));  
	            System.out.println(tagPerTrueReview + "/" + tagPerDecpetiveReview + " = " + (tagPerTrueReview/tagPerDecpetiveReview));
	            System.out.println("");
	            
	            bw.write(key + " " + tagPerTrueReview + " " + tagPerDecpetiveReview);
				bw.newLine();
		             
		    }
			
			bw.flush();
	        bw.close();
        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		/*
		System.out.println("Number of deceptive reviews: " + revCountDeceptive);
		System.out.println("Number of tokens in deceptive reviews: " + tokensDeceptive);
		System.out.println("Average tokens per deceptive review: " + tokensDeceptive/revCountDeceptive);
		
		System.out.println("");
		System.out.println("Deceptive Reviews: ");
		for (String posTag: posTagsDeceptive.keySet()){

            String key =posTag;
            String value = posTagsDeceptive.get(posTag).toString();  
            System.out.println(key + ": " + value);  
            
	    }
	    */
		
	}

}
