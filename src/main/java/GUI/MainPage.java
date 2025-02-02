package GUI;

import functions.Functions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainPage {
    public JPanel panel, buttonspanel;
    private JButton searchButton, createButton, homeButton,exitButton;
    public ArrayList<Pages> paginas; // [0]=CREATE, [1]=SEARCH
    public State estado;
    public boolean pressed=false;
    public Functions functions;

    public MainPage(Functions a) {
        functions=a;
        estado=State.SEARCH;

        panel=new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.gray);
        panel.setSize(1280,720);

        buttonspanel=new JPanel(new GridLayout(0, 1, 0, 10));
        buttonspanel.setBackground(Color.cyan);
        buttonspanel.setSize(panel.getWidth()/5,panel.getHeight());
        panel.add(buttonspanel);

        homeButton=new JButton("Home");
        searchButton=new JButton("Search Game");
        createButton=new JButton("Create Board");
        exitButton=new JButton("Exit");

        //Creation of the pages
        paginas=MainPage.generatePages(this);
        //Todos los botones para cambiar el estado y actualizar la página
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estado=State.HOME;
                pressed=true;
                paginas.get(0).refreshHome();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estado=State.SEARCH;
                pressed=true;
                paginas.get(1).refreshSearch();
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estado=State.CREATE;
                pressed=true;
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonspanel.add(homeButton);
        buttonspanel.add(searchButton);
        buttonspanel.add(createButton);
        buttonspanel.add(exitButton);

    }

    public JPanel getPanel() {
        return panel;
    }

    public static ArrayList<Pages> generatePages(MainPage mainPage){ //generar las páginas
        ArrayList<Pages> a=new ArrayList<>();
        a.add(new Home(mainPage.functions));
        a.add(new Search(mainPage.functions));
        a.add(new Create(mainPage.functions));
        for(int i=0; i<a.size(); i++){
            a.get(i).getPanel().setVisible(i == 0); //Modificar este valor para mostrar la pagina q se quiere ver
            a.get(i).getPanel().setBounds(mainPage.getPanel().getWidth()/5,0,mainPage.getPanel().getWidth()-mainPage.getPanel().getWidth()/5, mainPage.panel.getHeight());
            mainPage.panel.add(a.get(i).getPanel());
        }
        return a;
    }
}
