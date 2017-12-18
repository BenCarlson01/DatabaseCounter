package dbCount;

import java.awt.*;
import java.awt.List;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

/* ListDemo.java requires no other files. */
public class Display extends JPanel implements ListSelectionListener {
    private JList<String> listNames;
    private DefaultListModel<String> listModelNames;
    
    private JList<String> listCounts;
    private DefaultListModel<String> listModelCounts;

    private static final String hireString = "Add";
    private static final String fireString = "Remove";
    private JButton fireButton;
    private JTextField employeeName;
    private JLabel itemLabel;
    private JPanel incrementPanel;
    
    private Database db;

    public Display(Database db) {
    	super(new BorderLayout());
    	this.db = db;
    	
    	JPanel boxPanel = new JPanel();
    	boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.PAGE_AXIS));
    	
    	incrementPanel = new JPanel();
    	incrementPanel.setLayout(new BoxLayout(incrementPanel, BoxLayout.LINE_AXIS));
    	itemLabel = new JLabel("None");
    	incrementPanel.add(itemLabel);
    	
    	boxPanel.add(incrementPanel);

        listModelNames = new DefaultListModel<>();
        listModelCounts = new DefaultListModel<>();
        TreeSet<String> names = db.getData();
        String temp = "";
        for (String s : names) {
        	temp = s;
        	listModelNames.addElement(s);
        }

        if (names.size() == 0) {
        	itemLabel.setText("No Items");
        	listModelNames.addElement("NULL");
        	listModelCounts.addElement("NULL");
        } else {
        	itemLabel.setText(temp);
        }

        //Create the list and put it in a scroll pane.
        listNames = new JList<>(listModelNames);
        listNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listNames.setSelectedIndex(0);
        listNames.setVisibleRowCount(5);
        listNames.addListSelectionListener(new IncrementListener());
        //JScrollPane listScrollPane = new JScrollPane(listNames);
        
        HashMap<String, Integer> count = db.getMap();
        for (String s : names) {
        	listModelCounts.addElement("" + count.get(s));
        }
        

        listCounts = new JList<>(listModelCounts);
        //listCounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //listCounts.setSelectedIndex(0);
        //listCounts.setVisibleRowCount(5);
        //listCounts.addListSelectionListener(new IncrementListener());
        JScrollPane listScrollPane = new JScrollPane(listCounts);
        JViewport namesViewer = new JViewport();
        namesViewer.setView(listNames);
        listScrollPane.setRowHeader(namesViewer);
        
        
        
        

        
        JButton hireButton = new JButton(hireString);
        AddListener AddListener = new AddListener(hireButton);
        hireButton.setActionCommand(hireString);
        hireButton.addActionListener(AddListener);
        hireButton.setEnabled(false);

        fireButton = new JButton(fireString);
        fireButton.setActionCommand(fireString);
        fireButton.addActionListener(new FireListener());

        employeeName = new JTextField(10);
        employeeName.addActionListener(AddListener);
        employeeName.getDocument().addDocumentListener(AddListener);
        String name = listModelNames.getElementAt(
                              listNames.getSelectedIndex()).toString();
        /*
        employeeName = new JTextField(10);
        employeeName.addActionListener(AddListener);
        employeeName.getDocument().addDocumentListener(AddListener);
        String name = listModelNames.getElementAt(
                              list.getSelectedIndex()).toString();
                              */


        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(fireButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(employeeName);
        buttonPane.add(hireButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        boxPanel.add(buttonPane);

        add(listScrollPane, BorderLayout.CENTER);
        add(boxPanel, BorderLayout.PAGE_END);
    }
    
    class IncrementListener implements  ListSelectionListener {
    	public void valueChanged(ListSelectionEvent e) {
        	int cur = listNames.getSelectedIndex();
        	if (cur != -1) {
        		String curName = (String) listModelNames.get(cur);
        		itemLabel.setText(curName);
        	}
    	}
    }

    class FireListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = listNames.getSelectedIndex();
            listModelNames.remove(index);

            int size = listModelNames.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                fireButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModelNames.getSize()) {
                    //removed item in last position
                    index--;
                }

                listNames.setSelectedIndex(index);
                listNames.ensureIndexIsVisible(index);
            }
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
            String LCName = name.toLowerCase();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                employeeName.requestFocusInWindow();
                employeeName.selectAll();
                return;
            }

            int index = 0;
            TreeSet<String> order = db.getData();
            for (String s : order) {
            	if (LCName.compareTo(s.toLowerCase()) <= 0) {
            		if (name.compareTo(s.toLowerCase()) > 0) {
            			break;
            		}
            	}
            	index++;
            }

            listModelNames.insertElementAt(employeeName.getText(), index);
            db.put(name, 1);
            //If we just wanted to add to the end, we'd do this:
            //listModelNames.addElement(employeeName.getText());

            //Reset the text field.
            employeeName.requestFocusInWindow();
            employeeName.setText("");

            //Select the new item and make it visible.
            listNames.setSelectedIndex(index);
            listNames.ensureIndexIsVisible(index);
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModelNames.contains(name);
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

            if (listNames.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                fireButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                fireButton.setEnabled(true);
            }
        }
    }
    
}
