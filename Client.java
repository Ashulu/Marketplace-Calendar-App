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
                            String command = String.format("SELECT * FROM appointments WHERE sellerEmail = '%s",
                                    username.getText());
                            pw.write(command);

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

    //0 "View Approved Appointments"
    private void s0(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("s0");
        pw.flush();
        Panel panel = new Panel();
        JTextArea appointments = new JTextArea("",3, 20);
        String approved = br.readLine();
        panel.add(appointments);
        appointments.setText(approved);
    }
    //1 "Appointment Requests"
    private void s1(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("s1");
        pw.flush();
        Panel panel = new Panel();
        //i forgot if we're sending strings or objects and if so, when/where for which method :(
        String[] requests = br.readLine().split(",");
        JComboBox<String> appointments = new JComboBox<String>(requests);
        JButton confirm = new JButton("Confirm");
        //error handling for appointment confirm/delete bouncing not implemented
        //because I want to delete things from the array and refresh the
        //dropdown as we go along but i could end up deleting something that
        //didn't actually get processed
        confirm.addActionListener(e -> {
            String command = String.format("confirm,%s",(String)appointments.getSelectedItem());
            pw.write(command);
            pw.flush();
        });
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            String command = String.format("delete,%s",(String)appointments.getSelectedItem());
            pw.write(command);
            pw.flush();
        });
        panel.add(appointments);
        panel.add(confirm);
        panel.add(delete);
    }

    //2 "Create Store"
    private void s2(BufferedReader br, PrintWriter pw) throws IOException {
        JPanel panel = new JPanel();
        JTextField storeName = new JTextField("Example Name", 10);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton create = new JButton("Create");
        create.addActionListener(e -> {
            String command = String.format("s2,%s",storeName.getText());
            pw.write(command);
            pw.flush();
        });
        panel.add(storeName);
        panel.add(create);
        panel.add(result);
        int response = br.read();
        if (response == 0) {
            result.setText("Unable to create store.");
        } else {
            result.setText("Creation successful!");
        }
        result.setVisible(true);
    }

    //3 "Create Calendar"
    private void s3(BufferedReader br, PrintWriter pw) throws IOException {
        JPanel panel = new JPanel();
        //creation of calendar portion
        JTextField storeName = new JTextField("Example Store", 10);
        JTextField calendarName = new JTextField("Example Calendar", 10);
        JTextField calendarDescription = new JTextField("Example Description", 20);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton create = new JButton("Create");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = String.format("s2,%s,%s,%s",storeName.getText(),calendarName.getText(),
                        calendarDescription.getText());
                pw.write(command);
                pw.flush();
            }
        };
        create.addActionListener(actionListener);
        panel.add(storeName);
        panel.add(create);
        panel.add(result);
        //creation of windows section
        JTextArea appointments = new JTextArea("start,end,maxCapacity\nstart,end,maxCapacity\n...",
                3, 20);
        appointments.setVisible(false);
        panel.add(appointments);

        int response = br.read();
        if (response == 0) {
            result.setText("Unable to create calendar.");
        } else {
            result.setText("Creation successful!");
            appointments.setVisible(true);
        }
        create.removeActionListener(actionListener);
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = appointments.getText();
                command = command.strip();
                command = command.replace("\n",",");
                String[] check = command.split(",");
                boolean vFormat = true;
                for (int i = 0; i < check.length; i++) {
                    switch (i % 3) {
                        case 1:
                            if (Integer.parseInt(check[i]) < Integer.parseInt(check[i - 1])) {
                                vFormat = false;
                            }
                        case 0:
                            if (check[i].length() != 4) {
                                vFormat = false;
                            }
                    }
                }
                if (command.replace(",","").matches("\\d+")
                        && check.length % 3 == 0 && vFormat) {
                    pw.write(command);
                    pw.flush();
                } else {
                    result.setText("Invalid format.");
                }
            }
        };
        create.addActionListener(actionListener);
        result.setVisible(true);
    }

    //4 "Edit Calendar"
    //i need to check for reply of successful or not?
    private void s4(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("s4");
        JPanel panel = new JPanel();
        String[] options = { "Edit Name", "Edit Description", "Add Windows", "Delete Windows" };
        JComboBox<String> edit = new JComboBox<String>(options);
        JTextField result = new JTextField();
        result.setVisible(false);
        panel.add(result);
        panel.add(edit);
        switch (edit.getSelectedIndex()) {
            case 0 -> {
                panel = new JPanel();
                JButton submit = new JButton("Edit");
                panel.add(edit);
                panel.add(result);
                panel.add(submit);
                JTextField oldName = new JTextField("Old Name");
                JTextField newName = new JTextField("New Name");
                panel.add(oldName);
                panel.add(newName);
                submit.addActionListener(e -> {
                    if (oldName.getText().isEmpty() || newName.getText().isEmpty()) {
                        result.setText("Invalid input.");
                        result.setVisible(true);
                    } else {
                        String command = String.format("editName,%s,%s",oldName.getText(),newName.getText());
                        pw.write(command);
                        pw.flush();
                    }
                });
            }
            case 1 -> {
                panel = new JPanel();
                JButton submit = new JButton("Edit");
                panel.add(edit);
                panel.add(result);
                panel.add(submit);
                JTextField calendarName = new JTextField("Calendar Name");
                JTextField calendarDescription = new JTextField("New Description");
                panel.add(calendarName);
                panel.add(calendarDescription);
                submit.addActionListener(e -> {
                    if (calendarName.getText().isEmpty() || calendarDescription.getText().isEmpty()) {
                        result.setText("Invalid input.");
                        result.setVisible(true);
                    } else {
                        String command = String.format("editDescription,%s,%s",calendarName.getText(),
                                calendarDescription.getText());
                        pw.write(command);
                        pw.flush();
                    }
                });
            }
            case 2 -> {
                panel = new JPanel();
                JButton submit = new JButton("Add");
                panel.add(edit);
                panel.add(result);
                panel.add(submit);
                JTextField storeName = new JTextField("Calendar Name");
                JTextField calendarName = new JTextField("New Description");
                JTextField window = new JTextField("start,end,maxCapacity");
                panel.add(storeName);
                panel.add(calendarName);
                panel.add(window);
                submit.addActionListener(e -> {
                    String[] check = window.getText().split(",");
                    boolean vFormat = true;
                    for (int i = 0; i < check.length; i++) {
                        switch (i % 3) {
                            case 1:
                                if (Integer.parseInt(check[i]) < Integer.parseInt(check[i - 1])) {
                                    vFormat = false;
                                }
                            case 0:
                                if (check[i].length() != 4) {
                                    vFormat = false;
                                }
                        }
                    }
                    if (storeName.getText().isEmpty() || calendarName.getText().isEmpty() ||
                            window.getText().replace(",","").matches("\\d+")
                                    && check.length == 3 && vFormat) {
                        result.setText("Invalid input.");
                        result.setVisible(true);
                    } else {
                        String command = String.format("addWindow,%s,%s,%s",storeName.getText(),
                                calendarName.getText(),window.getText());
                        pw.write(command);
                        pw.flush();
                    }
                });
            }
            case 3 -> {
                panel = new JPanel();
                JButton submit = new JButton("Delete");
                panel.add(edit);
                panel.add(result);
                panel.add(submit);
                JTextField storeName = new JTextField("Store Name");
                JTextField calendarName = new JTextField("Calendar Name");
                JTextField window = new JTextField("start,end");
                panel.add(storeName);
                panel.add(calendarName);
                panel.add(window);
                submit.addActionListener(e -> {
                    String[] check = window.getText().split(",");
                    boolean vFormat = true;
                    if (Integer.parseInt(check[1]) < Integer.parseInt(check[0])) {
                        vFormat = false;
                    }
                    if (check[0].length() != 4 || check[1].length() != 4) {
                        vFormat = false;
                    }
                    if (storeName.getText().isEmpty() || calendarName.getText().isEmpty() ||
                            window.getText().replace(",","").matches("\\d+")
                                    && vFormat) {
                        result.setText("Invalid input.");
                        result.setVisible(true);
                    } else {
                        String command = String.format("addWindow,%s,%s,%s",storeName.getText(),
                                calendarName.getText(),window.getText());
                        pw.write(command);
                        pw.flush();
                    }
                });
            }
        }
    }

    //5 "Delete Calendar"
    private void s5(BufferedReader br, PrintWriter pw) throws IOException {
        JPanel panel = new JPanel();
        JTextField storeName = new JTextField("Example Store", 10);
        JTextField calendarName = new JTextField("Example Calendar", 10);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            String command = String.format("s5,%s,%s",storeName.getText(),calendarName.getText());
            pw.write(command);
            pw.flush();
        });
        panel.add(storeName);
        panel.add(calendarName);
        panel.add(delete);
        panel.add(result);
        int response = br.read();
        if (response == 0) {
            result.setText("Unable to delete calendar.");
        } else {
            result.setText("Deletion successful!");
        }
        result.setVisible(true);
    }

    //6 "Show Statistics"
    //i'll fix this tmrw after clarification
    private void s6() {

    }

    //7 "Import Calendar"
    private void s7(BufferedReader br, PrintWriter pw) throws IOException {
        JPanel panel = new JPanel();
        JTextField storeName = new JTextField("Example Store", 10);
        JTextField fileName = new JTextField("File Name", 10);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton imp = new JButton("Import");
        imp.addActionListener(e -> {
            String command = String.format("s7,%s,%s",storeName.getText(),fileName.getText());
            pw.write(command);
            pw.flush();
        });
        panel.add(storeName);
        panel.add(fileName);
        panel.add(imp);
        panel.add(result);
        int response = br.read();
        if (response == 0) {
            result.setText("Unable to import calendar.");
        } else {
            result.setText("Import successful!");
        }
        result.setVisible(true);
    }

    public static int exit() {
        //send exit message here
        return JFrame.DISPOSE_ON_CLOSE;
    }


}
