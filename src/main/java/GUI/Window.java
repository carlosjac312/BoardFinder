package GUI;

import com.mongodb.client.MongoDatabase;
import functions.Functions;

import javax.swing.*;
import java.awt.*;

public class Window {
    private JFrame ventana;
    public MainPage mainPage;
    public Login login;
    public boolean logged=false;
    public Functions functions;
    public Window(MongoDatabase mongoDatabase){
        functions=new Functions(mongoDatabase);
        ventana=new JFrame();
        ventana.setLayout(null);
        ventana.setTitle("BoardFinder");//Titulo de la ventana
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Para cerrar la ventana
        ventana.getContentPane().setBackground(Color.BLACK);
        ventana.setSize(1280,720);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(null);
        ventana.setResizable(false);
        login=new Login(functions);
        ventana.add(login.getPanel());
        //mainPage=new MainPage(functions);//Borrar esto luego
        //ventana.add(mainPage.getPanel());//Borrar esto luego

        ventana.setVisible(true);
    }

    public void logged(){
        logged=true;
        ventana.remove(login.getPanel());
        mainPage=new MainPage(functions);
        ventana.add(mainPage.getPanel());
        ventana.revalidate();
        ventana.repaint();
    }

    public JFrame getVentana() {
        return ventana;
    }

    public MainPage getMainPage() {
        return mainPage;
    }
}
