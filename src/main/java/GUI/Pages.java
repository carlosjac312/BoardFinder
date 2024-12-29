package GUI;

import javax.swing.*;
import java.awt.*;

public class Pages {
    JPanel panel=new JPanel();
    public State state;

    public JPanel getPanel() {
        return panel;
    }
}

class Search extends Pages {
    public Search() {
        state=State.SEARCH;
        panel=new JPanel();
        panel.setBackground(Color.green);
    }
}

class Create extends Pages {
    public Create() {
        state=State.CREATE;
        panel=new JPanel();
        panel.setBackground(Color.red);
    }
}
