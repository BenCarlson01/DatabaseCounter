package dbCount;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Main {
	
	/*
	 * Creates the main GUI window
	 */
	public static void main(String[] args) {
		/*
		JFrame frame = new JFrame("DBCounter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel test = new JLabel("Test");
		frame.getContentPane().add(test, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		*/
		Database db = new Database("basic.txt");
		
		JFrame frame = new JFrame("ListDemo");
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

}
