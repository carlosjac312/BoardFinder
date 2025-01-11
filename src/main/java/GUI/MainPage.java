package GUI;

import functions.Functions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainPage {
    public JPanel panel, buttonspanel;
    private JButton searchButton, createButton, exitButton;
    public ArrayList<Pages> paginas; // [0]=CREATE, [1]=SEARCH
    public State estado;
    public boolean pressed=false;
    public Functions functions;

    public MainPage(Functions a) {
        functions=a;
        estado=State.SEARCH;
        System.out.println(estado.getValor());

        panel=new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.gray);
        panel.setSize(1280,720);

        buttonspanel=new JPanel(new GridLayout(0, 1, 0, 10));
        buttonspanel.setBackground(Color.cyan);
        buttonspanel.setSize(panel.getWidth()/5,panel.getHeight());
        panel.add(buttonspanel);

        searchButton=new JButton("Search Game");
        createButton=new JButton("Create Board");
        exitButton=new JButton("Exit");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estado=State.SEARCH;
                System.out.println(estado.getValor());
                pressed=true;
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estado=State.CREATE;
                System.out.println(estado.getValor());
                pressed=true;
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonspanel.add(searchButton);
        buttonspanel.add(createButton);
        buttonspanel.add(exitButton);

        //Creation of the pages
        paginas=MainPage.generatePages(this);
    }

    public JPanel getPanel() {
        return panel;
    }

    public static ArrayList<Pages> generatePages(MainPage mainPage){
        ArrayList<Pages> a=new ArrayList<>();
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
