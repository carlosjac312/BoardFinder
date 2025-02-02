package GUI;

import functions.Functions;
import org.bson.Document;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pages {
    JPanel panel=new JPanel();
    public Functions functions;
    public State state;
    public JPanel gamespanel;
    protected JScrollPane scrollPane;
    protected ArrayList<Document> gamesdocuments;
    protected int pagina=0;


    public Pages(Functions a){
        functions=a;
    }
    public JPanel getPanel() {
        return panel;
    }
    public JPanel createPanel(Document game, Boolean ishome, JPanel panel) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);

        Document gameInfo=game.get("gameinfo", Document.class);
        List<String> playersId = game.getList("players", String.class);
        int currentplayers=playersId.size();
        jPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2.0f)));
        jPanel.setPreferredSize(new Dimension(800, 180)); // Tamaño preferido
        jPanel.setMaximumSize(new Dimension(800, 180)); // Evita que el panel se expanda
        jPanel.setMinimumSize(new Dimension(800, 180)); // Evita que el panel se reduzca

        // Label para el nombre del juego
        JLabel label = new JLabel(game.get("gamename", String.class)); // Extraer el nombre del juego
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setBounds(10, 10, 800, 25);
        jPanel.add(label);

        // Panel para información adicional
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 2)); // Grid de 2 filas por 2 columnas
        infoPanel.setBounds(10, 40, 800, 110);

        // Dueño (ObjectId convertido a String)
        JLabel owner = new JLabel("Owner: "+game.getString("owner"));
        infoPanel.add(owner);

        // Ubicación
        JLabel location = new JLabel("Location: "+gameInfo.getString("location"));
        infoPanel.add(location);

        // Fecha
        JLabel date = new JLabel("Date: "+gameInfo.getString("date"));
        infoPanel.add(date);

        // Número de jugadores
        JLabel numplayers = new JLabel("Current Players: "+currentplayers+"/"+ String.valueOf(game.getInteger("max_players")));
        infoPanel.add(numplayers);

        jPanel.add(infoPanel);

        // Botón para unirse
        JButton button = new JButton();
        button.setBounds(680, 140, 80, 30);
        if (ishome) {
            if(Objects.equals(game.getString("owner"), functions.getUserDataname())){
                button.setText("Delete");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        functions.deleteGame(game.getObjectId("_id"));
                        ArrayList<Document> refreshedgames;
                        panel.removeAll();
                        refreshedgames=functions.getUsergames();
                        for(Document document: refreshedgames){
                            System.out.println(document.toJson());
                            panel.add(createPanel(document,true,panel));
                        }
                        panel.revalidate();
                        panel.repaint();
                    }
                });
            } else {
                button.setText("Exit");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        functions.exitgame(game.getObjectId("_id"));
                        ArrayList<Document> refreshedgames;
                        panel.removeAll();
                        refreshedgames=functions.getUsergames();
                        for(Document document: refreshedgames){
                            System.out.println(document.toJson());
                            panel.add(createPanel(document,true,panel));
                        }
                        panel.revalidate();
                        panel.repaint();
                    }
                });
            }

        } else {
            button.setText("Join");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    functions.addUserToGame(game.getObjectId("_id"));
                    ArrayList<Document> refreshedgames;
                    panel.removeAll();
                    refreshedgames=functions.getAllGames(0);
                    for(Document document: refreshedgames){
                        panel.add(createPanel(document,false,panel));
                    }
                    panel.revalidate();
                    panel.repaint();
                }
            });
        }
        jPanel.add(button);

        return jPanel;
    }
    public void refreshHome(){
        gamespanel.removeAll();
        gamesdocuments=functions.getUsergames();
        for(Document game : gamesdocuments){
            System.out.println(game.toJson());
            gamespanel.add(createPanel(game,true,gamespanel));
        }
        gamespanel.revalidate();
        gamespanel.repaint();
    }
    public void refreshSearch(){
        gamespanel.removeAll();
        gamesdocuments=functions.getAllGames(pagina);
        for(Document document: gamesdocuments){
            gamespanel.add(createPanel(document,false,gamespanel));
        }
        gamespanel.revalidate();
        gamespanel.repaint();
    }
}

