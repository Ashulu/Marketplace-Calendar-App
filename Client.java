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
    private JFrame frame;
    private JTextField username;
    private JTextField password;

    private JButton enter;
    private JButton createAccount;
    private int choice = -1;
    private JButton confirm = new JButton("Confirm");

    public Client() {
        this.frame = new JFrame("Calendar");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }

    public void run() {
        Socket socket;
        BufferedReader br;
        PrintWriter pw;
        try {
            //this is Ian's host and portname - may need to modify?
            socket = new Socket("localhost", 4242);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection unsuccessful.",
                    "Search Client", JOptionPane.ERROR_MESSAGE);
            return;
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new CardLayout());
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();

        String[] selOp = new String[]{
                "View Approved Appointments", "Appointment Requests",
                "Create Store", "Create Calendar",
                "Edit Calendar", "Delete Calendar",
                "Show Statistics", "Import Calendar",
                "Logout", "Quit"
        };
        String[] selPans = new String[]{
                "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9"
        };

        //whatever option specific buttons and the like go into their
        //own slot here:
        //----------------------------------------------------------//
        JPanel s0 = createCardPanel(selOp[0]);

        String[] options0 = new String[0];
        JComboBox<String> storeOptions0 = new JComboBox<>(options0);
        JButton create0 = new JButton("Select");
        create0.addActionListener(e -> {
            String command = String.format("SELECT * FROM calendars WHERE storeName = '%s';",
                    storeOptions0.getSelectedItem());
            pw.write(command);
        });

        JTextArea appointments0 = null;
        try {
            //appointment information goes here
            appointments0 = new JTextArea(br.readLine());
            appointments0.setEditable(false);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        appointments0.setLineWrap(true);
        JScrollPane scroll0 = new JScrollPane(appointments0);


        //----------------------------------------------------------//
        JPanel s1 = createCardPanel(selOp[1]);

        //----------------------------------------------------------//
        JPanel s2 = createCardPanel(selOp[2]);

        JTextField storeName = new JTextField("Example Name", 10);
        JButton create2 = new JButton("Create");
        create2.addActionListener(e -> {
            String command = String.format("INSERT INTO stores (sellerEmail, storeName) VALUES " +
                    "('%s', '%s');", storeName.getText(), username.getText());
            //
            //how do we handle duplicates??
            //i don't know how
            //
            pw.write(command);
        });
        s2.add(storeName);
        s2.add(create2);

        //----------------------------------------------------------//
        JPanel s3 = createCardPanel(selOp[3]);
        //just a temp options array - will get replaced
        String[] options3 = new String[3];
        JComboBox<String> storeOptions3 = new JComboBox<>(options3);
        JButton create3 = new JButton("Create");
        JTextField calendarDescription = new JTextField(40);
        create3.addActionListener(e -> {
            String command = String.format("INSERT INTO calendars (storeName, calendarName," +
                            " calendarDescription) VALUES ('%s', '%s', '%s');", storeName.getText(),
                            username.getText(), calendarDescription.getText());
            //
            //how do we handle duplicates??
            //i don't know how
            //
            pw.write(command);
        });
        s3.add(storeOptions3);
        s3.add(create3);
        s3.add(calendarDescription);

        //----------------------------------------------------------//
        JPanel s4 = createCardPanel(selOp[4]);

        //----------------------------------------------------------//
        JPanel s5 = createCardPanel(selOp[5]);
        String[] options5 = new String[5];
        JComboBox<String> storeOptions5 = new JComboBox<>(options5);
        JButton create5 = new JButton("Select");
        create5.addActionListener(e -> {
            String command = String.format("SELECT * FROM calendars WHERE storeName = '%s';",
                    storeOptions5.getSelectedItem());
            pw.write(command);
        });
        JComboBox<String> calendarOptions5 = new JComboBox<>(options5);
        JButton delete5 = new JButton("Delete");
        ActionListener actionListener5 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String calendar = (String) calendarOptions5.getSelectedItem();
                String command = String.format("DELETE FROM calendars WHERE calendarName = '%s';\n" +
                                "DELETE FROM windows WHERE calendarName = '%s';\n" +
                                "DELETE FROM appointments WHERE calendarName = '%s';\n",
                                calendar, calendar, calendar);
                pw.write(command);
            }
        };
        delete5.addActionListener(actionListener5);
        //----------------------------------------------------------//
        JPanel s6 = createCardPanel(selOp[6]);
        //insert statistics code here

        //----------------------------------------------------------//
        JPanel s7 = createCardPanel(selOp[7]);

        JTextField calendar7 = new JTextField("File Path:");
        JButton import7 = new JButton("Import");
        ActionListener actionListener7 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //i'm not sure what to send so here you go
                pw.write(calendar7.getText());
            }
        };
        import7.addActionListener(actionListener7);
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


        JPanel selSide = createCardPanel("Calendar: Seller");
        //----------------------------------------------------------//
        selSide.add(confirm);
        //add this button to every single card - it's going to be
        //what sends the request

        //----------------------------------------------------------//
        JComboBox<String> sellerOptions = new JComboBox<>(selOp);
        JLabel resultLabel = new JLabel("");
        ActionListener selListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) sellerOptions.getSelectedItem();
                choice = Arrays.asList(selOp).indexOf(selectedOption);

                //SEND REQUEST HERE
                while (true) {
                    switch (choice) {
                        case 0 -> {
                            //view approved appointments
                            cardLayout.show(mainPanel, "s0");
                            pw.write("SELECT storeName FROM calendars;");
                        }
                        case 1 -> {
                            //appointment requests
                            cardLayout.show(mainPanel, "s1");

                        }
                        case 2 -> {
                            //create store
                            cardLayout.show(mainPanel, "s2");

                        }
                        case 3 -> {
                            //create calendar
                            cardLayout.show(mainPanel, "s3");
                            pw.write("SELECT storeName FROM calendars;");
                            try {
                                storeOptions3.setModel(new DefaultComboBoxModel<>(br.readLine().split(",")));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        }
                        case 4 -> {
                            //edit calendar
                            cardLayout.show(mainPanel, "s4");
                        }
                        case 5 -> {
                            //delete calendar
                            cardLayout.show(mainPanel, "s5");
                            pw.write("SELECT storeName FROM calendars;");
                            try {
                                storeOptions5.setModel(new DefaultComboBoxModel<>(br.readLine().split(",")));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        }
                        case 6 -> {
                            //show statistics
                            //i forgot what statistics are supposed to be shown
                            cardLayout.show(mainPanel, "s6");

                        }
                        case 7 -> {
                            //import calendar
                            cardLayout.show(mainPanel, "s7");

                        }
                        case 8 -> {
                            //logout
                            //or actually let's bring this back to login (but ill do that later)
                            cardLayout.show(mainPanel, "s8");

                        }
                        case 9 -> {
                            //quit
                            pw.write("QUIT");
                            //idk it won't let me close unless i have the try/catch
                            try {
                                br.close();
                                pw.close();
                                return;
                            } catch (Exception ex) {
                                return;
                            }
                        }
                    }

                    //WAIT FOR REPLY THEN
                    //cardLayout.show(THE PANEL SELECTED HERE)
                    resultLabel.setText("Selected Option: " + selectedOption);
                    cardLayout.show(mainPanel, selPans[choice]);
                }
            }
        };

        confirm.addActionListener(selListener);
        selSide.add(sellerOptions);
        selSide.add(resultLabel);
        mainPanel.add(selSide);
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

    private String[] s3(BufferedReader br, PrintWriter pw) {
        try {
            return br.readLine().split(",");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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
