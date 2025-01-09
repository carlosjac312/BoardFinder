import GUI.Window;
import StateManagement.StateManager;
import com.mongodb.client.MongoDatabase;

public class App implements Runnable{
    private Thread mainThread;
    private Window window;
    private StateManager stateManager;
    boolean kkck=true;

    public void startThread(MongoDatabase mongoDatabase){
        window=new Window(mongoDatabase);
        stateManager=new StateManager(window);
        stateManager.initState();
        mainThread=new Thread(this);
        mainThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/60;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (mainThread!=null){
            long currentTime = System.nanoTime();
            stateManager.stateChange();

            window.getVentana().revalidate();
            //window.getVentana().repaint();

            try {
                double remainingTime=nextDrawTime-System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime<0){
                    remainingTime=0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
