package model;

import controller.Controller;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileOperations {
    public static ArrayList<InvoiceHeader> readFile(String invoiceHeaderFilename,String invoiceLineFilename){
        //Check for file format ".csv"
        try {
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
                validation+="\n";
                validation+= FileOperations.validateDateFormat(data[1]);
                validation+="\n";
                validation+= FileOperations.validateNameFormat(data[2]);
                validation+="\n";
            }
            return validation.trim();
        }
        catch(Exception e){
            return "Cannot open file...";
        }
    }

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
                validation+="\n";
                validation+= FileOperations.validateNameFormat(data[1]);
                validation+="\n";
                validation+= FileOperations.validateDoubleFormat(data[2]);
                validation+="\n";
                validation+= FileOperations.validateIntegerFormat(data[3]);
                validation+="\n";
            }
            return validation.trim();
        }
        catch(Exception e){
            return "Cannot open file...";
        }
    }

    private static String validateNameFormat(String inputName){
        if(inputName.matches("[a-zA-Z].*$"))
            return "";
        return "Invalid name: Make sure to start name with a letter";
    }

    private static String validateDoubleFormat(String inputDouble){
        double isDouble;
        try {
            isDouble = Double.parseDouble(inputDouble);
        }
        catch(Exception e){
            return "Invalid price format: Data field should be a double value";
        }
        if(isDouble<0){
            return "Invalid price value: Data field cannot be of negative price";
        }

        return "";
    }
    private static String validateIntegerFormat(String inputInteger){
        Integer isInteger;
        try {
            isInteger = Integer.parseInt(inputInteger);
        }
        catch(Exception e){
            return "Invalid value format: Data field should be an integer";
        }
        if(isInteger<0){
            return "Invalid value: Data field cannot be of negative value";
        }

        return "";
    }
    private static String validateDateFormat(String inputDate){
        if (inputDate.trim().equals(""))
            return "Empty Date Field use MM/DD/YYYY or MM-DD-YYYY format";
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
                return "Invalid Date: Please, use DD/MM/YYYY or DD-MM-YYYY format with valid values";
            }
        }
        return "";
    }
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

    public void writeFile(ArrayList<InvoiceHeader> invoiceHeaders) {
        Controller controller = Controller.getInstance();
        String invoiceHeaderFilename = controller.getInvoiceHeaderFilename();
        String invoiceLineFilename = controller.getInvoiceLineFilename();
        //Check for file format ".csv"
        try {
            if (!invoiceHeaderFilename.endsWith(".csv"))
                throw new Exception("Wrong file format: InvoiceHeader file should be .csv file");
            if (!invoiceLineFilename.endsWith(".csv"))
                throw new Exception("Wrong file format: InvoiceLine file should be .csv file");

            File headerFile = new File(invoiceHeaderFilename);
            if (!headerFile.exists() || headerFile.isDirectory()) {
                throw new FileNotFoundException("File not found: InvoiceHeader.csv File not Found");
            }
            File lineFile = new File(invoiceLineFilename);
            if (!lineFile.exists() || lineFile.isDirectory()) {
                throw new FileNotFoundException("File path not found: InvoiceLine.csv File not Found");
            }
            writeInvoiceDate(invoiceHeaderFilename,invoiceLineFilename,invoiceHeaders);

        }
        catch(FileNotFoundException e){
            System.err.println(e.getMessage());
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
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
