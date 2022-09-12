package pdfGenerator.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import pdfGenerator.database.DB;
import pdfGenerator.model.InvoiceData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

// ktu behet kthimi i nje objekti invoiceData ne nje file pdf
public class PDFUtility {
    private static final String path = ".\\invoices_server"; // ketu trg folder ku do te ruhet pdf file

    public static String createInvoice(InvoiceData invoice) throws FileNotFoundException, DocumentException, SQLException, ClassNotFoundException {
        Document doc = new Document();
        String docName = invoice.getlName() +"_"+ new Date().getTime(); // emr i pdf krijohet nga mbiemri i klientit dhe kohen ne milisekonda
        PdfWriter.getInstance(doc,new FileOutputStream(path+"\\"+docName+".pdf")); // pdf file ruhet te folder perkates me emr perkates
        doc.open();
        doc.setPageSize(PageSize.A4);
        doc.addTitle("Invoice");
        doc.add(createTitle());
        doc.add(createInvoiceInfo(docName));
        doc.add(createCustomerInfo(invoice));
        doc.add(createTable(invoice));
        doc.close();
        DB.logInvoice(invoice.getCustomerID(),docName+".pdf");
        return docName+".pdf"; // emr i file behet return
    }

    //kjo metode krijon nje tabele me te dhenat e perdorimit te stacioneve
    private static PdfPTable createTable(InvoiceData invoice){
        PdfPTable table = new PdfPTable(4);
        PdfPCell rowHeader = new PdfPCell();
        rowHeader.setBackgroundColor(BaseColor.CYAN);
        rowHeader.setPhrase(new Phrase("#"));
        table.addCell(rowHeader);
        PdfPCell stationIdHeader = new PdfPCell();
        stationIdHeader.setBackgroundColor(BaseColor.CYAN);
        stationIdHeader.setPhrase(new Phrase("Station ID"));
        table.addCell(stationIdHeader);
        PdfPCell kwhHeader = new PdfPCell();
        kwhHeader.setBackgroundColor(BaseColor.CYAN);
        kwhHeader.setPhrase(new Phrase("kWhs"));
        table.addCell(kwhHeader);
        PdfPCell datetimeHeader = new PdfPCell();
        datetimeHeader.setBackgroundColor(BaseColor.CYAN);
        datetimeHeader.setPhrase(new Phrase("Time"));
        table.addCell(datetimeHeader);
        for (int i = 0; i<invoice.getStationIDs().size(); i++){
            table.addCell(""+(i+1));
            table.addCell(invoice.getStationIDs().get(i).toString());
            table.addCell(invoice.getKwhs().get(i).toString());
            table.addCell(invoice.getDatetimes().get(i).toString());
        }
        return table;
    }

    //kjo metode krijon nje paragraf me te dhenat e klientit
    private static Paragraph createCustomerInfo(InvoiceData invoice){
        Font font = FontFactory.getFont(FontFactory.HELVETICA,12, BaseColor.BLACK);
        String customerInfo = "\n"+invoice.getlName()+" "+invoice.getfName()
                +"\n"+invoice.getAddress()
                +"\n"+invoice.getZip()
                +"\n"+invoice.getCountry()+"\n\n\n\n\n\n\n\n";
        Paragraph customerParagraph = new Paragraph(customerInfo,font);
        return customerParagraph;
    }

    // kjo metode krijon nje titull
    private static Paragraph createTitle(){
        Font font = FontFactory.getFont(FontFactory.HELVETICA,18, BaseColor.BLACK);
        Paragraph title = new Paragraph("INVOICE",font);
        title.setAlignment(Element.ALIGN_CENTER);
        return title;
    }

    //kjo metode krijon nje pdf me emr dhe daten e krijimit e invoice
    private static Paragraph createInvoiceInfo(String docName){
        Font font = FontFactory.getFont(FontFactory.HELVETICA,12, BaseColor.BLACK);
        return new Paragraph("\n\n\nInvoice no.:"+docName+"\nDate created: "+new Date().toString(),font);
    }
}
