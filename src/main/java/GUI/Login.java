package GUI;

import com.mongodb.client.MongoCollection;
import functions.Functions;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    private JPanel panel, secpanel,buttonpanel;
    private JLabel title,name,password,errorMessage;
    private JTextField username, userpassword;
    private JButton logButton, registerButton;
    public boolean loged=false;

    public Login(Functions functions) {
        panel=new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.white);
        panel.setBounds(440,0,426,720);

        title=new JLabel("Log in");
        title.setBounds(180,0,200,100);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title);

        secpanel=new JPanel();
        secpanel.setBounds(120,142,200,140);
        secpanel.setBackground(Color.RED);
        secpanel.setLayout(new GridLayout(6,1));
        panel.add(secpanel);

        name=new JLabel("Introduce username");
        secpanel.add(name);
        username=new JTextField();
        secpanel.add(username);

        password=new JLabel("Introduce password");
        secpanel.add(password);
        userpassword=new JTextField();
        secpanel.add(userpassword);

        errorMessage=new JLabel("Username or password incorrect");
        errorMessage.setVisible(false);
        secpanel.add(errorMessage);

        buttonpanel=new JPanel();
        secpanel.add(buttonpanel);

        logButton=new JButton("Log in");
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean id=functions.getUser(username.getText(), userpassword.getText());
                if(id.equals(false)){
                    errorMessage.setVisible(true);
                }
                else {
                    loged=true;
                }
            }
        });
        buttonpanel.add(logButton);
        registerButton=new JButton("Register");
        buttonpanel.add(registerButton);
    }

    public JPanel getPanel() {
        return panel;
    }

}