class Home extends Pages {
    JLabel title;
    public Home(Functions a) {
        super(a);
        state=State.HOME;
        panel.setLayout(null);
        title=new JLabel("My Games");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(150,60,600,40);
        panel.add(title);

        gamespanel=new JPanel();
        gamespanel.setLayout(new BoxLayout(gamespanel, BoxLayout.PAGE_AXIS));
        scrollPane=new JScrollPane(gamespanel);
        scrollPane.setBounds(100,120,800,500);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        gamesdocuments=functions.getUsergames();

        for(Document game : gamesdocuments){
            gamespanel.add(createPanel(game,true,gamespanel));
        }
    }
}

class Search extends Pages {
    JTextField textField;
    public JButton findButton;
    private ArrayList<Document> gamesdocuments;

    public Search(Functions f) {
        super(f);
        state=State.SEARCH;
        panel.setBackground(Color.green);
        panel.setLayout(null);
        textField=new JTextField();
        textField.setBounds(200,60,600,40);
        panel.add(textField);
        findButton=new JButton("Search");
        findButton.setBounds(800,60,90,40);
        panel.add(findButton);

        gamespanel=new JPanel();
        gamespanel.setLayout(new BoxLayout(gamespanel, BoxLayout.PAGE_AXIS));
        scrollPane=new JScrollPane(gamespanel);
        scrollPane.setBounds(100,120,800,500);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamespanel.removeAll();
                gamesdocuments=functions.getGame(textField.getText());
                for(Document document: gamesdocuments){
                    System.out.println(document.toJson());
                    gamespanel.add(createPanel(document,false,gamespanel));
                }
                gamespanel.revalidate();
                gamespanel.repaint();
            }
        });
    }
}

class Create extends Pages {
    JPanel upPanel,downPanel,mpayersPanel;
    JTextField nameField,locationField,dateField;
    JSpinner maxplayers;
    JLabel maxPlayersLabel,descripcionLabel,errorLabel;
    JTextArea descripcion;
    JButton button;

    public Create(Functions f) {
        super(f);
        nameField=new JTextField();
        locationField=new JTextField();

        try {
            // Crear un formato de fecha (dd/MM/yyyy)
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');

            // Crear un JFormattedTextField con el formato de fecha
            dateField = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        upPanel=new JPanel();
        downPanel=new JPanel();
        state=State.CREATE;
        panel.setLayout(new GridLayout(2,1));

        upPanel.setLayout(new GridLayout(2,2));
        generateField("Game name",nameField);
        generateField("Location",locationField);
        generateField("Date",dateField);

        //Generar sección de jugadores maximos
        mpayersPanel=new JPanel(null);
        maxPlayersLabel=new JLabel("Max Players");
        maxplayers=new JSpinner(new SpinnerNumberModel(2, 2, 15, 1));
        maxPlayersLabel.setBounds(30,20,400,60);
        maxPlayersLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        maxplayers.setBounds(30,70,400,40);
        mpayersPanel.add(maxPlayersLabel);
        mpayersPanel.add(maxplayers);
        upPanel.add(mpayersPanel);

        panel.add(upPanel);

        downPanel.setLayout(null);
        descripcionLabel=new JLabel("Description (Optional)");
        descripcionLabel.setBounds(300,20,400,60);
        descripcionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        downPanel.add(descripcionLabel);
        descripcion=new JTextArea();
        descripcion.setBounds(300,70,400,100);
        downPanel.add(descripcion);

        button=new JButton("Create");
        button.setBounds(400,180,200,60);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nameField.getText().isEmpty() && !locationField.getText().isEmpty() && !dateField.getText().isEmpty()) {
                    functions.addGame(nameField.getText(),dateField.getText(),locationField.getText(), descripcion.getText(),(Integer) maxplayers.getValue());
                    clearFields();
                    errorLabel.setVisible(false);
                } else errorLabel.setVisible(true);

            }
        });
        downPanel.add(button);
        errorLabel=new JLabel("Information Missing");
        errorLabel.setVisible(false);
        errorLabel.setBounds(420,250,200,60);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        downPanel.add(errorLabel);
        panel.add(downPanel);
    }
    public void generateField(String fieldname,JTextField textField){
        JPanel a=new JPanel();
        a.setLayout(null);
        JLabel label=new JLabel(fieldname);
        label.setBounds(30,20,400,60);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        textField.setBounds(30,70,400,40);
        a.add(label);
        a.add(textField);
        upPanel.add(a);
    }
    public void clearFields(){
        nameField.setText("");
        locationField.setText("");
        dateField.setText("");
        descripcion.setText("");
        maxplayers.setValue(2);
    }
}
