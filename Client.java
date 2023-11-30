import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class Client extends JComponent implements Runnable {
    private JFrame frame;
    private JTextField username;
    private JTextField password;

    private JButton enter;
    private JButton createAccount;
    private int choice = -1;
    public Client() {
        this.frame = new JFrame("Calendar");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }

    public void run() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new CardLayout());
        CardLayout cardLayout = (CardLayout)  mainPanel.getLayout();

        String[] selOp = new String[] {
                "View Approved", "Appointment Requests",
                "Create Store", "Create Calendar",
                "Edit Calendar", "Delete Calendar",
                "Show Statistics", "Import Calendar",
                "Logout", "Quit"
        };
        String[] selPans = new String[] {
                "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9"
        };


        //maybe exit works?
        frame.setDefaultCloseOperation(exit());
        frame.setVisible(true);

        JPanel user = new JPanel();
        user.setLayout(new BoxLayout(user, BoxLayout.Y_AXIS));
        JPanel account = new JPanel();
        username = new JTextField("username", 10);
        password = new JTextField("password", 10);
        enter = new JButton("Enter");
        enter.addActionListener(actionListener);
        account.add(new JLabel("Account Login"));
        account.add(username);
        account.add(password);
        account.add(enter);

        JPanel create = new JPanel();
        createAccount = new JButton("Create Account");
        createAccount.addActionListener(actionListener);
        create.add(new JLabel("Don't have an account?"), BorderLayout.PAGE_START);
        create.add(createAccount, BorderLayout.PAGE_END);

        user.add(account);
        user.add(create);
        mainPanel.add(user);

        String[] custOps = {"View Calendar", "Request Appointment", "Cancel Appointment", "View Approved",
                "Show Statistics", "Logout", "Quit"};
        String[] custPans = {"s0", "s1", "s2", "s3", "s3", "s4", "s5", "s6"};
        JPanel custSide = createCardPanel("Calendar: Customer");
        JButton enter = new JButton("Confirm");

        custSide.add(enter);

        JComboBox<String> customerOptions = new JComboBox<>(custOps);
        JLabel custResult = new JLabel("");
        enter.addActionListener(e -> {
            String selectedOption = (String) customerOptions.getSelectedItem();
            int choice = Arrays.asList().indexOf(selectedOption);

            switch (choice) {
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:

                    break;
            }

            custResult.setText("Selected Option:" + selectedOption);
            cardLayout.show(mainPanel, custPans[choice]);
        });

        custSide.add(customerOptions);
        custSide.add(customerOptions);
        mainPanel.add(custSide);

        JPanel selSide = createCardPanel("Calendar: Seller");
        JButton confirm = new JButton("Confirm");
        //----------------------------------------------------------//
        selSide.add(confirm);
        //add this button to every single card - it's going to be
        //what sends the request

        //----------------------------------------------------------//
        JComboBox<String> sellerOptions = new JComboBox<>(selOp);
        JLabel resultLabel = new JLabel("");
        confirm.addActionListener(e -> {
            String selectedOption = (String) sellerOptions.getSelectedItem();
            int choice = Arrays.asList(selOp).indexOf(selectedOption);

            //SEND REQUEST HERE
            switch (choice) {
                case 0 -> {

                }
                case 1 -> {

                }
                case 2 -> {

                }
                case 3 -> {

                }
                case 4 -> {

                }
                case 5 -> {

                }
                case 6 -> {

                }
                case 7 -> {

                }
                case 8 -> {

                }
                case 9 -> {

                }
            }
            //WAIT FOR REPLY THEN
            //cardLayout.show(THE PANEL SELECTED HERE)
            resultLabel.setText("Selected Option: " + selectedOption);
            cardLayout.show(mainPanel, selPans[choice]);
        });

        selSide.add(sellerOptions);
        selSide.add(resultLabel);
        mainPanel.add(selSide);

        //whatever option specific buttons and the like go into their
        //own slot here:
        //----------------------------------------------------------//
        JPanel s0 = createCardPanel(selOp[0]);

        //----------------------------------------------------------//
        JPanel s1 = createCardPanel(selOp[1]);

        //----------------------------------------------------------//
        JPanel s2 = createCardPanel(selOp[2]);

        //----------------------------------------------------------//
        JPanel s3 = createCardPanel(selOp[3]);

        //----------------------------------------------------------//
        JPanel s4 = createCardPanel(selOp[4]);

        //----------------------------------------------------------//
        JPanel s5 = createCardPanel(selOp[5]);

        //----------------------------------------------------------//
        JPanel s6 = createCardPanel(selOp[6]);

        //----------------------------------------------------------//
        JPanel s7 = createCardPanel(selOp[7]);

        //----------------------------------------------------------//
        JPanel s8 = createCardPanel(selOp[8]);

        //----------------------------------------------------------//
        JPanel s9 = createCardPanel(selOp[9]);

        //----------------------------------------------------------//

        mainPanel.add(s0, "s0");
        mainPanel.add(s1, "s1");
        mainPanel.add(s2, "s2");
        mainPanel.add(s3, "s3");
        mainPanel.add(s4, "s4");
        mainPanel.add(s5, "s5");
        mainPanel.add(s6, "s6");
        mainPanel.add(s7, "s7");
        mainPanel.add(s8, "s8");
        mainPanel.add(s9, "s9");

        frame.add(mainPanel);

    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enter) {
                String Username = username.getText();
                String Password = password.getText();

                if (Username.equals("username") || Username.isEmpty() || Password.equals("password") || Password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an email and password or create an account.");
                } else {
                    // TODO: check the username and password to make sure it is valid (SQL Query) and
                    //  then change to specific calendar (seller vs. customer)
                    String type = "";
                    // SQL Query: 'SELECT type FROM accounts WHERE (email == Username AND password == Password)'
                    if (type == "c") {
                        //TODO: make ian's code methods and call them here?
                    } else if (type == "s") {
                        //TODO: same here as previous TODO
                    } else {
                        JOptionPane.showMessageDialog(null, "Username or Password are incorrect");
                    }
                }
            } else if (e.getSource() == createAccount) {
                // TODO: change panel and display text fields for user to enter type, email, and password
                // SQL Query: 'INSERT INTO accounts (type, email, password, password) VALUES (userInput, userInput, userInput)'
            }
        }
    };

    private static JPanel createCardPanel(String text) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(text));
        return panel;
    }

    public static int exit() {
        //send exit message here
        return JFrame.DISPOSE_ON_CLOSE;
    }
}
