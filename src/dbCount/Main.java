package dbCount;

import java.awt.event.*;

import javax.swing.*;

public class Main {
	
	/*
	 * Creates the main GUI window
	 */
	public static void main(String[] args) {
		TestTrieB test = new TestTrieB();
		test.testInsert();
		/*
		JFrame frame = new JFrame("Database Counter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainMenuDisplay newContentPane = new MainMenuDisplay();
        newContentPane.setOpaque(true);
		WindowListener exitListener = new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	newContentPane.save();
		    }
		};
		frame.addWindowListener(exitListener);
        frame.setContentPane(newContentPane);
		frame.setSize(400, 400);
		frame.setVisible(true);
		*/
	}
}
