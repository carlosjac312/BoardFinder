package StateManagement;

import GUI.MainPage;
import GUI.State;

public class StateManager {
    private MainPage mainPage;
    private State state;

    public StateManager(MainPage a, State b) {mainPage=a; state=b;}

    public void initState(){
        state=State.LOGIN;
        stateChange();
    }

    public void stateChange(){
        if (mainPage.pressed){
            switch (mainPage.estado) {
                case SEARCH:
                    updatePanels(State.SEARCH);
                    break;
                case CREATE:
                    updatePanels(State.CREATE);
                    break;
                default:

            }
            mainPage.pressed=false;
        }
    }
    public void updatePanels(State a){
        for(int i=0; i<mainPage.paginas.size(); i++) {
            mainPage.paginas.get(i).getPanel().setVisible(mainPage.paginas.get(i).state==a);
        }
    }

}
