package StateManagement;

import GUI.State;
import GUI.Window;

public class StateManager {
    private Window window;

    public StateManager(Window a) {window=a;}

    public void initState(){ //estado inicial
        while (!window.login.loged){
            System.out.println("Log in");
            window.getVentana().revalidate();
            window.getVentana().repaint();
        }
        window.logged();
        stateChange();
    }

    public void stateChange(){ //manejador del estado
        if (window.mainPage.pressed){
            switch (window.mainPage.estado) {
                case HOME:
                    updatePanels(State.HOME);
                    break;
                case SEARCH:
                    updatePanels(State.SEARCH);
                    break;
                case CREATE:
                    updatePanels(State.CREATE);
                    break;
                default:

            }
            window.mainPage.pressed=false;
        }
    }
    public void updatePanels(State a){ //hacer páginas visibles o no
        for(int i=0; i<window.mainPage.paginas.size(); i++) {
            window.mainPage.paginas.get(i).getPanel().setVisible(window.mainPage.paginas.get(i).state==a);
        }
    }

}
