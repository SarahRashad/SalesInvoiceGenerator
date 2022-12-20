import controller.Controller;
import model.InvoiceHeader;
import view.ConsoleView;

import java.util.ArrayList;

public class Main {
    /**
     * Main Function starts the ConsoleView which consequently communicates with the other modules
     */
    public static void main(String[] args) {

        //Default invoice filenames
        String invoiceHeaderFilename = "InvoiceHeader.csv";
        String invoiceLineFilename = "InvoiceLine.csv";
        //An instance of ConsoleView is created to take over and start the flow
        ConsoleView consoleView= new ConsoleView();
        int status=consoleView.loadFiles(invoiceHeaderFilename,invoiceLineFilename);
        if(status!=-1) {
            consoleView.showSaveFilePrompt();
        }
    }
}