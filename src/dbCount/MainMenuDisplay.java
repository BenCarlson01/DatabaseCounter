package dbCount;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;


public class MainMenuDisplay extends JPanel implements ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private JList<String> listDatabases;
    private DefaultListModel<String> listModelDatabases;

    private static final String loadString = "Load";
    private static final String createString = "New";
    private JButton createButton;
    
    private JScrollPane listScrollPane;
    private JButton loadButton;
    private JPanel buttonPane;
    private JTextField employeeName;
    private JLabel dbLabel, instructionLabel;
    
    private JTabbedPane tabbedPane;
    
    private JFrame createFrame;
    
    private int tabs;
    private ArrayList<Database> dbs;
    

    public MainMenuDisplay() {
    	super(new BorderLayout());
    	dbs = new ArrayList<>();
        
        loadButton = new JButton(loadString);
        //AddListener AddListener = new AddListener(loadButton);
        loadButton.setActionCommand(loadString);
        loadButton.addActionListener(new LoadListener());
        
        createButton = new JButton(createString);
        createButton.setActionCommand(createString);
        createButton.addActionListener(new CreateListener());


        //Create a panel that uses BoxLayout.
        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.PAGE_AXIS));
        //buttonPane.add(createButton);
        //buttonPane.add(Box.createHorizontalStrut(5));
        //buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        //buttonPane.add(Box.createHorizontalStrut(5));
        //buttonPane.add(employeeName);
        
        dbLabel = new JLabel("No databases currently loaded.");
        dbLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionLabel = new JLabel("Please load a database.");
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        buttonPane.add(dbLabel);
        buttonPane.add(instructionLabel);
        buttonPane.add(createButton);
        buttonPane.add(loadButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BorderLayout());
        mainMenuPanel.add(buttonPane, BorderLayout.WEST);
        
        
        
        
    	File folder = new File("Saved Databases/");
    	File[] listOfFiles = folder.listFiles();
    	
        listModelDatabases = new DefaultListModel<>();
        for (int i = 0; i < listOfFiles.length; i++) {
        	String temp = listOfFiles[i].getName();
        	listModelDatabases.addElement(temp.substring(0, temp.length() - 4));
        }

        //Create the list and put it in a scroll pane.
        listDatabases = new JList<>(listModelDatabases);
        listDatabases.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //listDatabases.setSelectedIndex(0);
        listDatabases.setVisibleRowCount(5);
        //listDatabases.addListSelectionListener(new LoadListener());
        listScrollPane = new JScrollPane(listDatabases);
        
        JLabel dbHeader = new JLabel("Saved Databases");
        JPanel dbPane = new JPanel();
        dbPane.setLayout(new BoxLayout(dbPane, BoxLayout.PAGE_AXIS));
        dbPane.add(dbHeader);
        dbPane.add(listScrollPane);
        mainMenuPanel.add(dbPane, BorderLayout.CENTER);
        
        
        
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Main Menu", mainMenuPanel);
        tabs = 1;

        add(tabbedPane, BorderLayout.CENTER);
    }
    
    public void save() {
    	for (Database db : dbs) {
    		db.save();
    	}
    }

    class LoadListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
            String name = listDatabases.getSelectedValue();
            if (name == null) {
            	return;
            }
    		
            Database db = new Database(name + ".txt");
            dbs.add(db);
	        JComponent panel = new Display(db);
	        tabbedPane.addTab(name, panel);
	        tabbedPane.setSelectedIndex(tabs);
	        //tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	        tabs++;
	        
    	}
    }

    class CreateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            createDB();
        }
    }
    
    //This listener is shared by the text field and the hire button.
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = employeeName.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                employeeName.requestFocusInWindow();
                employeeName.selectAll();
                return;
            }

            int index = 0;

            listModelDatabases.insertElementAt(employeeName.getText(), index);
            //If we just wanted to add to the end, we'd do this:
            //listModelDatabases.addElement(employeeName.getText());

            //Reset the text field.
            employeeName.requestFocusInWindow();
            employeeName.setText("");

            //Select the new item and make it visible.
            listDatabases.setSelectedIndex(index);
            listDatabases.ensureIndexIsVisible(index);
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModelDatabases.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (listDatabases.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                createButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                createButton.setEnabled(true);
            }
        }
    }
    
	private void createDB() {
		createFrame = new JFrame("Database Counter");
		createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new CreatePanel();
        newContentPane.setOpaque(true); //content panes must be opaque
        createFrame.setContentPane(newContentPane);

        //Display the window.
        createFrame.pack();
        createFrame.setVisible(true);
	}
    
	private class CreatePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private JTextField nameField;
		
		private CreatePanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			JLabel nameLabel = new JLabel("Please enter the Database name.");
			nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			nameField = new JTextField(10);
			nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			JButton nameButton = new JButton("Create");
			nameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			nameButton.addActionListener(new SubCreateListener());
			
			add(nameLabel);
			add(nameField);
			add(nameButton);
		}
		
		private class SubCreateListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
	            Database newDB = new Database(name + ".txt");
	            dbs.add(newDB);
	            JComponent panel = new Display(newDB);
		        tabbedPane.addTab(name, panel);
		        tabbedPane.setSelectedIndex(tabs);
		        tabs++;
		        createFrame.dispatchEvent(new WindowEvent(createFrame, WindowEvent.WINDOW_CLOSING));
	        }
		}
	}

    /*
	private void loadDB(String name) {
		Database db = new Database(name);
		
		JFrame frame = new JFrame("Database Counter");
		WindowListener exitListener = new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	db.save();
		    }
		};
		frame.addWindowListener(exitListener);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new Display(db);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}
	*/
    
}