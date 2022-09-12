package pdfGenerator;

import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;
import pdfGenerator.activemq.MessageHandler;
import pdfGenerator.model.InvoiceData;
import pdfGenerator.pdf.PDFUtility;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Ky service merr datat qe vijne nga service "DataCollectionReceiver" nepermjet queue "invoiceData".
// Nga queue merret nje json i cili kthehet ne nje objekt InvoiceData. Nga ky objekt pastaj krijohet PDF file.
// Ky file ruhet te folder "invoices_server", dhe path i file cohet te REST API (DataCollectionDispatcher) nepermjet
// queue "invoiceResponse". Pas krijimit te pdf file emr i file dhe id i customer ruhen ne database te tabela "invoice_log".
public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        List<String> messages = MessageHandler.getAllMessages("invoiceData"); // ketu merren te gjitha mesazhet nga queue "invoiceData"
        for(String msg  : messages){ //per cdo mesazh i marre nga queue kryhen aksionet e meposhtme
            if(!msg.isBlank()) {
                InvoiceData invoiceData = gson.fromJson(msg, InvoiceData.class); //mesazhi kthehet nga json ne nje objekt invoiceData
                try {
                    String docName = PDFUtility.createInvoice(invoiceData);  // ketu invoicedata kthehet ne pdf dhe path i file ruhet te variable docName
                    int id = invoiceData.getCustomerID(); //ketu merret id i customer
                    Map<String, String> invoiceResponse = new HashMap<>(); // ketu krijhoet nje map me emr e pdf file dhe id te customer
                    invoiceResponse.put("docName", docName); // ketu futet emr i pdf file
                    invoiceResponse.put("customerID", "" + id); // letu futet id i customer
                    MessageHandler.sendMessage("invoiceResponse"+id, gson.toJson(invoiceResponse, Map.class)); // ketu map kthehet ne json dhe cohet te serveri nepermjet queue "invoiceResponse{customerID"
                } catch (FileNotFoundException e) {
                    System.out.println("File could not be generated!");
                } catch (DocumentException e) {
                    System.out.println("Document could not be generated!");
                } catch (SQLException throwables) {
                    System.out.println("Database Error!");
                } catch (ClassNotFoundException e) {
                    System.out.println("Driver Error!");
                }
            }
        }
    }
}
