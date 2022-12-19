package controller;

import model.FileOperations;
import model.InvoiceHeader;
import model.InvoicesData;

import java.util.ArrayList;

// Singleton Design for the controller
//only on instance is needed
//The controller sole purpose is to control the flow of data from model to view
// and organize the requests from view to model
public class Controller {
    private static Controller controller = null;
    private InvoicesData invoicesData;

    private Controller(){
        invoicesData= new InvoicesData();
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

    public void loadInvoiceData(String invoiceHeaderFilename,String invoiceLineFilename ){
        this.invoicesData.setInvoiceHeaders(FileOperations.readFile(invoiceHeaderFilename,invoiceLineFilename));
    }

    public ArrayList<InvoiceHeader> getInvoiceHeaders(){
        return this.invoicesData.getInvoiceHeaders();
    }

    public String getInvoiceHeaderFilename(){
        return this.invoicesData.getInvoiceHeaderFilename();
    }

    public String getInvoiceLineFilename(){
        return this.invoicesData.getInvoiceLineFilename();
    }
}
