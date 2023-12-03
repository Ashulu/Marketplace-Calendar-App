import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
public class Client extends JComponent implements Runnable {
//    userInput
//private JFrame frame;
//    private JTextField username;
//    private JTextField password;
//    private JButton enter;
//    private JButton createAccount;
//    private int choice = -1;
//    public Client() {
//        this.frame = new JFrame("Calendar");
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Client());
//    }
//
//    public void run() {
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        Container content = frame.getContentPane();
//        content.setLayout(new BorderLayout());
//
//        frame.setSize(600, 400);
//        frame.setLocationRelativeTo(null);
//
//        JPanel mainPanel = new JPanel(new CardLayout());
//        CardLayout cardLayout = (CardLayout)  mainPanel.getLayout();
//
//        String[] selOp = new String[] {
//                "View Approved", "Appointment Requests",
//                "Create Store", "Create Calendar",
//                "Edit Calendar", "Delete Calendar",
//                "Show Statistics", "Import Calendar",
//                "Logout", "Quit"
//        };
//        String[] selPans = new String[] {
//                "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9"
//        };
//
//
//        //maybe exit works?
//        frame.setDefaultCloseOperation(exit());
//        frame.setVisible(true);
//
//        JPanel user = new JPanel();
//        user.setLayout(new BoxLayout(user, BoxLayout.Y_AXIS));
//        JPanel account = new JPanel();
//        username = new JTextField("username", 10);
//        password = new JTextField("password", 10);
//        enter = new JButton("Enter");
//        enter.addActionListener(actionListener);
//        account.add(new JLabel("Account Login"));
//        account.add(username);
//        account.add(password);
//        account.add(enter);
//
//        JPanel create = new JPanel();
//        createAccount = new JButton("Create Account");
//        createAccount.addActionListener(actionListener);
//        create.add(new JLabel("Don't have an account?"), BorderLayout.PAGE_START);
//        create.add(createAccount, BorderLayout.PAGE_END);
//
//        user.add(account);
//        user.add(create);
//        mainPanel.add(user);
//
//
//        JPanel selSide = createCardPanel("Calendar: Seller");
//        JButton confirm = new JButton("Confirm");
//        //----------------------------------------------------------//
//        selSide.add(confirm);
//        //add this button to every single card - it's going to be
//        //what sends the request
//
//        //----------------------------------------------------------//
//        JComboBox<String> sellerOptions = new JComboBox<>(selOp);
//        JLabel resultLabel = new JLabel("");
//        confirm.addActionListener(e -> {
//            String selectedOption = (String) sellerOptions.getSelectedItem();
//            int choice = Arrays.asList(selOp).indexOf(selectedOption);
//
//            //SEND REQUEST HERE
//            switch (choice) {
//                case 0 -> {
//
//                }
//                case 1 -> {
//
//                }
//                case 2 -> {
//
//                }
//                case 3 -> {
//
//                }
//                case 4 -> {
//
//                }
//                case 5 -> {
//
//                }
//                case 6 -> {
//
//                }
//                case 7 -> {
//
//                }
//                case 8 -> {
//
//                }
//                case 9 -> {
//
//                }
//            }
//            //WAIT FOR REPLY THEN
//            //cardLayout.show(THE PANEL SELECTED HERE)
//            resultLabel.setText("Selected Option: " + selectedOption);
//            cardLayout.show(mainPanel, selPans[choice]);
//        });
//
//        selSide.add(sellerOptions);
//        selSide.add(resultLabel);
//        mainPanel.add(selSide);
//
//        //whatever option specific buttons and the like go into their
//        //own slot here:
//        //----------------------------------------------------------//
//        JPanel s0 = createCardPanel(selOp[0]);
//
//        //----------------------------------------------------------//
//        JPanel s1 = createCardPanel(selOp[1]);
//
//        //----------------------------------------------------------//
//        JPanel s2 = createCardPanel(selOp[2]);
//
//        //----------------------------------------------------------//
//        JPanel s3 = createCardPanel(selOp[3]);
//
//        //----------------------------------------------------------//
//        JPanel s4 = createCardPanel(selOp[4]);
//
//        //----------------------------------------------------------//
//        JPanel s5 = createCardPanel(selOp[5]);
//
//        //----------------------------------------------------------//
//        JPanel s6 = createCardPanel(selOp[6]);
//
//        //----------------------------------------------------------//
//        JPanel s7 = createCardPanel(selOp[7]);
//
//        //----------------------------------------------------------//
//        JPanel s8 = createCardPanel(selOp[8]);
//
//        //----------------------------------------------------------//
//        JPanel s9 = createCardPanel(selOp[9]);
//
//        //----------------------------------------------------------//
//
//        mainPanel.add(s0, "s0");
//        mainPanel.add(s1, "s1");
//        mainPanel.add(s2, "s2");
//        mainPanel.add(s3, "s3");
//        mainPanel.add(s4, "s4");
//        mainPanel.add(s5, "s5");
//        mainPanel.add(s6, "s6");
//        mainPanel.add(s7, "s7");
//        mainPanel.add(s8, "s8");
//        mainPanel.add(s9, "s9");
//
//        frame.add(mainPanel);
//
//    }
//
//    ActionListener actionListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (e.getSource() == enter) {
//                String Username = username.getText();
//                String Password = password.getText();
//                if (Username.equals("username") || Username.isEmpty() || Password.equals("password") || Password.isEmpty()) {
//                    JOptionPane.showMessageDialog(null, "Please enter an email and password or create an account.");
//                } else {
//                    // TODO: check the username and password to make sure it is valid (SQL Query) and
//                    //  then change to specific calendar (seller vs. customer)
//                    String type = "";
//                    // SQL Query: 'SELECT type FROM accounts WHERE (email == Username AND password == Password)'
//                    if (type == "c") {
//                        //TODO: make ian's code methods and call them here?
//                    } else if (type == "s") {
//                        //TODO: same here as previous TODO
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Username or Password are incorrect");
//                    }
//                }
//            } else if (e.getSource() == createAccount) {
//                // TODO: change panel and display text fields for user to enter type, email, and password
//                // SQL Query: 'INSERT INTO accounts (type, email, password, password) VALUES (userInput, userInput, userInput)'
//            }
//        }
//    };

    JPanel loginPanel;
    JPanel createPanel;
    JButton login;
    JButton createAccount;
    JFrame frame;
    static BufferedReader reader;
    static PrintWriter writer;


    public void creationPanel() {
        JLabel createLabel = new JLabel("Account creation");
        createPanel.add(createLabel);

        JTextField userField = new JTextField("Email", 30);
        createPanel.add(userField);

        JTextField passField = new JTextField("Password", 30);
        createPanel.add(passField);

        String[] choices = {"Customer", "Seller"};

        JComboBox<String> types = new JComboBox<>(choices);
        createPanel.add(types);
        String type = (String)types.getSelectedItem();

        JButton create = new JButton("Create Account");
        createPanel.add(create);

        JButton back = new JButton("Back");
        createPanel.add(back);



        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send the user and pass to the server to add to the database
                boolean exists = false;
                if (exists) {
                    //server checks if the account already exists and writes true or false back to the client. Exists is then updated to that value

                    JOptionPane.showMessageDialog(frame.getContentPane(), "Account already Exists!");
                } else if (userField.getText().equals("Email") || passField.getText().equals("Password") ){
                    JOptionPane.showMessageDialog(frame.getContentPane(), "Enter an Eamil and/or password");
                } else {
                    //goes back to the main page to then login with the newly created account
                    frame.remove(createPanel);
                    frame.add(loginPanel, BorderLayout.CENTER);
                    frame.pack();
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(createPanel);
                frame.add(loginPanel, BorderLayout.CENTER);
                frame.pack();
            }
        });

    }

    public void run(){
        frame = new JFrame("Calendar");

        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginPage();

        frame.setVisible(true);
    }

    public void loginPage() {

        loginPanel = new JPanel();
        loginPanel.setBounds(0, 0, 30, frame.getHeight());
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        JLabel loginLabel = new JLabel("Login:");
        loginPanel.add(loginLabel);

        JTextField userField = new JTextField("Email", 30);
        loginPanel.add(userField);

        JTextField passField = new JTextField("Password", 30);
        loginPanel.add(passField);

        login = new JButton("Login");
        loginPanel.add(login);

        createAccount = new JButton("Create account");
        loginPanel.add(createAccount);

        frame.add(loginPanel, BorderLayout.CENTER);

        createAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPanel = new JPanel();
                createPanel.setBounds(0, 0, 30, frame.getHeight());
                createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));
                creationPanel();
                frame.remove(loginPanel);
                frame.add(createPanel, BorderLayout.CENTER);
                frame.pack();
            }
        });

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userField.getText().isEmpty() || passField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame.getContentPane(), "Invalid inputs");
                } else {
                    try {
                        writer.println("login," + userField.getText() + "," + passField);
                        writer.flush();

                        int check = Integer.parseInt(reader.readLine());
                        switch (check){
                            case 1:
                                //go to the customer panel
                            case 2:
                                //go to the seller panel
                            case 0:
                                JOptionPane.showMessageDialog(frame.getContentPane(), "Password is wrong!");
                                break;
                            case -1:
                                JOptionPane.showMessageDialog(frame.getContentPane(), "Account not made yet!");
                        }
                    } catch (IOException a) {
                        a.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog(null, "Enter the IP you want to connect to:", "Calender System", JOptionPane.QUESTION_MESSAGE);
        try {
            Socket socket = new Socket(host,5555);
            JOptionPane.showMessageDialog(null, "Client connected");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            SwingUtilities.invokeLater(new Client());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
