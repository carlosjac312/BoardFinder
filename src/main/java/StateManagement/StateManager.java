package StateManagement;

import GUI.MainPage;
import GUI.State;
import GUI.Window;
import com.mongodb.client.MongoDatabase;

public class StateManager {
    private Window window;

    public StateManager(Window a) {window=a;}

    public void initState(){
        while (!window.login.loged){
            System.out.println("Log in baby boy");
            window.getVentana().revalidate();
            window.getVentana().repaint();
        }
        window.logged();
        stateChange();
    }

    public void stateChange(){
        if (window.mainPage.pressed){
            switch (window.mainPage.estado) {
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
    public void updatePanels(State a){
        for(int i=0; i<window.mainPage.paginas.size(); i++) {
            window.mainPage.paginas.get(i).getPanel().setVisible(window.mainPage.paginas.get(i).state==a);
        }
    }

}
