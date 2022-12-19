import controller.Controller;
import model.InvoiceHeader;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        //Singleton Controller
        Controller controller = Controller.getInstance();
        String invoiceHeaderFilename = "InvoiceHeader.csv";
        String invoiceLineFilename = "InvoiceLine.csv";
        controller.loadInvoiceData(invoiceHeaderFilename, invoiceLineFilename);
        ArrayList<InvoiceHeader> ihl = controller.getInvoiceHeaders();
        if(ihl!=null) {
            for (InvoiceHeader ih : ihl) {
                System.out.println(ih);
            }
        }
    }
}