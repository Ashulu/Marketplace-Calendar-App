import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JComponent implements Runnable {
    public Client() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }

    public void run() {
        JFrame frame = new JFrame("Calendar");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
    }


}
