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
    private InvoicesData invoicesData; // Data Holder (InvoiceHeaders and Filenames)

    private Controller(){
        invoicesData= new InvoicesData();
    }

    /**
     * Only one instace can be created and returned from class Controller
     * @return Controller instance
     * The controller main role is to control requests from view to model modules and Dataflow from model to view
     */
    public static Controller getInstance(){
        if (controller==null){
            controller = new Controller();
        }
        return controller;
    }

    /**
     * This function loads invoice Data from the provides .csv files
     * @param invoiceHeaderFilename
     * @param invoiceLineFilename
     */
    public void loadInvoiceData(String invoiceHeaderFilename,String invoiceLineFilename ){
        this.invoicesData.setInvoiceHeaders(FileOperations.readFile(invoiceHeaderFilename,invoiceLineFilename));
    }

    /**
     * This function saves the current invoices Data into the provides .csv files
     * @param invoiceHeaderFilename
     * @param invoiceLineFilename
     * @return
     */
    public int saveInvoiceData(String invoiceHeaderFilename,String invoiceLineFilename ){
        return FileOperations.writeFile(invoiceHeaderFilename,invoiceLineFilename,this.getInvoiceHeaders());
    }

    /**
     * This function returns the currently held invoiceHeaders arraylist
     * @return ArrayList of invoice Headers
     */
    public ArrayList<InvoiceHeader> getInvoiceHeaders(){
        return this.invoicesData.getInvoiceHeaders();
    }
}
