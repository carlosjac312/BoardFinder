package GUI;

import javax.swing.*;
import java.awt.*;

public class Window {
    private JFrame ventana;
    public MainPage mainPage;
    public Window(){
        ventana=new JFrame();
        ventana.setLayout(null);
        ventana.setTitle("BoardFinder");//Titulo de la ventana
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Para cerrar la ventana
        ventana.getContentPane().setBackground(Color.BLACK);
        ventana.setSize(1280,720);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(null);
        ventana.setResizable(false);

        mainPage=new MainPage();
        ventana.add(mainPage.getPanel());

        ventana.setVisible(true);
    }

    public JFrame getVentana() {
        return ventana;
    }

    public MainPage getMainPage() {
        return mainPage;
    }
}
