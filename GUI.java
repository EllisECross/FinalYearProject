import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import java.awt.Color;

public class GUI {

	private JFrame frmReviewAnalyser;
	JButton btnAnalyse = new JButton("Analyse");
	JLabel lblCurrentStatus = new JLabel("Status:");
	JLabel lblReady = new JLabel("Ready");
	JButton btnTrain = new JButton("Train");
	
	Color myGreen = new Color(0, 153, 51);
	Color myOrange = new Color(255, 153, 51);
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmReviewAnalyser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		
		try { 
		    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		    e.printStackTrace();
		}
		frmReviewAnalyser = new JFrame();
		frmReviewAnalyser.setResizable(false);
		frmReviewAnalyser.setTitle("Review Analyser");
		frmReviewAnalyser.setBounds(100, 100, 450, 300);
		frmReviewAnalyser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmReviewAnalyser.getContentPane().setLayout(null);
		
		
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                lblReady.setText("Training...");
		        lblReady.setForeground(myOrange);
				new TrainReviews().execute();
			}
		});
		btnTrain.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnTrain.setBounds(30, 114, 374, 51);
		frmReviewAnalyser.getContentPane().add(btnTrain);
		
		btnAnalyse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblReady.setText("Analysing...");
		        lblReady.setForeground(myOrange);
				new AnalyseReviews().execute();
			}
		});
		btnAnalyse.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAnalyse.setBounds(30, 192, 374, 51);
		frmReviewAnalyser.getContentPane().add(btnAnalyse);
		
		lblCurrentStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentStatus.setFont(new Font("Calibri", Font.BOLD, 22));
		lblCurrentStatus.setBounds(30, 45, 102, 32);
		frmReviewAnalyser.getContentPane().add(lblCurrentStatus);
		
		lblReady.setForeground(myGreen);
		lblReady.setHorizontalAlignment(SwingConstants.LEFT);
		lblReady.setFont(new Font("Calibri", Font.BOLD, 22));
		lblReady.setBounds(121, 45, 283, 32);
		frmReviewAnalyser.getContentPane().add(lblReady);
		
	}
	
	class TrainReviews extends SwingWorker<String, Object>
	{

		protected String doInBackground() throws Exception
	    { 
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int status = fileChooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                	String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                	try {
        	    		ArrayList<Review> arrReviewTrain = CSVReader.readCSV(fileName);
        	    		TextTrainer.train(arrReviewTrain);
        	    		lblReady.setText("Training Complete");
        		        lblReady.setForeground(myGreen);
        	    	} catch (Exception e) {
        	    		lblReady.setText("Error! Training Failed");
        	    		lblReady.setForeground(Color.RED);
        	    	}
                }
        
            } else {
            	lblReady.setText("No file selected");
		        lblReady.setForeground(Color.RED);
            }
			
	    	
			return "Done";
	    }

	}
	
	class AnalyseReviews extends SwingWorker<String, Object>
	{
	    protected String doInBackground() throws Exception
	    {
	    				
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int status = fileChooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                	String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                	try {
            	    	ArrayList<Review> arrReviewTest = CSVReader.readCSV(fileName);	
            	    	ArrayList<Review> analysedReviews = TextAnalysis.analyse(arrReviewTest);
            			CSVWriter.writeToCSV(analysedReviews);
            			lblReady.setText("Analysis Complete");
            		    lblReady.setForeground(myGreen);
            	    } catch (Exception e) {
            	    		lblReady.setText("Error! Analysis Incomplete");
            		        lblReady.setForeground(Color.RED);
            	    }
                }
        
            } else {
            	lblReady.setText("No file selected");
		        lblReady.setForeground(Color.RED);
            }
			
	    	
			return "Done";
		    		
	    }
	}
	
}
