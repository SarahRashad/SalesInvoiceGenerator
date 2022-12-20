package model;

import controller.Controller;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class handles the read and write operations from invoiceHeader and invoiceLine .csv files
 * All function are static as they are utility functions .. they don't depend on any member data
 */
public class FileOperations {
    /**
     * reads invoiceHeader and invoiceLine files ... the function
     * first:
     * Handles wrong file format (non .csv file), file not found and error in file data field formats
     * if no problem is encountered it calls readInvoiceFileHeader and readInvoiceLine function to do the actual reading
     * @param invoiceHeaderFilename
     * @param invoiceLineFilename
     * @return
     */
    public static ArrayList<InvoiceHeader> readFile(String invoiceHeaderFilename,String invoiceLineFilename){

        try {
            //Check for file format ".csv"
            if (!invoiceHeaderFilename.endsWith(".csv"))
                throw new Exception("Wrong file format: InvoiceHeader file should be .csv file");
            if (!invoiceLineFilename.endsWith(".csv"))
                throw new Exception("Wrong file format: InvoiceLine file should be .csv file");

            File headerFile = new File(invoiceHeaderFilename);
            if(!headerFile.exists() || headerFile.isDirectory()) {
                throw new FileNotFoundException("File not found: InvoiceHeader.csv File not Found");
            }
            File lineFile = new File(invoiceLineFilename);
            if(!lineFile.exists() || lineFile.isDirectory()) {
                throw new FileNotFoundException("File not found: InvoiceLine.csv File not Found");
            }

            String invoiceHeaderFileCheck= checkInvoiceHeaderFileFormat(invoiceHeaderFilename);
            String invoiceLineFileCheck= checkInvoiceLineFileFormat(invoiceLineFilename);

            if(!invoiceHeaderFileCheck.trim().equals(""))
                throw new Exception("Wrong file format: "+invoiceHeaderFileCheck);
            if(!invoiceLineFileCheck.trim().equals(""))
                throw new Exception("Wrong file format: "+invoiceLineFileCheck);
            ArrayList<InvoiceHeader> invoiceHeaders=FileOperations.readInvoiceHeaderFile(invoiceHeaderFilename);
            FileOperations.readInvoiceLineFile(invoiceLineFilename,invoiceHeaders);
            return invoiceHeaders;

        }
        catch(FileNotFoundException e){
            System.err.println(e.getMessage());
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * This function checks whether invoiceHeader.csv fields format are correct
     * The checks are
     * 1.Date check
     * 2.Integer Checks (and makes sure that invoiceNum is of a non-negative value)
     * 3.Name starts with a letter
     * @param filename
     * @return
     */
    private static String checkInvoiceHeaderFileFormat(String filename){
        //Controller controller = Controller.getInstance();
        BufferedReader csvReader = null;
        String validation="";

        try {
            int i=0;
            csvReader = new BufferedReader(new FileReader(filename));
            String row = "";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                validation+= FileOperations.validateIntegerFormat(data[0]);
                validation+= FileOperations.validateDateFormat(data[1]);
                validation+= FileOperations.validateNameFormat(data[2]);
            }
            return validation.trim();
        }
        catch(Exception e){
            return "Cannot open file...";
        }
    }

    /**
     * This function checks whether invoiceLine.csv fields format are correct
     * The checks are
     * 1.Integer Checks (and make sure that invoiceNum is of a non-negative value)
     * 2.Name starts with a letter
     * 3.Double Checks (and make sure that price is of a non-negative value)
     * 4.Integer Checks (and make sure that the count is of a non-negative value)
     * @param filename
     * @return
     */
    private static String checkInvoiceLineFileFormat(String filename){
        Controller controller = Controller.getInstance();
        BufferedReader csvReader = null;
        String validation="";
        try {
            csvReader = new BufferedReader(new FileReader(filename));
            String row = "";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                validation+= FileOperations.validateIntegerFormat(data[0]);
                validation+= FileOperations.validateNameFormat(data[1]);
                validation+= FileOperations.validateDoubleFormat(data[2]);
                validation+= FileOperations.validateIntegerFormat(data[3]);
            }
            return validation.trim();
        }
        catch(Exception e){
            return "Cannot open file...";
        }
    }

