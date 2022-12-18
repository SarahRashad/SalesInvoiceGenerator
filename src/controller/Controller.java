package controller;

// Singleton Design for the controller
//only on instance is needed
//The controller sole purpose is to control the flow of data from model to view
// and organize the requests from view to model
public class Controller {
    private static Controller controller = null;

    private Controller(){

    }

    /**
     * Only one instace can be created and returned from class Controller
     * @return Controller instance
     */
    public static Controller getInstance(){
        if (controller==null){
            controller = new Controller();
        }
        return controller;
    }
}
