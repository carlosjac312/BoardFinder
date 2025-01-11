package GUI;

import functions.Functions;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.text.ParseException;
import java.util.Objects;

public class Pages {
    JPanel panel=new JPanel();
    public Functions functions;
    public State state;

    public Pages(Functions a){
        functions=a;
    }
    public JPanel getPanel() {
        return panel;
    }
}

class Search extends Pages {
    JTextField textField;
    JScrollPane scrollPane;
    public JPanel gamespanel;
    public JButton findButton;

    public Search(Functions f) {
        super(f);
        state=State.SEARCH;
        panel.setBackground(Color.green);
        panel.setLayout(null);
        textField=new JTextField();
        textField.setBounds(200,60,600,40);
        panel.add(textField);

        gamespanel=new JPanel();
        gamespanel.setLayout(new BoxLayout(gamespanel, BoxLayout.PAGE_AXIS));
        scrollPane=new JScrollPane(gamespanel);
        scrollPane.setBounds(100,120,800,500);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);
        for(int i=0; i<20; i++){
            JPanel jPanel=new JPanel();
            jPanel.setLayout(null);

            jPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2.0f)));
            jPanel.setPreferredSize(new Dimension(800, 180)); // Tamaño preferido

            JLabel label=new JLabel("GameName");
            label.setFont(new Font("Arial", Font.PLAIN, 20));
            label.setBounds(10,10,800,25);
            jPanel.add(label);

            JPanel infoPanel=new JPanel();
            infoPanel.setLayout(new GridLayout(2,2));
            infoPanel.setBounds(10,30,800,110);
            JLabel owner=new JLabel("Sexo");
            infoPanel.add(owner);
            JLabel location=new JLabel("Sexo");
            infoPanel.add(location);
            JLabel date=new JLabel("Sexo");
            infoPanel.add(date);
            JLabel numplayers=new JLabel("Sexo");
            infoPanel.add(numplayers);
            jPanel.add(infoPanel);

            JButton button=new JButton("Join");
            button.setBounds(680,140,80,30);
            jPanel.add(button);

            gamespanel.add(jPanel);
        }
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
