import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.nio.Buffer;

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
    JPanel sellerMain;
    JPanel sellerSub;
    JPanel customerMain;
    JPanel customerSub;
    JButton login;
    JButton createAccount;
    JPanel jPanel;
    JFrame frame;
    BufferedReader reader;
    PrintWriter writer;


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
//                        writer.println("login," + userField.getText() + "," + passField);
//                        writer.flush();
//
//                        int check = Integer.parseInt(reader.readLine());
                        int check = 2;
                        switch (check){
                            case 1 -> {
                                //go to the customer panel
                                customerMain = new JPanel();
                                customerMain.setBounds(0, 0, 30, frame.getHeight());
                                customerMain.setLayout(new BoxLayout(customerMain, BoxLayout.Y_AXIS));
                                customer(reader, writer);
                                frame.remove(loginPanel);
                                frame.add(customerMain);
                                frame.pack();
                            }
                            case 2 -> {
                                //go to the seller panel
                                sellerMain = new JPanel();
                                sellerMain.setBounds(0, 0, 30, frame.getHeight());
                                sellerMain.setLayout(new BoxLayout(sellerMain, BoxLayout.Y_AXIS));
                                seller(reader, writer);
                                frame.remove(loginPanel);
                                frame.add(sellerMain, BorderLayout.CENTER);
                                frame.pack();
                            }
                            case 0 -> JOptionPane.showMessageDialog(frame.getContentPane(), "Password is wrong!");
                            case -1 -> JOptionPane.showMessageDialog(frame.getContentPane(), "Account not made yet!");
                        }
                    } catch (IOException a) {
                        System.out.println("This is the reason");
                        a.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        /*String host = JOptionPane.showInputDialog(null, "Enter the IP you want to connect to:", "Calender System",
                JOptionPane.QUESTION_MESSAGE);
        try {
            Socket socket = new Socket(host,5555);
            JOptionPane.showMessageDialog(null, "Client connected");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            SwingUtilities.invokeLater(new Client());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        SwingUtilities.invokeLater(new Client());

    }

    // ------------------------
    // START OF CUSTOMER GUI
    // ------------------------

    private void customer(BufferedReader br, PrintWriter pw) throws IOException {
        JLabel welcome = new JLabel("Welcome Customer! \nPlease Select what you would like to do:");
        customerMain.add(welcome);

        String[] customerOp = new String[]{
                // TODO: Add view calendars option
                "Request an appointment",
                "Cancel an appointment",
                "View your approved appointments",
                "Show store statistics",
                "Logout",
                "Quit"
        };

        JComboBox<String> customerOptions = new JComboBox<>(customerOp);
        customerMain.add(customerOptions);

        JButton choice = new JButton("Select");
        customerMain.add(choice);

        choice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    customerSub = new JPanel();
                    customerSub.setBounds(0, 0, 30, frame.getHeight());
                    customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                    switch (customerOptions.getSelectedIndex()){
                        case 0 -> {
                            //c0
                        }
                        case 1 -> {
                            //c1
                        }
                        case 2 -> {
                            //c2
                        }
                        case 3 -> {
                            //c3
                        }
                        case 4 -> {
                            frame.remove(customerMain);
                            frame.add(loginPanel);
                            frame.pack();
                            return;
                        }
                        case 5 -> {
                            frame.remove(customerMain);
                            writer.write("QUIT");
                            writer.flush();
                        }
                    }
                    frame.remove(customerMain);
                    frame.add(customerSub);
                    frame.pack();
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        });
    }


    private void c0(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("viewCalendars");
        pw.flush();
        customerBack(customerSub);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(customerSub);
                customerSub = new JPanel();
                customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                c0(br,pw);
                frame.add(customerSub, BorderLayout.CENTER);
                frame.pack();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        customerSub.add(refresh);

        //get all store names from server and print out in dropdown to user
        //once user selects, get all calendars from that specific store and print out
        // user should be able to select based on a dropdown

    }

    private void c1(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("requestAppointment");
        pw.flush();
        jPanel = new JPanel();
        frame.add(jPanel);
        customerBack(jPanel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(customerSub);
                customerSub = new JPanel();
                customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                c0(br,pw);
                frame.add(customerSub, BorderLayout.CENTER);
                frame.pack();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        customerSub.add(refresh);

        //creation of appointment
        //TODO: make storeName, calendarName, and calendarDescription into dropdowns that the user can select
        JTextField storeName = new JTextField("Example Store", 10);
        JTextField calendarName = new JTextField("Example Calendar", 10);
        JTextField calendarDescription = new JTextField("Example Calendar", 10);
        JTextField startTime = new JTextField("Example Start Time", 10);
        JTextField endTime = new JTextField("Example End Time", 20);
        JTextField booking = new JTextField("Example Bookng", 20);

        JTextField result = new JTextField();
        result.setVisible(false);
        JButton create = new JButton("Create");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = String.format("c1,%s,%s,%s",storeName.getText(),calendarName.getText(),
                        calendarDescription.getText(), startTime.getText(), endTime.getText(), booking.getText());
                pw.write(command);
                pw.flush();
            }
        };
        create.addActionListener(actionListener);
        jPanel.add(storeName);
        jPanel.add(create);
        jPanel.add(result);

        int response = br.read();
        if (response == 0) {
            result.setText("Unable to create calendar.");
        } else {
            result.setText("Creation successful!");
        }

        result.setVisible(true);
        frame.pack();


    }


    // -------------------
    // START OF SELLER GUI
    // -------------------

    //Seller landing page
    private void seller(BufferedReader br, PrintWriter pw) throws IOException {
        JLabel welcome = new JLabel("Hello Seller! \nPlease Select what you would like to do");
        sellerMain.add(welcome);

        String[] selOp = new String[] {
                "View Approved Appointments", "Appointment Requests",
                "Create Store", "Create Calendar",
                "Edit Calendar", "Delete Calendar",
                "Show Statistics", "Import Calendar",
                "Logout", "Quit"
        };
//        frame.add(jPanel);
        JComboBox<String> sellerOptions = new JComboBox<>(selOp);
        sellerMain.add(sellerOptions);

        JButton choice = new JButton("Select");
        sellerMain.add(choice);

        choice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sellerSub = new JPanel();
                    sellerSub.setBounds(0, 0, 30, frame.getHeight());
                    sellerSub.setLayout(new BoxLayout(sellerSub, BoxLayout.Y_AXIS));
                    switch (sellerOptions.getSelectedIndex()) {
                        case 0 -> {

                            s0(br, pw);

                        }
                        case 1 -> {

                            s1(br,pw);
                        }
                        case 2 -> {

                            s2(br,pw);

                        }
                        case 3 -> {

                            s3(br,pw);

                        }
                        case 4 -> {

                            s4(br,pw);

                        }
                        case 5 -> {

                            s5(br,pw);

                        }
                        case 6 -> {

                            s6(br,pw);

                        }
                        case 7 -> {

                            s7(br,pw);

                        }
                        //return to login
                        case 8 -> {
                            frame.remove(sellerMain);
                            frame.add(loginPanel);
                            frame.pack();
                            return;
                        }
                        //quit
                        case 9 -> {
                            frame.remove(sellerMain);
                            writer.write("exit");
                            writer.flush();
                        }
                    }
                    frame.remove(sellerMain);
                    frame.add(sellerSub, BorderLayout.CENTER);
                    frame.pack();
                } catch(Exception a) {
                    a.printStackTrace();
                }
            }
        });
    }
    //0 "View Approved Appointments"
    private void s0(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("viewApproved");
        pw.flush();
        sellerBack(sellerSub);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                sellerSub = new JPanel();
                sellerSub.setLayout(new BoxLayout(sellerSub, BoxLayout.Y_AXIS));
                s0(br,pw);
                frame.add(sellerSub, BorderLayout.CENTER);
                frame.pack();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        JTextArea appointments = new JTextArea("",3, 20);

        //add processing a little later
        String approved = br.readLine();

        sellerSub.add(appointments);
        appointments.setText(approved);
    }
    //1 "Appointment Requests"
    private void s1(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("approveRequest");
        pw.flush();

        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                sellerSub = new JPanel();
                sellerSub.setLayout(new BoxLayout(sellerSub, BoxLayout.Y_AXIS));
                s1(br,pw);
                frame.add(sellerSub, BorderLayout.CENTER);
                frame.pack();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        String temp = br.readLine();
        String[] requests = temp.substring(0,temp.length() - 1).split("],\\[");
        JComboBox<String> appointments = new JComboBox<String>(requests);
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(e -> {
            String[] temp2 = ((String)appointments.getSelectedItem()).split(", ");
            String command = String.format("confirm,%s,%s,%s,%s,%s",
                    temp2[0],temp2[1],temp2[2],temp2[3],temp2[5]);
            pw.write(command);
            pw.flush();
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Operation failed.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Operation successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            String[] temp2 = ((String)appointments.getSelectedItem()).split(", ");
            String command = String.format("delete,%s,%s,%s,%s,%s",
                    temp2[0],temp2[1],temp2[2],temp2[3],temp2[5]);
            pw.write(command);
            pw.flush();
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Operation failed.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Operation successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });
        sellerSub.add(appointments);
        sellerSub.add(confirm);
        sellerSub.add(delete);
    }

    //2 "Create Store"
    private void s2(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("createStore");
        pw.flush();
        frame.add(sellerSub);
        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s0(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        JTextField storeName = new JTextField("Example Name", 10);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton create = new JButton("Create");
        create.addActionListener(e -> {
            String command = String.format("s2,%s",storeName.getText());
            pw.write(command);
            pw.flush();
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Store creation failed.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Store creation successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });
        sellerSub.add(storeName);
        sellerSub.add(create);
        sellerSub.add(result);
        result.setVisible(true);
        frame.pack();
    }

    //3 "Create Calendar"
    private void s3(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("createCalendar");
        pw.flush();
        frame.add(sellerSub);
        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s3(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
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
        sellerSub.add(storeName);
        sellerSub.add(create);
        sellerSub.add(result);
        //creation of windows section
        JTextArea appointments = new JTextArea("start,end,maxCapacity\nstart,end,maxCapacity\n...",
                3, 20);
        appointments.setVisible(false);
        sellerSub.add(appointments);

        int response = br.read();
        if (response == 0) {
            result.setText("Calendar creation failed.");
        } else {
            result.setText("Calendar creation successful!");
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
                    int response;
                    try {
                        response = Integer.parseInt(br.readLine());
                    } catch (IOException exc) {
                        throw new RuntimeException(exc);
                    }
                    if (response == 0) {
                        JOptionPane.showMessageDialog(null, "Operation failed.",
                                "Seller Client", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Operation successful!",
                                "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                    }
                    frame.remove(sellerSub);
                    frame.add(sellerMain, BorderLayout.CENTER);
                    frame.pack();
                } else {
                    result.setText("Invalid format.");
                }
            }
        };
        create.addActionListener(actionListener);
        result.setVisible(true);
        frame.pack();
    }

    //4 "Edit Calendar"
    //i need to check for reply of successful or not?
    private void s4(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("editCalendar");
        pw.flush();
        frame.add(sellerSub);
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(sellerSub);
                frame.add(sellerMain, BorderLayout.CENTER);
                frame.pack();
            }
        });
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s4(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        String[] options = { "Edit Name", "Edit Description", "Add Windows", "Delete Windows" };
        JComboBox<String> edit = new JComboBox<String>(options);
        JTextField result = new JTextField();
        result.setVisible(false);
        sellerSub.add(result);
        sellerSub.add(edit);
        JButton select = new JButton("Select");
        select.addActionListener(ex -> {
            sellerSub.remove(back);
            switch (edit.getSelectedIndex()) {
                case 0 -> {
                    pw.write("editCalendarName");
                    JButton submit = new JButton("Edit");
                    sellerSub.add(edit);
                    sellerSub.add(result);
                    sellerSub.add(submit);
                    JTextField oldName = new JTextField("Old Name");
                    JTextField newName = new JTextField("New Name");
                    sellerSub.add(oldName);
                    sellerSub.add(newName);
                    submit.addActionListener(e -> {
                        if (oldName.getText().isEmpty() || newName.getText().isEmpty()) {
                            result.setText("Invalid input.");
                            result.setVisible(true);
                        } else {
                            String command = String.format("editName,%s,%s",oldName.getText(),newName.getText());
                            pw.write(command);
                            pw.flush();
                            int response;
                            try {
                                response = Integer.parseInt(br.readLine());
                            } catch (IOException exc) {
                                throw new RuntimeException(exc);
                            }
                            if (response == 0) {
                                JOptionPane.showMessageDialog(null, "Operation failed.",
                                        "Seller Client", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Operation successful!",
                                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                            }
                            frame.remove(sellerSub);
                            frame.add(sellerMain, BorderLayout.CENTER);
                            frame.pack();
                        }
                    });
                }
                case 1 -> {
                    pw.write("editCalendarName");
                    frame.add(sellerSub);
                    JButton submit = new JButton("Edit");
                    sellerSub.add(edit);
                    sellerSub.add(result);
                    sellerSub.add(submit);
                    JTextField calendarName = new JTextField("Calendar Name");
                    JTextField calendarDescription = new JTextField("New Description");
                    sellerSub.add(calendarName);
                    sellerSub.add(calendarDescription);
                    submit.addActionListener(e -> {
                        if (calendarName.getText().isEmpty() || calendarDescription.getText().isEmpty()) {
                            result.setText("Invalid input.");
                            result.setVisible(true);
                        } else {
                            String command = String.format("editDescription,%s,%s",calendarName.getText(),
                                    calendarDescription.getText());
                            pw.write(command);
                            pw.flush();
                            int response;
                            try {
                                response = Integer.parseInt(br.readLine());
                            } catch (IOException exc) {
                                throw new RuntimeException(exc);
                            }
                            if (response == 0) {
                                JOptionPane.showMessageDialog(null, "Operation failed.",
                                        "Seller Client", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Operation successful!",
                                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                            }
                            frame.remove(sellerSub);
                            frame.add(sellerMain, BorderLayout.CENTER);
                            frame.pack();
                        }
                    });
                }
                case 2 -> {
                    pw.write("editCalendarAddWindow");
                    frame.add(sellerSub);
                    JButton submit = new JButton("Add");
                    sellerSub.add(edit);
                    sellerSub.add(result);
                    sellerSub.add(submit);
                    JTextField storeName = new JTextField("Calendar Name");
                    JTextField calendarName = new JTextField("New Description");
                    JTextField window = new JTextField("start,end,maxCapacity");
                    sellerSub.add(storeName);
                    sellerSub.add(calendarName);
                    sellerSub.add(window);
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
                            int response;
                            try {
                                response = Integer.parseInt(br.readLine());
                            } catch (IOException exc) {
                                throw new RuntimeException(exc);
                            }
                            if (response == 0) {
                                JOptionPane.showMessageDialog(null, "Operation failed.",
                                        "Seller Client", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Operation successful!",
                                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                            }
                            frame.remove(sellerSub);
                            frame.add(sellerMain, BorderLayout.CENTER);
                            frame.pack();
                        }
                    });
                    frame.pack();
                }
                case 3 -> {
                    pw.write("editCalendarRemoveWindow");
                    frame.add(sellerSub);
                    JButton submit = new JButton("Delete");
                    sellerSub.add(edit);
                    sellerSub.add(result);
                    sellerSub.add(submit);
                    JTextField storeName = new JTextField("Store Name");
                    JTextField calendarName = new JTextField("Calendar Name");
                    JTextField window = new JTextField("start,end");
                    sellerSub.add(storeName);
                    sellerSub.add(calendarName);
                    sellerSub.add(window);
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
                                window.getText().replace(",", "").matches("\\d+")
                                        && vFormat) {
                            result.setText("Invalid input.");
                            result.setVisible(true);
                        } else {
                            String command = String.format("addWindow,%s,%s,%s", storeName.getText(),
                                    calendarName.getText(), window.getText());
                            pw.write(command);
                            pw.flush();
                            int response;
                            try {
                                response = Integer.parseInt(br.readLine());
                            } catch (IOException exc) {
                                throw new RuntimeException(exc);
                            }
                            if (response == 0) {
                                JOptionPane.showMessageDialog(null, "Operation failed.",
                                        "Seller Client", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Operation successful!",
                                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                            }
                            result.setVisible(true);
                            frame.remove(sellerSub);
                            frame.add(sellerMain, BorderLayout.CENTER);
                            frame.pack();
                        }
                    });
                    frame.pack();
                }
            }
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Edit failed.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Edit successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            result.setVisible(true);
        });
    }

    //5 "Delete Calendar"
    private void s5(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("deleteCalendar");
        pw.flush();

        frame.add(sellerSub);
        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s5(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        JTextField storeName = new JTextField("Example Store", 10);
        JTextField calendarName = new JTextField("Example Calendar", 10);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            String command = String.format("s5,%s,%s",storeName.getText(),calendarName.getText());
            pw.write(command);
            pw.flush();
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });
        sellerSub.add(storeName);
        sellerSub.add(calendarName);
        sellerSub.add(delete);
        sellerSub.add(result);
        int response;
        try {
            response = Integer.parseInt(br.readLine());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if (response == 0) {
            JOptionPane.showMessageDialog(null, "Failed to delete.",
                    "Seller Client", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Deletion successful!",
                    "Seller Client", JOptionPane.INFORMATION_MESSAGE);
        }
        result.setVisible(true);
        frame.pack();
    }

    //6 "Show Statistics"
    //i'll fix this tmrw after clarification
    private void s6(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("statisticsSeller");
        pw.flush();
        String[] stores = br.readLine().split(",");
        JComboBox<String> storeOptions = new JComboBox<String>(stores);
        sellerSub.add(storeOptions);
        frame.add(sellerSub);
        sellerBack(sellerSub);
        JTextArea result = new JTextArea(3,20);
        sellerSub.add(result);
        String[] sort = new String[] { "Most Popular Window",
                "Customer Appointments"};
        JComboBox<String> sortOptions = new JComboBox<String>(sort);
        sellerSub.add(sortOptions);
        JButton sorted = new JButton("Sort");
        sorted.addActionListener(e -> {
            switch (sortOptions.getSelectedIndex()) {
                case 0 -> {
                    try {
                        result.setText(br.readLine());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                case 1 -> {
                    //i'll handle formatting later
                    try {
                        result.setText(br.readLine());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        sellerSub.add(sorted);
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(e -> {
            pw.write((String)storeOptions.getSelectedItem());
        });
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s6(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        frame.pack();
    }

    //7 "Import Calendar"
    private void s7(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("importCalendar");
        pw.flush();

        frame.add(sellerSub);
        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s7(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        JTextField storeName = new JTextField("Example Store", 10);
        JTextField fileName = new JTextField("File Name", 10);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton imp = new JButton("Import");
        imp.addActionListener(e -> {
            String command = String.format("s7,%s,%s",storeName.getText(),fileName.getText());
            pw.write(command);
            pw.flush();
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Failed to import.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Import successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            result.setVisible(true);
        });
        sellerSub.add(storeName);
        sellerSub.add(fileName);
        sellerSub.add(imp);
        sellerSub.add(result);
        frame.pack();
    }
    private void sellerBack(JPanel panel) {
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            frame.remove(panel);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });
        panel.add(back);
    }

    private void customerBack(JPanel panel) {
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(panel);
                frame.add(customerMain, BorderLayout.CENTER);
                frame.pack();
            }
        });
    }

    // TODO: no choice for this in the customer options !! need to change
//    private void c0(BufferedReader br, PrintWriter pw) throws IOException {
//        pw.write("viewCalendars");
//        pw.flush();
//        customerBack(customerSub);
//
//        JButton refresh = new JButton("Refresh");
//        refresh.addActionListener(e -> {
//            try {
//                frame.remove(customerSub);
//                customerSub = new JPanel();
//                customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
//                c0(br,pw);
//                frame.add(customerSub, BorderLayout.CENTER);
//                frame.pack();
//
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        });
//        customerSub.add(refresh);
//
//        //get all store names from server and print out in dropdown to user
//        //once user selects, get all calendars from that specific store and print out
//        // user should be able to select based on a dropdown
//
//    }
//
//    private void c1(BufferedReader br, PrintWriter pw) throws IOException {
//        pw.write("requestAppointment");
//        pw.flush();
//        jPanel = new JPanel();
//        frame.add(jPanel);
//        customerBack(jPanel);
//        JButton refresh = new JButton("Refresh");
//        refresh.addActionListener(e -> {
//            try {
//                frame.remove(customerSub);
//                customerSub = new JPanel();
//                customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
//                c0(br,pw);
//                frame.add(customerSub, BorderLayout.CENTER);
//                frame.pack();
//
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        });
//        customerSub.add(refresh);
//
//        //creation of appointment
//        //TODO: make storeName, calendarName, and calendarDescription into dropdowns that the user can select
//        JTextField storeName = new JTextField("Example Store", 10);
//        JTextField calendarName = new JTextField("Example Calendar", 10);
//        JTextField calendarDescription = new JTextField("Example Calendar", 10);
//        JTextField startTime = new JTextField("Example Start Time", 10);
//        JTextField endTime = new JTextField("Example End Time", 20);
//        JTextField booking = new JTextField("Example Bookng", 20);
//
//        JTextField result = new JTextField();
//        result.setVisible(false);
//        JButton create = new JButton("Create");
//        ActionListener actionListener = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String command = String.format("c1,%s,%s,%s",storeName.getText(),calendarName.getText(),
//                        calendarDescription.getText(), startTime.getText(), endTime.getText(), booking.getText());
//                pw.write(command);
//                pw.flush();
//            }
//        };
//        create.addActionListener(actionListener);
//        jPanel.add(storeName);
//        jPanel.add(create);
//        jPanel.add(result);
//
//        int response = br.read();
//        if (response == 0) {
//            result.setText("Unable to create calendar.");
//        } else {
//            result.setText("Creation successful!");
//        }
//
//        result.setVisible(true);
//        frame.pack();
//
//
//    }



    public static int exit() {
        //send exit message here
        return JFrame.DISPOSE_ON_CLOSE;
    }
}
