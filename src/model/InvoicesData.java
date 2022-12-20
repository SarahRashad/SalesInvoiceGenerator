package model;

import java.util.ArrayList;

/**
 * POJO class (Only a data holder class)
 * This class holds the data read from input files and the names of the input files
 */
public class InvoicesData {
    private String invoiceHeaderFilename;
    private String invoiceLineFilename;

    ArrayList<InvoiceHeader> invoiceHeaders;

    public InvoicesData(){

    }
    public InvoicesData(String invoiceHeaderFilename,String invoiceLineFilename, ArrayList<InvoiceHeader> invoiceHeaders){
        this.setInvoiceHeaderFilename(invoiceHeaderFilename);
        this.setInvoiceLineFilename(invoiceLineFilename);
        this.setInvoiceHeaders(invoiceHeaders);

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

    public ArrayList<InvoiceHeader> getInvoiceHeaders() {
        return invoiceHeaders;
    }

    public void setInvoiceHeaders(ArrayList<InvoiceHeader> invoiceHeadersList) {
        this.invoiceHeaders = invoiceHeadersList;
    }
}