    /**
     * Receives a string and checks whether it is a valid name or not ( starts with a letter)
     * @param inputName
     * @return
     */
    private static String validateNameFormat(String inputName){
        if(inputName.matches("[a-zA-Z].*$"))
            return "";
        return "Invalid name: Make sure to start name with a letter\n";
    }

    /**
     * Receives a String and checks whether it's double value is of a valid format or not
     * @param inputDouble
     * @return
     */
    private static String validateDoubleFormat(String inputDouble){
        double isDouble;
        try {
            isDouble = Double.parseDouble(inputDouble);
        }
        catch(Exception e){
            return "Invalid price format: Data field should be a double value\n";
        }
        if(isDouble<0){
            return "Invalid price value: Data field cannot be of negative price\n";
        }

        return "";
    }
    /**
     * Receives a String and checks whether it's integer value is of a valid format or not
     * @param inputInteger
     * @return
     */
    private static String validateIntegerFormat(String inputInteger){
        Integer isInteger;
        try {
            isInteger = Integer.parseInt(inputInteger);
        }
        catch(Exception e){
            return "Invalid value format: Data field should be an integer\n";
        }
        if(isInteger<0){
            return "Invalid value: Data field cannot be of negative value\n";
        }

        return "";
    }
    /**
     * Receives a String and checks whether it is of a valid date format or not
     * @param inputDate
     * @return
     */
    private static String validateDateFormat(String inputDate){
        if (inputDate.trim().equals(""))
            return "Empty Date Field use MM/DD/YYYY or MM-DD-YYYY format\n";
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setLenient(false);
        try {
            Date javaDate = simpleDateFormat.parse(inputDate);

        }
        catch(ParseException e){
            simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.setLenient(false);
            try {
                Date javaDate = simpleDateFormat.parse(inputDate);

            }
            catch(ParseException e2) {
                return "Invalid Date: Please, use DD/MM/YYYY or DD-MM-YYYY format with valid values\n";
            }
        }
        return "";
    }

