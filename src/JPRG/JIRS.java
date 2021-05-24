package JPRG;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.FontUIResource;

import JPRG.PlacesOfInterest;

import java.awt.Color;
import javax.swing.border.BevelBorder;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Desktop;

public class JIRS {
	
	private String fs2;
	private static int OPTION_MAINMENU = 0;
	private static int OPTION_VIEWALLATTRACTIONS = 1;
	private static int OPTION_DISPLAYATTRACTIONSFORSELECTION = 2;
	private static int OPTION_SEARCHATTRACTIONBYTYPEORACTIVITIES = 3;
	
	private JFrame frmItineraryRecommendationSystem;
	private JTextField typeTextField;
	private JTextField attractionTextField;
	private JTextField ratingTextField;
	private JTextField activitiesTextField;
	private JButton moreInfoButton;
	
	private JPanel mainPanel;
	private JPanel subPanel; 
	private JLabel recordXOfNLabel;
	private JLabel imageDisplayLabel;
	private static JLabel messageLabel;
	
	private int placePointer;
	private int placePointerFiltered;
	private int imagePointer;
	private int optionPointer;
	private static ArrayList<PlacesOfInterest> places = new ArrayList<PlacesOfInterest>();
	private static ArrayList<PlacesOfInterest> placesFiltered = new ArrayList<PlacesOfInterest>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JIRS window = new JIRS();
					window.frmItineraryRecommendationSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JIRS() {
		// change the message font size of a JOptionPane
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(  
		          "Arial", Font.BOLD, 24)));
		// Change the button font size of a JOptionPane
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(  
		          "Arial", Font.BOLD, 24)));  
		
		initialize();
		optionPointer = OPTION_MAINMENU;
		loadPlaceOfInterest();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		String fs = System.getProperty("file.separator");
		fs2 = fs+fs;
		frmItineraryRecommendationSystem = new JFrame();
		frmItineraryRecommendationSystem.setTitle("Itinerary Recommendation System v1.0");
		ImageIcon jirsIcon = new ImageIcon("img"+fs+fs+"JIRS.png");
		frmItineraryRecommendationSystem.setIconImage(jirsIcon.getImage());
		frmItineraryRecommendationSystem.setBounds(new Rectangle(0, 0, 720, 960));
		frmItineraryRecommendationSystem.setBounds(100, 100, 720, 960);
		frmItineraryRecommendationSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set to absolute layout so that easier to position the components
		frmItineraryRecommendationSystem.getContentPane().setLayout(null);
		// disallow resizing of the frame
		frmItineraryRecommendationSystem.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// center the frame in the screen
		frmItineraryRecommendationSystem.setLocation(dim.width/2-frmItineraryRecommendationSystem.getSize().width/2, dim.height/2-frmItineraryRecommendationSystem.getSize().height/2);
		
		
		/** Sub panel section
		 *  visual display and control for second screen (view all attractions, filtered attraction
		 */
		
		
		/* Main panel section
		 * this panel hold the button control for the main menu
		 */
		subPanel = new JPanel();
		subPanel.setVisible(false);
		subPanel.setBounds(17, 542, 696, 440);
		frmItineraryRecommendationSystem.getContentPane().add(subPanel);
		subPanel.setLayout(null);
		
		JButton prevImageButton = new JButton("");
		prevImageButton.setIcon(new ImageIcon("img"+fs2+"left.png"));
		prevImageButton.setFont(new Font("Tahoma", Font.BOLD, 22));
		prevImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check if current action is on filtered list (search by attraction type & activities)? if yes use placesFiltered ArrayList, else places ArrayList
				if(optionPointer==OPTION_SEARCHATTRACTIONBYTYPEORACTIVITIES)	{
					// process only if the pointer is greater than 1
					if (imagePointer > 1) {
						// reduce pointer by 1 and check if an image file that correspond to the pointer exist
		        		// if exist display the image, otherwise add 1 back to the pointer
		        		imagePointer--;
		        		PlacesOfInterest p1 = (PlacesOfInterest) placesFiltered.get(placePointerFiltered);
		        		String imageUrl = "img"+fs2+p1.getPlaceID()+"_"+String.valueOf(imagePointer)+".jpg";
		        		
		        	   	BufferedImage img = null;
		        	   	try {
		        	   		img = ImageIO.read(new File(imageUrl));
		        	   		imageDisplayLabel.setIcon(new ImageIcon(img));
		        	   		
		        	   	} catch (Exception ex) 		{
			    			//ex.printStackTrace();
			    			imagePointer++;
			    			// image no change
			    		}
		        	}
				} else {
					// process only if the pointer is greater than 1
					if (imagePointer > 1) {
						// reduce pointer by 1 and check if an image file that correspond to the pointer exist
		        		// if exist display the image, otherwise add 1 back to the pointer
						imagePointer--;
		        		PlacesOfInterest p1 = (PlacesOfInterest) places.get(placePointer);
		        		String imageUrl = "img"+fs2+p1.getPlaceID()+"_"+String.valueOf(imagePointer)+".jpg";
		        		
		        	   	BufferedImage img = null;
		        	   	try {
		        	   		img = ImageIO.read(new File(imageUrl));
		        	   		imageDisplayLabel.setIcon(new ImageIcon(img));
		        	   		
		        	   	} catch (Exception ex) 		{
			    			//ex.printStackTrace();
		        	   		// add 1 back to the pointer
			    			imagePointer++;
			    			// image no change
			    		}
		        	}
				}
			}
		});
		prevImageButton.setBounds(290, 0, 72, 31);
		subPanel.add(prevImageButton);
		
		JButton nextImageButton = new JButton("");
		nextImageButton.setIcon(new ImageIcon("img"+fs2+"right.png"));
		nextImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check if current action is on filtered list (search by attraction type & activities)? if yes use placesFiltered ArrayList, else places ArrayList
				if (optionPointer==OPTION_SEARCHATTRACTIONBYTYPEORACTIVITIES)	{
					// increase pointer by 1 and check if an image file that correspond to the pointer exist
	        		// if exist display the image, otherwise reduce 1 from the pointer
					imagePointer++;
		        	PlacesOfInterest p1 = (PlacesOfInterest) placesFiltered.get(placePointerFiltered);
		        	String imageUrl = "img"+fs2+p1.getPlaceID()+"_"+String.valueOf(imagePointer)+".jpg";
		        		
		        	BufferedImage img = null;
		        	try {
		        		img = ImageIO.read(new File(imageUrl));
		        		imageDisplayLabel.setIcon(new ImageIcon(img));
		        	   		
		        	} catch (Exception ex) 		{
		        		//JOptionPane.showMessageDialog(null, imageUrl);
		        		//ex.printStackTrace();
		        		imagePointer--;
			    			// image no change
			    	}
				} else {
					// increase pointer by 1 and check if an image file that correspond to the pointer exist
	        		// if exist display the image, otherwise reduce 1 from the pointer
					imagePointer++;
		        	PlacesOfInterest p1 = (PlacesOfInterest) places.get(placePointer);
		        	String imageUrl = "img"+fs2+p1.getPlaceID()+"_"+String.valueOf(imagePointer)+".jpg";
		        		
		        	BufferedImage img = null;
		        	try {
		        		img = ImageIO.read(new File(imageUrl));
		        		imageDisplayLabel.setIcon(new ImageIcon(img));
		        	   		
		        	} catch (Exception ex) 		{
		        		//JOptionPane.showMessageDialog(null, imageUrl);
		        		//ex.printStackTrace();
		        		imagePointer--;
			    			// image no change
			    	}
				}
			}
		});
		nextImageButton.setFont(new Font("Tahoma", Font.BOLD, 22));
		nextImageButton.setBounds(365, 0, 72, 31);
		subPanel.add(nextImageButton);
		
		JButton mainMenuButton = new JButton("BACK");
		mainMenuButton.setIconTextGap(10);
		mainMenuButton.setIcon(new ImageIcon("img"+fs2+"home.png"));
		mainMenuButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		mainMenuButton.setBounds(34, 345, 190, 60);
		mainMenuButton.setHorizontalAlignment(SwingConstants.LEFT);
		mainMenuButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		mainMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionPointer=OPTION_MAINMENU;
				restoreMainImageDisplay();
				mainPanel.setVisible(true);
				subPanel.setVisible(false);
			}
		});
		subPanel.add(mainMenuButton);
		
		JButton prevAttractionButton = new JButton("PREV");
		prevAttractionButton.setIcon(new ImageIcon("img"+fs2+"arrow_left.png"));
		prevAttractionButton.setIconTextGap(10);
		prevAttractionButton.setHorizontalAlignment(SwingConstants.LEFT);
		prevAttractionButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		prevAttractionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (optionPointer==OPTION_SEARCHATTRACTIONBYTYPEORACTIVITIES)	{
					if (placePointerFiltered>0) {
						// load the previous places of interest in the filtered list
		        		loadPlaceInfoFiltered(--placePointerFiltered);
		        	} else {
		        		startOfCollectionException();
		        	}
				} else {
					if (placePointer>0) {
						// load the previous places of interest in the main list
		        		loadPlaceInfo(--placePointer);
		        	} else {
		        		startOfCollectionException();
		        	}
					
				}
			}
		});
		prevAttractionButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		prevAttractionButton.setBounds(313, 345, 180, 60);
		subPanel.add(prevAttractionButton);
		
		JButton nextAttractionButton = new JButton("NEXT");
		nextAttractionButton.setIcon(new ImageIcon("img"+fs2+"arrow_right.png"));
		nextAttractionButton.setIconTextGap(10);
		nextAttractionButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		nextAttractionButton.setHorizontalAlignment(SwingConstants.RIGHT);
		nextAttractionButton.setHorizontalTextPosition(SwingConstants.LEFT);
		nextAttractionButton.setText("<html><div align=left width=55px>NEXT</div></html>");
		nextAttractionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (optionPointer==OPTION_SEARCHATTRACTIONBYTYPEORACTIVITIES)	{
					if (placePointerFiltered<placesFiltered.size()-1) {
						// load the next places of interest in the filtered list
						loadPlaceInfoFiltered(++placePointerFiltered);
					} else {
						endOfCollectionException();
					}
				} else {
					if (placePointer<places.size()-1) {
						// load the next places of interest in the main list
						loadPlaceInfo(++placePointer);
					} else {
						endOfCollectionException();
					}
				}
				
			}
		});
		nextAttractionButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		nextAttractionButton.setActionCommand("");
		nextAttractionButton.setBounds(499, 345, 180, 60);
		subPanel.add(nextAttractionButton);
		
		JLabel lblType = new JLabel("TYPE :");
		lblType.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType.setBounds(17, 77, 177, 23);
		subPanel.add(lblType);
		
		JLabel lblAttraction = new JLabel("ATTRACTION :");
		lblAttraction.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblAttraction.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAttraction.setBounds(17, 119, 177, 23);
		subPanel.add(lblAttraction);
		
		JLabel lblActivities = new JLabel("ACTIVITIES :");
		lblActivities.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActivities.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblActivities.setBounds(17, 162, 177, 23);
		subPanel.add(lblActivities);
		
		JLabel lblRating = new JLabel("RATING :\r\n");
		lblRating.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRating.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblRating.setBounds(17, 204, 177, 23);
		subPanel.add(lblRating);
		
		typeTextField = new JTextField();
		typeTextField.setDisabledTextColor(Color.BLUE);
		typeTextField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		typeTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		typeTextField.setEditable(false);
		typeTextField.setEnabled(false);
		typeTextField.setBounds(205, 71, 474, 36);
		subPanel.add(typeTextField);
		typeTextField.setColumns(60);
		
		attractionTextField = new JTextField();
		attractionTextField.setDisabledTextColor(Color.BLUE);
		attractionTextField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		attractionTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		attractionTextField.setEditable(false);
		attractionTextField.setEnabled(false);
		attractionTextField.setColumns(60);
		attractionTextField.setBounds(205, 113, 427, 36);
		subPanel.add(attractionTextField);
		
		ratingTextField = new JTextField();
		ratingTextField.setDisabledTextColor(Color.BLUE);
		ratingTextField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		ratingTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		ratingTextField.setEditable(false);
		ratingTextField.setEnabled(false);
		ratingTextField.setColumns(60);
		ratingTextField.setBounds(205, 198, 474, 36);
		subPanel.add(ratingTextField);
		
		activitiesTextField = new JTextField();
		activitiesTextField.setDisabledTextColor(Color.BLUE);
		activitiesTextField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		activitiesTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		activitiesTextField.setEditable(false);
		activitiesTextField.setEnabled(false);
		activitiesTextField.setColumns(60);
		activitiesTextField.setBounds(205, 156, 474, 36);
		subPanel.add(activitiesTextField);
		
		recordXOfNLabel = new JLabel("1 of 10");
		recordXOfNLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		recordXOfNLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		recordXOfNLabel.setBounds(205, 29, 474, 23);
		subPanel.add(recordXOfNLabel);
		
		moreInfoButton = new JButton("New button");
		moreInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moreInfoOnInternet();
			}
		});
		moreInfoButton.setForeground(Color.MAGENTA);
		moreInfoButton.setFont(new Font("Tahoma", Font.ITALIC, 20));
		moreInfoButton.setBounds(205, 239, 474, 31);
		subPanel.add(moreInfoButton);
		
		messageLabel = new JLabel("XXX");
		messageLabel.setForeground(Color.RED);
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setFont(new Font("Tahoma", Font.ITALIC, 22));
		messageLabel.setVisible(false);
		messageLabel.setBounds(34, 292, 645, 23);
		subPanel.add(messageLabel);
		
		JButton mapsLocationButton = new JButton("");
		mapsLocationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mapsTheLocation();
			}
		});
		mapsLocationButton.setIcon(new ImageIcon("img"+fs2+"gmaps.jpg"));
		mapsLocationButton.setBounds(634, 111, 45, 38);
		subPanel.add(mapsLocationButton);
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 560, 714, 420);
		frmItineraryRecommendationSystem.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		JButton viewAllAttractionsButton = new JButton("VIEW ALL ATTRACTIONS");
		viewAllAttractionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionPointer = OPTION_VIEWALLATTRACTIONS;
				placePointer = 0;
				imagePointer = 0;
				subPanel.setVisible(true);
				mainPanel.setVisible(false);
				loadPlaceInfo(placePointer);
				
			}
		});
		viewAllAttractionsButton.setBounds(26, 76, 660, 60);
		mainPanel.add(viewAllAttractionsButton);
		viewAllAttractionsButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		
		JButton displayAttractionsButton = new JButton("DISPLAY ATTRACTIONS FOR SELECTION");
		displayAttractionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAttractionListSelection();
			}
		});
		displayAttractionsButton.setBounds(26, 140, 660, 60);
		mainPanel.add(displayAttractionsButton);
		displayAttractionsButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		
		JButton searchAttractionButton = new JButton("SEARCH ATTRACTION");
		searchAttractionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAttractionTypeSelection();
			}
		});
		searchAttractionButton.setBounds(26, 204, 660, 60);
		mainPanel.add(searchAttractionButton);
		searchAttractionButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		
		JButton quitButton = new JButton("QUIT");
		quitButton.setBounds(26, 342, 660, 60);
		mainPanel.add(quitButton);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		quitButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		
		JButton searchActivitiesButton = new JButton("SEARCH ACTIVITIES");
		searchActivitiesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showActivitiesSelection();
			}
		});
		
		searchActivitiesButton.setActionCommand("SEARCH ACTIVITIES");
		searchActivitiesButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		searchActivitiesButton.setBounds(26, 268, 660, 60);
		mainPanel.add(searchActivitiesButton);
		
		JButton developedByButton = new JButton();
		developedByButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop desktop;
					if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
						URI mailto = new URI("mailto:neo_yew_chuan_liang01.pt@ichat.sp.edu.sg?subject=[FEEDBACK]%20Itinerary%20Recommendation%20System");
						desktop.mail(mailto);			
						
			        } else {
			        	throw new RuntimeException("Desktop doesn't support mailto!");
			        }
				} catch (Exception ex)	{
					ex.printStackTrace();
				}
		        	
			}
		});
		developedByButton.setIcon(new ImageIcon("img"+fs2+"Email.png"));
		developedByButton.setIconTextGap(10);
		developedByButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		developedByButton.setForeground(Color.GRAY);
		developedByButton.setOpaque(false);
		developedByButton.setBorderPainted(false);
		developedByButton.setContentAreaFilled(false);
		developedByButton.setMargin(new Insets(0, 0, 0, 0));
		developedByButton.setFocusPainted(false);
		developedByButton.setFont(new Font("Tahoma", Font.ITALIC, 18));
		developedByButton.setBounds(193, 19, 325, 31);
		developedByButton.setText("<html><dev align=center><u>Developed by Neo Yew Chuan</u></div></html>");
		developedByButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mainPanel.add(developedByButton);
		
		
		/* Common section for main screen and second screen.
		 * 
		 */
		JLabel lblWelcomeToItinerary = new JLabel("Welcome to Itinerary Recommendation System");
		lblWelcomeToItinerary.setBounds(17, 5, 680, 55);
		lblWelcomeToItinerary.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblWelcomeToItinerary.setHorizontalAlignment(SwingConstants.CENTER);
		frmItineraryRecommendationSystem.getContentPane().add(lblWelcomeToItinerary);
		
		imageDisplayLabel = new JLabel("");
		imageDisplayLabel.setBackground(Color.WHITE);
		imageDisplayLabel.setBounds(27, 61, 662, 480);
		imageDisplayLabel.setRequestFocusEnabled(false);
		imageDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageDisplayLabel.setMaximumSize(new Dimension(480, 320));
		imageDisplayLabel.setMinimumSize(new Dimension(480, 320));
		imageDisplayLabel.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
		imageDisplayLabel.setIcon(new ImageIcon("img"+fs2+"main.png"));
		frmItineraryRecommendationSystem.getContentPane().add(imageDisplayLabel);
		
		
	}
	
	/* To display 'Start of collection!' message when user trying to click Prev button when it is already the first collection in the list
	 * make use of Timer class to only briefly show (setVisible=true) the message on a JLabel, after 1 sec set the JLabel visibility to false 
	 */
	private void startOfCollectionException()	{
		messageLabel.setText("Start of Collection!");
		messageLabel.setVisible(true);
		int delay = 1000; //milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				messageLabel.setVisible(false);
			}};
			new javax.swing.Timer(delay, taskPerformer).start();
	}
	
	/* To display 'End of collection!' message when user trying to click Next button when it is already the last collection in the list
	 * make use of Timer class to only briefly show (setVisible=true) the message on a JLabel, after 1 sec set the JLabel visibility to false 
	 */
	private void endOfCollectionException()	{
		messageLabel.setText("End of Collection!");
		messageLabel.setVisible(true);
		int delay = 1000; //milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				messageLabel.setVisible(false);
			}};
			new javax.swing.Timer(delay, taskPerformer).start();
	}
	
	/* Using FileInputStream & BufferReader to load the pre-defined attraction list into ArrayList places.
	 * Each attraction is created as an object of PlacesOfInterest, hence the ArrayList places is a list of PlacesOfInterest object
	 * Any number of new attractions can be easily added to the places.txt without changing the program code
	 * This only run once at the beginning of the program
	 */
	private void loadPlaceOfInterest()	{
		FileInputStream fis = null;
        BufferedReader reader = null;
        boolean appIsGoodToStart = false;
        try {
            fis = new FileInputStream("txt//places.txt");
            reader = new BufferedReader(new InputStreamReader(fis));
            
            String placeID = "";
        	String placeName = "";
        	String interestType = "";
        	String mustSeeOrDo = "";
        	double placeRating = 0.00;
            String line = reader.readLine();
            while(line != null){
            	String[] splitString = line.split(";");
            	placeID = splitString[0];
            	placeName = splitString[1];
            	interestType = splitString[2];
            	mustSeeOrDo = splitString[3];
            	//JOptionPane.showMessageDialog(null,splitString[4]);
            	placeRating = Double.parseDouble(splitString[4]);
            	places.add(new PlacesOfInterest(placeID, placeName, interestType, mustSeeOrDo, placeRating));
            					
                line = reader.readLine();
            }           
            appIsGoodToStart = true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
        	ex.printStackTrace();
          
        } catch (Exception ex) {
        	ex.printStackTrace();
     	
        } finally {
        
            try {
                reader.close();
                fis.close();
            } catch (IOException ex) {
            	ex.printStackTrace();
            }
        }
       
        if (!appIsGoodToStart) {
        	JOptionPane.showMessageDialog(null,
        			"Program failed to start, program directory may not have setup correctly!",
        			"Program failed", JOptionPane.ERROR_MESSAGE);
        	System.exit(0);
        }

	}
	
	
	/*
     * Load the attraction info of the master ArrayList 'places'
     * when user click View all Activities or Display Attractions for Selection.
     * A pointer to the ArrayList need to be maintained, 
     * the pointer will be 0 when View All Attraction was clicked, or any number between 0 to the size(-1) of ArrayList 'places'
     * images to always load the first images for the attraction, i.e. ending with '_1'
     * Each attraction may not have the equal number of available images, resetting imagePointer to 1 is the easiest 
     */
    
    private void loadPlaceInfo(int pointer) {
		
		try {
					
			PlacesOfInterest poi = (PlacesOfInterest) places.get(pointer);
			recordXOfNLabel.setText("Attraction "+(pointer+1)+" of "+places.size());
			BufferedImage image = null;
			try
			{
				image = ImageIO.read(new File("img"+fs2+poi.getPlaceID()+"_1.jpg"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		
			imageDisplayLabel.setIcon(new ImageIcon(image));  
			typeTextField.setText("  " + poi.getPlaceInterestType());
			attractionTextField.setText("  "+poi.getPlaceName());
			activitiesTextField.setText("  "+poi.getMustSeeOrDo());
			ratingTextField.setText("  "+String.valueOf(poi.getRating()));
			moreInfoButton.setFocusPainted(false);
			moreInfoButton.setMargin(new Insets(0,0,0,0));
			moreInfoButton.setContentAreaFilled(false);
			moreInfoButton.setBorderPainted(false);
			moreInfoButton.setOpaque(false);
			moreInfoButton.setText("<html><div alight=center><u>Learn more about "+poi.getPlaceName()+"</u></div></html>");
			moreInfoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			//moreInfoButton.setText("< Learn more about "+poi.getPlaceName()+" >");
			//moreInfoButton.setText(" <<< CLICK FOR MORE INFO >>> ");
			//moreInfoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			imagePointer=1;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
    /*
     * Load the attraction info of the selected filtered ArrayList 'placesFiltered'
     * when user click Search Attraction By Type or Search Attraction by Activities.
     * A separate pointer to the filtered ArrayList need to be maintained
     * images to always load the first images for the attraction, i.e. ending with '_1'
     * Each attraction may not have the equal number of available images, resetting imagePointer to 1 is the easiest 
     */
	private void loadPlaceInfoFiltered(int pointer) {
		try {
					
			PlacesOfInterest poi = (PlacesOfInterest) placesFiltered.get(pointer);
			recordXOfNLabel.setText("Attraction "+(pointer+1)+" of "+placesFiltered.size());
			BufferedImage image = null;
			try
			{
				image = ImageIO.read(new File("img"+fs2+poi.getPlaceID()+"_1.jpg"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		
			imageDisplayLabel.setIcon(new ImageIcon(image));  
			typeTextField.setText("  " + poi.getPlaceInterestType());
			attractionTextField.setText("  "+poi.getPlaceName());
			activitiesTextField.setText("  "+poi.getMustSeeOrDo());
			ratingTextField.setText("  "+String.valueOf(poi.getRating()));
			//moreInfoButtonSub2.setText("  More info of "+poi.getPlaceName());
			moreInfoButton.setFocusPainted(false);
			moreInfoButton.setMargin(new Insets(0,0,0,0));
			moreInfoButton.setContentAreaFilled(false);
			moreInfoButton.setBorderPainted(false);
			moreInfoButton.setOpaque(false);
			moreInfoButton.setText("<html><div alight=center><u>Learn more about "+poi.getPlaceName()+"</u></div></html>");
			moreInfoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			imagePointer=1;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
    
	/* Open Wiki pages to the selected attraction using the name
	 * all spaces and comma in the Attraction name need to be replace with '_' when parsing to URL
	 */
	protected void moreInfoOnInternet() {
		try {
			PlacesOfInterest p =  (PlacesOfInterest) (optionPointer==OPTION_SEARCHATTRACTIONBYTYPEORACTIVITIES ? placesFiltered.get(placePointerFiltered) : places.get(placePointer));
            //Desktop.getDesktop().browse(new URL("https://wiki2.org/en/"+p.getPlaceName().replaceAll(" ","_")).toURI());
            Desktop.getDesktop().browse(new URL("https://en.wikipedia.org/wiki/"+p.getPlaceName().replaceAll(" ","_")).toURI());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	/* Open Google Maps search to the selected attraction using the name, can be using address also
	 * all spaces and comma in the Attraction name need to be replace with '+' or '%20' when parsing to URL
	 */
	protected void mapsTheLocation() {
		try {
			PlacesOfInterest p =  (PlacesOfInterest) (optionPointer==OPTION_SEARCHATTRACTIONBYTYPEORACTIVITIES ? placesFiltered.get(placePointerFiltered) : places.get(placePointer));
            //Desktop.getDesktop().browse(new URL("https://wiki2.org/en/"+p.getPlaceName().replaceAll(" ","_")).toURI());
            Desktop.getDesktop().browse(new URL("https://maps.google.com/?q="+p.getPlaceName().replaceAll(", ","+").replaceAll(" ","+")).toURI());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	
	/* display a combobox loaded with the list of attraction type for user selection 
	 * Combobox is presented in a JOptionPane, the font size of the 
	 * message, button has been increased for better readability.
	 * showAttractionTypeSelection() > askAttractionTypeSelect() > AttractionTypeResponse()
	 * */
	private void showAttractionTypeSelection()	{
		ArrayList<String> attractionType = new ArrayList<String>();
		for (int i=0; i<places.size(); i++) {
			PlacesOfInterest p = (PlacesOfInterest) places.get(i);
			if (i==0) {
				attractionType.add(p.getPlaceInterestType());
			} else {
				boolean notAlreadyExist = true;
				for (int j=0; j<attractionType.size(); j++) {
					if (attractionType.get(j).equals(p.getPlaceInterestType())) {
						notAlreadyExist = false;
						break;
					}
				}
				if (notAlreadyExist)	{
					attractionType.add(p.getPlaceInterestType());
				}
			}
		}
		Collections.sort(attractionType);
		String filteredAttractionType = askAttractionTypeSelect(attractionType);
		if (filteredAttractionType!=null && !filteredAttractionType.equals("")) {
			filterPlacesByAttractionType(filteredAttractionType);
			optionPointer=OPTION_SEARCHATTRACTIONBYTYPEORACTIVITIES;
			subPanel.setVisible(true);
			mainPanel.setVisible(false); 
			placePointerFiltered = 0;
			loadPlaceInfoFiltered(placePointerFiltered);
		}
		
	}
		
	public static String askAttractionTypeSelect(ArrayList<String> list1) {

        String result = null;

        if (EventQueue.isDispatchThread()) {

            JPanel panel = new JPanel();
            JLabel askLabel = new JLabel("Please make a selection:");
            askLabel.setFont(new FontUIResource(new Font(  
    		          "Arial", Font.PLAIN, 24)));
            panel.add(askLabel);
            
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
            for (int i=0; i<list1.size(); i++) {
            	model.addElement(list1.get(i));
            }
            JComboBox<String> comboBox = new JComboBox<String>(model);
            panel.add(comboBox);
            comboBox.setFont(new FontUIResource(new Font(  
		          "Arial", Font.ITALIC, 24)));
            int iResult = JOptionPane.showConfirmDialog(null, panel, "Select Attraction Type", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (iResult) {
                case JOptionPane.OK_OPTION:
                    result = (String) comboBox.getSelectedItem();
                    break;
            }

        } else {

        	AttractionTypeResponse response = new AttractionTypeResponse(list1);
            try {
                SwingUtilities.invokeAndWait(response);
                result = response.getResponse();
            } catch (InterruptedException ex) {
            	ex.printStackTrace(); 
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }

        }

        return result;

    }
	
	/* create a filtered ArrayList of PlacesOfInterest according to the selected Attraction Type */
	private void filterPlacesByAttractionType(String attractionType)	{
		// clear the ArrayList before use.
		placesFiltered.clear();
		for (int i=0; i<places.size(); i++)	{
			PlacesOfInterest p = (PlacesOfInterest) places.get(i);
			if (p.getPlaceInterestType().equals(attractionType))	{
				placesFiltered.add(p);
			}
		}
		
		
		
	}

	/* display a combobox loaded with the list of attraction for user selection 
	 * Combobox is presented in a JOptionPane, the font size of the 
	 * message, button has been increased for better readability.
	 * showAttractionListSelection() > askAttrractionListSelect() > AttractionListResponse()
	 * */
	private void showAttractionListSelection()	{
		ArrayList<String> attractionList = new ArrayList<String>();
		for (int i=0; i<places.size(); i++) {
			PlacesOfInterest p = (PlacesOfInterest) places.get(i);
			attractionList.add(p.getPlaceName());
				
			
		}
		int yourRespond =  askAttractionListSelect(attractionList);
		if (yourRespond >= 0) {
			placePointer = yourRespond;
			optionPointer=OPTION_DISPLAYATTRACTIONSFORSELECTION;
			subPanel.setVisible(true);
			mainPanel.setVisible(false);
			loadPlaceInfo(placePointer);
		}
	}
	
	
	public static int askAttractionListSelect(ArrayList<String> list1) {

        int result = -1;

        if (EventQueue.isDispatchThread()) {

            JPanel panel = new JPanel();
            JLabel askLabel = new JLabel("Please make a selection:");
            askLabel.setFont(new FontUIResource(new Font(  
    		          "Arial", Font.PLAIN, 24)));
            panel.add(askLabel);
            
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
            for (int i=0; i<places.size(); i++) {
            	model.addElement(((PlacesOfInterest)places.get(i)).getPlaceName());
            }
            JComboBox<String> comboBox = new JComboBox<String>(model);
            panel.add(comboBox);
            comboBox.setFont(new FontUIResource(new Font(  
		          "Arial", Font.ITALIC, 24)));
            int iResult = JOptionPane.showConfirmDialog(null, panel, "Select Places of Attraction", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (iResult) {
                case JOptionPane.OK_OPTION:
                    //result = (String) comboBox.getSelectedItem();
                	result = comboBox.getSelectedIndex();
                    break;
            }

        } else {

        	AttractionListResponse response = new AttractionListResponse(list1);
            try {
                SwingUtilities.invokeAndWait(response);
                result = response.getResponse();
            } catch (InterruptedException ex) {
            	ex.printStackTrace(); 
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }

        }

        return result;

    }
	
	/* display a combobox loaded with the activities for user selection 
	 * Combobox is presented in a JOptionPane, the font size of the 
	 * message, button has been increased for better readability.
	 * showActivitiesSelection() > askActivitiesSelect() > ActivitiesResponse()
	 * */
	private void showActivitiesSelection()	{
		ArrayList<String> activityList = new ArrayList<String>();
		for (int i=0; i<places.size(); i++) {
			PlacesOfInterest p = (PlacesOfInterest) places.get(i);
			String activity = p.getMustSeeOrDo();
			String[] splitString = activity.split(",");
        	for (int j=0; j<splitString.length; j++)	{
        		String activityToAdd = splitString[j];
        		activityToAdd = activityToAdd.replaceAll("^\\s+|\\s+$", ""); // remove leading & trailing spaces
        		if (activityList.size()==0)	{
        			activityList.add(activityToAdd);
        		} else {
        			boolean notAlreadyExist = true;
    				for (int k=0; k<activityList.size(); k++) {
    					if (activityList.get(k).equals(activityToAdd)) {
    						notAlreadyExist = false;
    						break;
    					}
    				}
    				if (notAlreadyExist)	{
    					activityList.add(activityToAdd);
    				}
        		}
        	}
        	
			
		}
		Collections.sort(activityList);
		String filteredActivities = askActivitiesSelect(activityList);
		if (filteredActivities!=null && !filteredActivities.equals("")) {
			filterPlacesByActivities(filteredActivities);
			optionPointer=OPTION_SEARCHATTRACTIONBYTYPEORACTIVITIES;
			subPanel.setVisible(true);
			mainPanel.setVisible(false);
			placePointerFiltered = 0;
			loadPlaceInfoFiltered(placePointerFiltered);
		}
		
		
	}
	
	
	public static String askActivitiesSelect(ArrayList<String> list1) {

        String result = null;

        if (EventQueue.isDispatchThread()) {

            JPanel panel = new JPanel();
            JLabel askLabel = new JLabel("Please make a selection:");
            askLabel.setFont(new FontUIResource(new Font(  
    		          "Arial", Font.PLAIN, 24)));
            panel.add(askLabel);
            
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
            for (int i=0; i<list1.size(); i++) {
            	model.addElement(list1.get(i));
            }
            JComboBox<String> comboBox = new JComboBox<String>(model);
            panel.add(comboBox);
            comboBox.setFont(new FontUIResource(new Font(  
		          "Arial", Font.ITALIC, 24)));
            int iResult = JOptionPane.showConfirmDialog(null, panel, "Select Activity", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (iResult) {
                case JOptionPane.OK_OPTION:
                    result = (String) comboBox.getSelectedItem();
                    break;
            }

        } else {

        	ActivitiesResponse response = new ActivitiesResponse(list1);
            try {
                SwingUtilities.invokeAndWait(response);
                result = response.getResponse();
            } catch (InterruptedException ex) {
            	ex.printStackTrace(); 
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }

        }

        return result;

    }
	
	/* create a filtered ArrayList of PlacesOfInterest according to the selected Activities */
	private void filterPlacesByActivities(String activitiesType)	{
		// clear the ArrayList before use
		placesFiltered.clear();
		for (int i=0; i<places.size(); i++)	{
			PlacesOfInterest p = (PlacesOfInterest) places.get(i);
			if (p.getMustSeeOrDo().matches("(.*)"+activitiesType+"(.*)"))	{
				placesFiltered.add(p);
			}
		}
				
	}
	
	private void restoreMainImageDisplay()	{
		// when back to main screen, restore the main.png to the ImageView object
		try {
			BufferedImage image = null;
			image = ImageIO.read(new File("img"+fs2+"main.png"));
			imageDisplayLabel.setIcon(new ImageIcon(image));
		} catch (Exception e) {
			
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public static class AttractionTypeResponse implements Runnable {

        private ArrayList<String> values;
        private String response;

        public AttractionTypeResponse(ArrayList<String> values) {
            this.values = values;
        }

        @Override
        public void run() {
            response = askAttractionTypeSelect(values);
        }

        public String getResponse() {
            return response;
        }
    }
    
    public static class AttractionListResponse implements Runnable {

        private ArrayList<String> values;
        private int response;

        public AttractionListResponse(ArrayList<String> values) {
            this.values = values;
        }

        @Override
        public void run() {
            response = askAttractionListSelect(values);
        }

        public int getResponse() {
            return response;
        }
    }
    
    public static class ActivitiesResponse implements Runnable {

        private ArrayList<String> values;
        private String response;

        public ActivitiesResponse(ArrayList<String> values) {
            this.values = values;
        }

        @Override
        public void run() {
            response = askActivitiesSelect(values);
        }

        public String getResponse() {
            return response;
        }
    }
    
    public static final class OsUtils
    {
       private static String OS = null;
       public static String getOsName()
       {
          if(OS == null) { OS = System.getProperty("os.name"); }
          return OS;
       }
       public static boolean isWindows()
       {
          return getOsName().startsWith("Windows");
       }

       public static boolean isUnix() // and so on
       {
    	   return getOsName().startsWith("Unix");
       }
    }
}
