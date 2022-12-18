import controller.Controller;
import model.InvoiceHeader;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        //Singleton Controller
        Controller controller = Controller.getInstance();
        String invoiceHeaderFilename = "InvoicHeader.csv";
        String invoiceLineFilename = "InvoiceLine.csv";
        controller.loadInvoiceData(invoiceHeaderFilename, invoiceLineFilename);
        ArrayList<InvoiceHeader> ihl = controller.getInvoiceHeaders();
        for (InvoiceHeader ih : ihl) {
            System.out.println(ih);
        }
    }
}