    /**
     * This function reads the invoiceHeaderFile and returns an ArrayList of invoiceHeaders
     * @param invoiceHeaderFilename
     * @return
     */
    private static  ArrayList<InvoiceHeader> readInvoiceHeaderFile(String invoiceHeaderFilename) {
        BufferedReader csvReader = null;
        ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<InvoiceHeader>();
        try {
            csvReader = new BufferedReader(new FileReader(invoiceHeaderFilename));
            String row="";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                int invoiceNum= Integer.parseInt(data[0]);
                String invoiceDate= data[1];
                String customerName= data[2];
                ArrayList<InvoiceLine> dataInvoiceLines = new ArrayList<InvoiceLine>();
                InvoiceHeader readInvoiceHeader = new InvoiceHeader(invoiceNum,invoiceDate,data[2],dataInvoiceLines);
                invoiceHeaders.add(readInvoiceHeader);
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return invoiceHeaders;
    }

    /**
     * This function reads the invoiceHeaderFile and adds every invoiceLine to its corresponding invoiceHeader
     * @param invoiceLineFilename
     * @param invoiceHeaders
     */
    private static void readInvoiceLineFile(String invoiceLineFilename, ArrayList<InvoiceHeader> invoiceHeaders) {
        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(invoiceLineFilename));
            String row="";
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                int invoiceNumber= Integer.parseInt(data[0]);
                String itemName= data[1];
                Double itemPrice = Double.parseDouble(data[2]);
                int count = Integer.parseInt(data[3]);
                InvoiceLine readInvoiceLine = new InvoiceLine(itemName,itemPrice,count);
                FileOperations.addInvoiceLinetoInvoiceHeader(invoiceNumber,readInvoiceLine, invoiceHeaders);
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void addInvoiceLinetoInvoiceHeader( int invoiceNumber, InvoiceLine invoiceLine, ArrayList<InvoiceHeader> invoiceHeaders){
        for(InvoiceHeader ih: invoiceHeaders){
            if( ih.getInvoiceNum()==invoiceNumber){
                ih.addInvoiceLine(invoiceLine);
                break;
            }
        }

    }

    /**
     * This function writes invoiceHeaders to invoiceHeaderFile and invoicLineFile
     * The fuction first handles
     * 1.Wrong file format (not ending with. csv)
     * 2.Missing contaning folder
     * 3.File not found
     * then calls writeInvoiceDate that does the actual writing
     * @param invoiceHeaderFilename
     * @param invoiceLineFilename
     * @param invoiceHeaders
     * @return
     */
    public static int writeFile(String invoiceHeaderFilename, String invoiceLineFilename,ArrayList<InvoiceHeader> invoiceHeaders) {
        int returnStatus= 1;
        try {
            //Checks for file format
            if (!invoiceHeaderFilename.endsWith(".csv")) {
                returnStatus=-1;
                throw new Exception("Wrong file format: InvoiceHeader file should be .csv file");

            }
            if (!invoiceLineFilename.endsWith(".csv")) {
                returnStatus=-1;
                throw new Exception("Wrong file format: InvoiceLine file should be .csv file");
            }
            //Checks for containing folder existence
            File fh= new File(invoiceHeaderFilename);
            File fl= new File(invoiceLineFilename);
            String invoiceHeaderFileDirectory=fh.getParent();
            String invoiceLineFileDirectory=fl.getParent();
            File headerFolder = new File(invoiceHeaderFileDirectory);
            if (!headerFolder.isDirectory()) {
                returnStatus= -1;
                throw new FileNotFoundException("Folder/file not found: InvoiceHeader  containing folder not Found");
            }

            File lineFolder = new File(invoiceLineFileDirectory);
            if (!lineFolder.isDirectory()) {
                returnStatus= -1;
                throw new FileNotFoundException("Folder/file not found: InvoiceLine  containing folder not Found");
            }

            //Checks for file existence
            File headerFile = new File(invoiceHeaderFilename);
            if (!headerFile.exists() || headerFile.isDirectory()) {
                returnStatus=-3;
                throw new FileNotFoundException("File not found: InvoiceHeader.csv File not Found");
            }
            File lineFile = new File(invoiceLineFilename);
            if (!lineFile.exists() || lineFile.isDirectory()) {
                returnStatus=-3;
                throw new FileNotFoundException("File path not found: InvoiceLine.csv File not Found");
            }
            writeInvoiceDate(invoiceHeaderFilename,invoiceLineFilename,invoiceHeaders);

        }
        catch(FileNotFoundException e){
            System.err.println(e.getMessage());
            return returnStatus;
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            return returnStatus;
        }
        return 0;
    }

    /**
     * The function that does the actual writing of invoicHeaders to invoiceHeaderFilename and invoiceLineFilename
     * @param invoiceHeaderFilename
     * @param invoiceLineFilename
     * @param invoiceHeaders
     */
    private static void writeInvoiceDate(String invoiceHeaderFilename,String invoiceLineFilename, ArrayList<InvoiceHeader> invoiceHeaders){
        try {

            FileWriter invoiceHeaderWriter = new FileWriter(invoiceHeaderFilename);
            BufferedWriter invoiceHeaderBuffered= new BufferedWriter(invoiceHeaderWriter);
            FileWriter invoiceLineWriter = new FileWriter(invoiceLineFilename);
            BufferedWriter invoiceLineBuffered= new BufferedWriter(invoiceLineWriter);
            for (InvoiceHeader ih: invoiceHeaders){
                int invoiceNum= ih.getInvoiceNum();
                invoiceHeaderBuffered.write(""+invoiceNum);
                invoiceHeaderBuffered.write(",");
                invoiceHeaderBuffered.write(""+ih.getInvoiceDate());
                invoiceHeaderBuffered.write(",");
                invoiceHeaderBuffered.write(""+ih.getCustomerName());
                invoiceHeaderBuffered.write("\n");
                invoiceHeaderBuffered.flush();
                for(InvoiceLine il : ih.getInvoiceLines()){
                    invoiceLineBuffered.append(""+ih.getInvoiceNum());
                    invoiceLineBuffered.append(",");
                    invoiceLineBuffered.append(""+il.getItemName());
                    invoiceLineBuffered.append(",");
                    invoiceLineBuffered.append(""+il.getItemPrice());
                    invoiceLineBuffered.append(",");
                    invoiceLineBuffered.append(""+il.getCount());
                    invoiceLineBuffered.append("\n");
                    invoiceLineBuffered.flush();
                }
            }

            invoiceHeaderWriter.close();
            invoiceLineWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
