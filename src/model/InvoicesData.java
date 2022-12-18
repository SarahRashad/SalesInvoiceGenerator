package model;

import java.util.ArrayList;

/**
 * This class holds the data read from input files
 */
public class InvoicesData {
    private String invoiceHeaderFilename;
    private String invoiceLineFilename;

    ArrayList<InvoiceHeader> invoiceHeaders;

    public InvoicesData(String invoiceHeaderFilename,String invoiceLineFilename, ArrayList<InvoiceHeader> invoiceHeaders){
        this.setInvoiceHeaderFilename(invoiceHeaderFilename);
        this.setInvoiceLineFilename(invoiceLineFilename);
        this.setInvoiceHeadersList(invoiceHeaders);

    }

    public String getInvoiceHeaderFilename() {
        return invoiceHeaderFilename;
    }

    public void setInvoiceHeaderFilename(String invoiceHeaderFilename) {
        this.invoiceHeaderFilename = invoiceHeaderFilename;
    }

    public String getInvoiceLineFilename() {
        return invoiceLineFilename;
    }

    public void setInvoiceLineFilename(String invoiceLineFilename) {
        this.invoiceLineFilename = invoiceLineFilename;
    }

    public ArrayList<InvoiceHeader> getInvoiceHeadersList() {
        return invoiceHeaders;
    }

    public void setInvoiceHeadersList(ArrayList<InvoiceHeader> invoiceHeadersList) {
        this.invoiceHeaders = invoiceHeadersList;
    }
}
