import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class TextAnalysis {

	public static ArrayList<Review> analyse(ArrayList<Review> reviews) {
		
		HashMap<String, double []> posTags = new HashMap<String, double []>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("trainedValues.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] lineArr = line.split(" ");
		    	String tag = lineArr[0];
		    	double [] tagCounts = {0, 0};
		    	tagCounts[0] = Double.parseDouble(lineArr[1]);
		    	tagCounts[1] = Double.parseDouble(lineArr[2]);
		    	posTags.put(tag, tagCounts);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HashMap<String, Integer> posCount = new HashMap<String, Integer>();
		
		int correct = 0;
		int wrong = 0;
		int decepCount = 0;
		double averageTrue = 0;
		double averageFalse = 0;
		
		for (Review r : reviews) {
			
			posCount.clear();
			int tokenCount = 0;
			
			Properties props=new Properties();
		    props.setProperty("annotators","tokenize, ssplit, pos");
		    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		    
		    Annotation annotation = new Annotation(r.getReview());
		    pipeline.annotate(annotation);
		    
		    // Store all the sentences in CoreMap
		    // A CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		    List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		    
		    // Word count HashMap
		    
		    
		    for(CoreMap sentence: sentences) {
		    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {	        
			        // POS tag of the token
		    		tokenCount = tokenCount + 1;
			        String pos = token.get(PartOfSpeechAnnotation.class);
			        
			        if (posCount.containsKey(pos)) {
			        	int prev = posCount.get(pos);
			        	posCount.put(pos, prev + 1);
			        } else {
			        	posCount.put(pos, 1);
			        }
			        
		    	}
		    }
		    
		    //Edit suspicion for token counts
		    double tokPerTrue = posTags.get("TokenCount")[0];
	    	double tokPerDeceptive = posTags.get("TokenCount")[1];
	    	
	    	double absValueTrueTokens = Math.abs(tokPerTrue - tokenCount);
	    	double absValueDeceptiveTokens = Math.abs(tokPerDeceptive - tokenCount);
	    	
	    	if (absValueTrueTokens < absValueDeceptiveTokens) {
	    		r.setSuspicion(r.getSuspicion() - tokPerDeceptive/tokPerTrue);
	    	}
	    	if (absValueDeceptiveTokens < absValueTrueTokens) {
	    		r.setSuspicion(r.getSuspicion() + tokPerTrue/tokPerDeceptive);
	    	}
		    
		    //Edit suspicion for POS counts
		    for (String key : posCount.keySet()) {
		    	try {
			    	int tagCount = posCount.get(key);
			    	double tagPerTrue = posTags.get(key)[0];
			    	double tagPerDeceptive = posTags.get(key)[1];
			    	
			    	double absValueTrue = Math.abs(tagPerTrue - tagCount);
			    	double absValueDeceptive = Math.abs(tagPerDeceptive - tagCount);
			    	
			    	if (absValueTrue < absValueDeceptive) {
			    		r.setSuspicion(r.getSuspicion() - tagPerDeceptive/tagPerTrue);
			    		//System.out.println(tagPerDeceptive/tagPerTrue);
			    	}
			    	if (absValueDeceptive < absValueTrue) {
			    		r.setSuspicion(r.getSuspicion() + tagPerTrue/tagPerDeceptive);
			    		//System.out.println(tagPerTrue/tagPerDeceptive);
			    	}
		    	}catch (Exception e) {
		    		System.out.println(e);
		    		System.out.println(key);
		    	}
		    	
			}
	    	
		}
			
		
		
		System.out.println("MADE it here");
		
		for (Review r: reviews) {
			boolean suspicion = true;
	    	
	    	if (r.getSuspicion() > 2.13028846452) {
	    		suspicion = false;
	    		decepCount = decepCount + 1;
	    	}
	    	
	    	boolean deceptive = true;
	    	if (r.getDeceptive() > 0) {
	    		averageFalse = averageFalse + r.getSuspicion();
	    		deceptive = false;
	    	} else {
	    		averageTrue = averageTrue + r.getSuspicion();
	    	}
	    	
	    	//System.out.println(r.getSuspicion() + " " + suspicion + " " + deceptive);
	    	
	    	if (suspicion == deceptive) {
	    		correct = correct + 1;
	    	} else {
	    		wrong = wrong + 1;
	    	}
		}
		
		System.out.println(correct + " to " + wrong);
		System.out.println("Predicted deceptive: " + decepCount);
		System.out.println("Avg true: " + averageTrue/800);
		System.out.println("Avg false: " + averageFalse/800); 
		
		//Sort reviews based on suspicion
		Collections.sort(reviews, new Comparator<Review>(){
			@Override
			public int compare(Review r1, Review r2) {
				if (r1.getSuspicion() > r2.getSuspicion()) {
					return  -1;
				}
				return 1;
			}
		});
		
		/*
		for (Review r: reviews) {
			System.out.println(r.getSuspicion() + " " + r.getDeceptive());
			System.out.println();
		}
		*/
		
		return reviews;
		
	}
	
}