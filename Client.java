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
//        reader = new BufferedReader(new InputStreamReader(System.in));
//        writer = new PrintWriter(new OutputStreamWriter(System.out));

        SwingUtilities.invokeLater(new Client());

    }

    // ------------------------
    // START OF CUSTOMER GUI
    // ------------------------

    private void customer(BufferedReader br, PrintWriter pw) throws IOException {
        JLabel welcome = new JLabel("Welcome Customer! \nPlease Select what you would like to do:");
        customerMain.add(welcome);

        String[] customerOp = new String[]{
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
                            frame.remove(jPanel);
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

//        String approved = br.readLine();
        String approved = "approved";
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
        //i forgot if we're sending strings or objects and if so, when/where for which method :(
//        String[] requests = br.readLine().split(",");
//        JComboBox<String> appointments = new JComboBox<String>(requests);
        JButton confirm = new JButton("Confirm");
        //error handling for appointment confirm/delete bouncing not implemented
        //because I want to delete things from the array and refresh the
        //dropdown as we go along but i could end up deleting something that
        //didn't actually get processed
        confirm.addActionListener(e -> {
//            String command = String.format("confirm,%s",(String)appointments.getSelectedItem());
//            pw.write(command);
//            pw.flush();
        });
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
//            String command = String.format("delete,%s",(String)appointments.getSelectedItem());
//            pw.write(command);
//            pw.flush();
        });
//        sellerSub.add(appointments);
        sellerSub.add(confirm);
        sellerSub.add(delete);

    }

    //2 "Create Store"
    private void s2(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("createStore");
        pw.flush();
        jPanel = new JPanel();
        frame.add(jPanel);
        sellerBack(jPanel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(jPanel);
                s0(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        jPanel.add(refresh);
        JTextField storeName = new JTextField("Example Name", 10);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton create = new JButton("Create");
        create.addActionListener(e -> {
            String command = String.format("s2,%s",storeName.getText());
            pw.write(command);
            pw.flush();
        });
        jPanel.add(storeName);
        jPanel.add(create);
        jPanel.add(result);
        int response = br.read();
        if (response == 0) {
            result.setText("Unable to create store.");
        } else {
            result.setText("Creation successful!");
        }
        result.setVisible(true);
        frame.pack();
    }

    //3 "Create Calendar"
    private void s3(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("createCalendar");
        pw.flush();
        jPanel = new JPanel();
        frame.add(jPanel);
        sellerBack(jPanel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(jPanel);
                s3(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        jPanel.add(refresh);
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
        jPanel.add(storeName);
        jPanel.add(create);
        jPanel.add(result);
        //creation of windows section
        JTextArea appointments = new JTextArea("start,end,maxCapacity\nstart,end,maxCapacity\n...",
                3, 20);
        appointments.setVisible(false);
        jPanel.add(appointments);

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
        frame.pack();
    }

    //4 "Edit Calendar"
    //i need to check for reply of successful or not?
    private void s4(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("editCalendar");
        pw.flush();
        jPanel = new JPanel();
        frame.add(jPanel);
        sellerBack(jPanel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(jPanel);
                s4(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        jPanel.add(refresh);
        String[] options = { "Edit Name", "Edit Description", "Add Windows", "Delete Windows" };
        JComboBox<String> edit = new JComboBox<String>(options);
        JTextField result = new JTextField();
        result.setVisible(false);
        jPanel.add(result);
        jPanel.add(edit);
        switch (edit.getSelectedIndex()) {
            case 0 -> {
                jPanel = new JPanel();
                JButton submit = new JButton("Edit");
                jPanel.add(edit);
                jPanel.add(result);
                jPanel.add(submit);
                JTextField oldName = new JTextField("Old Name");
                JTextField newName = new JTextField("New Name");
                jPanel.add(oldName);
                jPanel.add(newName);
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
                jPanel = new JPanel();
                frame.add(jPanel);
                JButton submit = new JButton("Edit");
                jPanel.add(edit);
                jPanel.add(result);
                jPanel.add(submit);
                JTextField calendarName = new JTextField("Calendar Name");
                JTextField calendarDescription = new JTextField("New Description");
                jPanel.add(calendarName);
                jPanel.add(calendarDescription);
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
                jPanel = new JPanel();
                frame.add(jPanel);
                JButton submit = new JButton("Add");
                jPanel.add(edit);
                jPanel.add(result);
                jPanel.add(submit);
                JTextField storeName = new JTextField("Calendar Name");
                JTextField calendarName = new JTextField("New Description");
                JTextField window = new JTextField("start,end,maxCapacity");
                jPanel.add(storeName);
                jPanel.add(calendarName);
                jPanel.add(window);
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
                frame.pack();
            }
            case 3 -> {
                jPanel = new JPanel();
                frame.add(jPanel);
                JButton submit = new JButton("Delete");
                jPanel.add(edit);
                jPanel.add(result);
                jPanel.add(submit);
                JTextField storeName = new JTextField("Store Name");
                JTextField calendarName = new JTextField("Calendar Name");
                JTextField window = new JTextField("start,end");
                jPanel.add(storeName);
                jPanel.add(calendarName);
                jPanel.add(window);
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
                frame.pack();
            }
        }
    }

    //5 "Delete Calendar"
    private void s5(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("deleteCalendar");
        pw.flush();
        jPanel = new JPanel();
        frame.add(jPanel);
        sellerBack(jPanel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(jPanel);
                s5(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        jPanel.add(refresh);
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
        jPanel.add(storeName);
        jPanel.add(calendarName);
        jPanel.add(delete);
        jPanel.add(result);
        int response = br.read();
        if (response == 0) {
            result.setText("Unable to delete calendar.");
        } else {
            result.setText("Deletion successful!");
        }
        result.setVisible(true);
        frame.pack();
    }

    //6 "Show Statistics"
    //i'll fix this tmrw after clarification
    private void s6(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("statisticsSeller");
        pw.flush();
        jPanel = new JPanel();
        /*
        i still don't know what goes here
        output: sellerEmail, storeName [customerName, numofApproved]
        most popular window sorted in number of customers

         */
        frame.add(jPanel);
        sellerBack(jPanel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(jPanel);
                s6(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        jPanel.add(refresh);
        frame.pack();
    }

    //7 "Import Calendar"
    private void s7(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("importCalendar");
        pw.flush();
        jPanel = new JPanel();
        frame.add(jPanel);
        sellerBack(jPanel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(jPanel);
                s7(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        jPanel.add(refresh);
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
        jPanel.add(storeName);
        jPanel.add(fileName);
        jPanel.add(imp);
        jPanel.add(result);
        int response = br.read();
        if (response == 0) {
            result.setText("Unable to import calendar.");
        } else {
            result.setText("Import successful!");
        }
        result.setVisible(true);
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
    public static int exit() {
        //send exit message here
        return JFrame.DISPOSE_ON_CLOSE;
    }
}
