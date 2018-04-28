package dbCount;
import java.awt.BorderLayout;
    import java.awt.Color;
    import java.awt.Component;
    import java.awt.EventQueue;
    import java.util.Vector;

   import javax.swing.DefaultListCellRenderer;
   import javax.swing.JFrame;
   import javax.swing.JList;
   import javax.swing.JScrollPane;

   public class UserList {

 public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {

           @Override
           public void run() {
                JFrame f = new JFrame("Users");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(300, 300);

                JList list = new JList(new Vector<User>() {
                     {
                          add(new User("A", false));
                          add(new User("B", true));
                          add(new User("C", true));
                          add(new User("D", false));
                     }
                });

                list.setCellRenderer(new DefaultListCellRenderer() {

                     @Override
                     public Component getListCellRendererComponent(JList list, Object value, int index,
                               boolean isSelected, boolean cellHasFocus) {
                          Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                          if (value instanceof User) {
                               User nextUser = (User) value;
                               setText(nextUser.name);
                               if (nextUser.loggedIn) {
                                    setBackground(Color.GREEN);
                               } else {
                                    setBackground(Color.RED);
                               }
                               if (isSelected) {
                                    setBackground(getBackground().darker());
                               }
                          } else {
                               setText("whodat?");
                          }
                          return c;
                     }

                });
                f.add(new JScrollPane(list), BorderLayout.CENTER);
                f.setVisible(true);
           }
      });
 }

 static class User {
      String name = "NN";
      boolean loggedIn = false;

      public User(String name, boolean loggedIn) {
           this.name = name;
           this.loggedIn = loggedIn;
      }
 }
   }