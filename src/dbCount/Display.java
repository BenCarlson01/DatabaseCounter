package dbCount;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;


public class Display extends JPanel implements ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private JList<String> listNames;
    private DefaultListModel<String> listModelNames;
    
    private JList<String> listCounts;
    private DefaultListModel<String> listModelCounts;

    private static final String addString = "Add";
    private static final String removeString = "Remove";
    private JButton removeButton;
    private JTextField itemName, itemCount, customField, searchBar;
    private JLabel itemLabel;
    private JPanel incrementPanel;
    
    private Database db;

    public Display(Database db) {
    	super(new BorderLayout());
    	this.db = db;


    	itemLabel = new JLabel("None");
        listModelNames = new DefaultListModel<>();
        listModelCounts = new DefaultListModel<>();
        TreeSet<String> names = db.getData();
        for (String s : names) {
        	listModelNames.addElement(s);
        }

        //Create the list and put it in a scroll pane.
        listNames = new JList<>(listModelNames);
        listNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listNames.setSelectedIndex(0);
        listNames.setVisibleRowCount(5);
        listNames.addListSelectionListener(new NameListener());
        //JScrollPane listScrollPane = new JScrollPane(listNames);
        
        HashMap<String, Integer> count = db.getMap();
        for (String s : names) {
        	listModelCounts.addElement("" + count.get(s));
        }
        

        listCounts = new JList<>(listModelCounts);
        listCounts.setSelectionModel(new NoSelectionModel());
        JScrollPane listScrollPane = new JScrollPane(listCounts);
        JViewport namesViewer = new JViewport();
        namesViewer.setView(listNames);
        listScrollPane.setRowHeader(namesViewer);

        add(listScrollPane, BorderLayout.CENTER);
        
        

    	
    	JPanel boxPanel = new JPanel();
    	boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.PAGE_AXIS));
    	
    	incrementPanel = new JPanel();
    	incrementPanel.setLayout(new FlowLayout());
    	
        removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());
        
        JButton plus1Button = new JButton("+1");
        plus1Button.addActionListener(new Incrementer(1));
        plus1Button.setPreferredSize(new Dimension(28, 20));
        
        JButton plus5Button = new JButton("+5");
        plus5Button.addActionListener(new Incrementer(5));
        
        JButton plus10Button = new JButton("+10");
        plus10Button.addActionListener(new Incrementer(10));
        
        JLabel selectedLabel = new JLabel("Selected:");
        
        JButton customButton = new JButton("+");
        CustomListener csListener = new CustomListener(customButton);
        customButton.addActionListener(csListener);
        customButton.setPreferredSize(new Dimension(20, 20));
        customButton.setEnabled(false);
        
        customField = new JTextField(4);
        customField.addActionListener(csListener);
        customField.getDocument().addDocumentListener(csListener);
        
        incrementPanel.add(selectedLabel);
    	incrementPanel.add(itemLabel);
    	incrementPanel.add(plus1Button);
    	//incrementPanel.add(plus5Button);
    	//incrementPanel.add(plus10Button);
    	incrementPanel.add(customButton);
    	incrementPanel.add(customField);
    	incrementPanel.add(removeButton);
        
    	boxPanel.add(incrementPanel);
        
        

        
        JButton addButton = new JButton(addString);
        AddListener adder = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(adder);
        addButton.setEnabled(false);

        JLabel nameLabel = new JLabel("Name:");
        itemName = new JTextField(10);
        itemName.addActionListener(adder);
        itemName.getDocument().addDocumentListener(adder);
        
        JLabel countLabel = new JLabel("Count:");
        itemCount = new JTextField(4);
        
        JPanel addPane = new JPanel();
        addPane.setLayout(new FlowLayout());
        addPane.add(nameLabel);
        addPane.add(itemName);
        addPane.add(countLabel);
        addPane.add(itemCount);
        addPane.add(addButton);
        /*
        itemName.addActionListener(AddListener);
        itemName.getDocument().addDocumentListener(AddListener);
        String name = listModelNames.getElementAt(
                              listNames.getSelectedIndex()).toString();
        */
        /*
        itemName = new JTextField(10);
        itemName.addActionListener(AddListener);
        itemName.getDocument().addDocumentListener(AddListener);
        String name = listModelNames.getElementAt(
                              list.getSelectedIndex()).toString();
                              */


        //Create a panel that uses BoxLayout.
        /*
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(itemName);
        buttonPane.add(addButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        */
        
        boxPanel.add(addPane);
        add(boxPanel, BorderLayout.PAGE_END);
        
        searchBar = new JTextField(25);
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateList();
			}
			  
			public void removeUpdate(DocumentEvent e) {
				updateList();
			}
			  
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}
        });
        
        JLabel searchLabel = new JLabel("Search: ");
        JPanel searchPane = new JPanel(new FlowLayout());
        searchPane.add(searchLabel);
        searchPane.add(searchBar);
        add(searchPane, BorderLayout.PAGE_START);
    }
    
    private void updateList() {
    	String prefix = searchBar.getText();
    	
    	listModelNames.removeAllElements();
    	TreeSet<String> names = db.getPrefix(prefix);
        for (String s : names) {
        	listModelNames.addElement(s);
        }

        listModelCounts.removeAllElements();
        HashMap<String, Integer> count = db.getMap();
        for (String s : names) {
        	listModelCounts.addElement("" + count.get(s));
        }
    	
    }
    
    class NameListener implements ListSelectionListener {
    	public void valueChanged(ListSelectionEvent e) {
        	int cur = listNames.getSelectedIndex();
        	if (cur == -1) {
        		removeButton.setEnabled(false);
        	} else {
        		removeButton.setEnabled(true);
        		String curName = (String) listModelNames.get(cur);
        		itemLabel.setText(curName);
        	}
    	}
    }
    
    private class Incrementer implements ActionListener {
    	private int amt;
    	
    	private Incrementer(int amount) {
    		amt = amount;
    	}
    	
    	public void actionPerformed(ActionEvent e) {
    		int cur = listNames.getSelectedIndex();
        	if (cur != -1) {
        		int curCount = Integer.parseInt(listModelCounts.get(cur));
        		curCount += amt;
        		db.put(listNames.getSelectedValue(), curCount);
        		listModelCounts.set(cur, "" + curCount);
        	}
    	}
    }
    
    private class CustomListener implements ActionListener, DocumentListener {
    	private boolean alreadyEnabled = false;
        private JButton button;

        public CustomListener(JButton button) {
            this.button = button;
        }
    	public void actionPerformed(ActionEvent e) {
    		int cur = listNames.getSelectedIndex();
        	if (cur != -1) {
        		int curCount = Integer.parseInt(listModelCounts.get(cur));
        		try {
        			curCount += Integer.parseInt(customField.getText());
        		} catch (NumberFormatException ex) {
        			return;
        		}
        		db.put(listNames.getSelectedValue(), curCount);
        		listModelCounts.set(cur, "" + curCount);
        	}
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

    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = listNames.getSelectedIndex();
            if (index == -1) {
            	return;
            }
            db.remove(listNames.getSelectedValue());
            listModelNames.remove(index);
            listModelCounts.remove(index);

            int size = listModelNames.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModelNames.getSize()) {
                    //removed item in last position
                    index--;
                }

                //listNames.setSelectedIndex(index);
                //listNames.ensureIndexIsVisible(index);
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
            String name = itemName.getText();

            int count;
            try {
            	count = Integer.parseInt(itemCount.getText());
            } catch (NumberFormatException ex) {
            	count = 1;
            }

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                itemName.requestFocusInWindow();
                itemName.selectAll();
                return;
            }

            int index = 0;
            TreeSet<String> order = db.getData();
            for (String s : order) {
            	if (db.comp(name, s) <= 0) {
            		break;
            	}
            	index++;
            }

            listModelNames.insertElementAt(itemName.getText(), index);
            listModelCounts.insertElementAt("" + count, index);
            db.put(name, count);
            //If we just wanted to add to the end, we'd do this:
            //listModelNames.addElement(itemName.getText());

            //Reset the text field.
            itemName.requestFocusInWindow();
            itemName.setText("");

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
            //No selection, disable remove button.
                removeButton.setEnabled(false);

            } else {
            //Selection, enable the remove button.
                removeButton.setEnabled(true);
            }
        }
    }
    
    private static class NoSelectionModel extends DefaultListSelectionModel {

	   /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	@Override
	   public void setAnchorSelectionIndex(final int anchorIndex) { }

	   @Override
	   public void setLeadAnchorNotificationEnabled(final boolean flag) { }

	   @Override
	   public void setLeadSelectionIndex(final int leadIndex) { }

	   @Override
	   public void setSelectionInterval(final int index0, final int index1) { }
    } 
}
