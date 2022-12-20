package view;

import controller.Controller;
import model.InvoiceHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleView {
    public ConsoleView(){



    }

    /**]
     * This function is the responsible for initiating the load invoice data scenario and
     * printing the invoice data after receiving them from controller
     * @param invoiceHeaderFilename
     * @param invoiceLineFilename
     * @return
     */
    public int loadFiles(String invoiceHeaderFilename, String invoiceLineFilename){
        Controller controller=Controller.getInstance();
        controller.loadInvoiceData(invoiceHeaderFilename, invoiceLineFilename);
        ArrayList<InvoiceHeader> ihl = controller.getInvoiceHeaders();
        if(ihl!=null) {
            for (InvoiceHeader ih : ihl) {
                System.out.println(ih);
            }
            return 1;
        }
        else{
            System.out.println("No Data to Display: The InvoiceHeader and the invoiceLine files are either empty or an error occurred during loading the header files.");
            return -1;
        }
    }

    /**
     * This function prompt the user to save the currently held invoice data to another files
     * it also notifies whether the process is successful or an error occurs and what is that error.
     */
    public void showSaveFilePrompt(){
        Scanner sc= new Scanner(System.in);
        System.out.println("Would you like to save invoice data in another file? \n Press y or Y for Yes / anything else for No");
        String saveCheck=sc.next();
        if(saveCheck.equals("y")||saveCheck.equals("Y")){
            System.out.println("Please enter the invoiceHeader file full path or just the file name if you want to save the file in the current directory:");
            String invoiceHeaderFilename=sc.next();
            System.out.println("Please enter the invoiceLine file full path or just the file name if you want to save the file in the current directory:");
            String invoiceLineFilename=sc.next();
            Controller controller = Controller.getInstance();
            int status=controller.saveInvoiceData(invoiceHeaderFilename,invoiceLineFilename);
            if(status==-1) { //Error occurred in saving data
                System.out.println("An error occurred while saving the invoice data ... do you want to try again?\n Press y or Y for Yes / anything else for No");
                String continueCheck = sc.next();
                if (continueCheck.equals("y") || continueCheck.equals("Y")) {
                    showSaveFilePrompt();
                }
            }else if(status==-3){ // files do not exist create files and continue?
                System.out.println("Files do not exist, do you want to create new files and continue? \n Press y or Y for Yes / anything else for No");
                String createFilesCheck=sc.next();
                if (createFilesCheck.equals("y") || createFilesCheck.equals("Y")) {
                    try {
                        File invoiceHeaderFile = new File(invoiceHeaderFilename);
                        invoiceHeaderFile.createNewFile();
                        File invoiceLineFile = new File(invoiceLineFilename);
                        invoiceLineFile.createNewFile();
                        status=controller.saveInvoiceData(invoiceHeaderFilename,invoiceLineFilename);
                        if(status==0)
                            System.out.println("Data Saved successfully in "+invoiceHeaderFilename+" and "+invoiceLineFilename);
                        else
                            System.out.println("Multiple errors occurred and no recovery plan succeeded ... Thank you!!");
                    }catch(Exception e){
                        System.err.println("An error occurred while creating new files");
                    }

                }
            }
            else{
                System.out.println("Data Saved successfully in "+invoiceHeaderFilename+" and "+invoiceLineFilename);
            }
            
        }else {
            System.out.println("Thank you... Good Bye!!");
        }
    }
}
