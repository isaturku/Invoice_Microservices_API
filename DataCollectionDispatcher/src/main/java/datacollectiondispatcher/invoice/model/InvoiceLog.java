package datacollectiondispatcher.invoice.model;

import javax.persistence.*;

// klase per tabelen invoice_log ne database
@Entity // annotation trg se klasa perfaqeson nje rrjesht te nje tabele ne database
@Table(name = "invoice_log") // annotaion trg se klasa lidhet me tabelen "invoice_log" ne databsae
public class InvoiceLog {
    @Id // ky annotation trg q attributi posht esht primary key i tabeles
    private int id;
    @Column(name = "customer_id") //ky annotation lidh attributin posht me kolonen "customer_id" ne tabele
    private int customerID;
    @Column(name = "pdf_name") //ky annotation lidh attributin posht me kolonen "pdf_name" ne tabele
    private String pdfName;

    public InvoiceLog(int id, int customerID, String pdfName) {
        this.id = id;
        this.customerID = customerID;
        this.pdfName = pdfName;
    }

    public InvoiceLog() {
    }

    public int getId() {
        return id;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
}
