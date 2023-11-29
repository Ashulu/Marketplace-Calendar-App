import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class Client extends JComponent implements Runnable {
    public Client() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }

    public void run() {
        JFrame frame = new JFrame("Calendar");
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

        //TODO: GUI for login and create account
        JPanel buttons = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.VERTICAL;
        JButton login = new JButton("Login");
        JButton createAccount = new JButton("Create Account");
        buttons.add(login, gbc);
        buttons.add(createAccount, gbc);

        frame.add(buttons);

        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean cont = false;
                do {
                    //TODO: user enters login info -- if info is correct, break, else continue
                } while (cont);
                frame.remove(buttons);
                //TODO: GUI for dropdown options based on customer or seller
                String[] choices = { "CHOICE 1", "CHOICE 2", "CHOICE 3", "CHOICE 4",
                        "CHOICE 5", "CHOICE 6" };

                JComboBox<String> cb = new JComboBox<String>(choices);
                frame.add(cb);
            }
        });
        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean cont = false;
                do {
                    //TODO: user enters account info -- if valid, break, else continue
                } while (cont);
                frame.remove(buttons);
                //TODO: GUI for dropdown options based on customer or seller
                String[] choices = { "CHOICE 1", "CHOICE 2", "CHOICE 3", "CHOICE 4",
                        "CHOICE 5", "CHOICE 6" };

                JComboBox<String> cb = new JComboBox<String>(choices);
                frame.add(cb);
            }
        });

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
