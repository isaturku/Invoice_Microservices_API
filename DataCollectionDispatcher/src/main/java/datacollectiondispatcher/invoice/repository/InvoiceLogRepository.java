package datacollectiondispatcher.invoice.repository;

import datacollectiondispatcher.invoice.model.InvoiceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// kjo klase ben te mundur lidhjen me database, specifikisht me tabelen "invoice_log".
// te file "resources/application.properties" behete konfigurimi i lidhjes me database
@Repository // annotation qe trg se kjo  klase funksionon si lidhje me nje tabele te nje database-i
public interface InvoiceLogRepository extends JpaRepository<InvoiceLog,Integer> {
    //annotation @query trg se metoda e poshtme kthen mrapsht rezultatet e query-t brenda value. Ne kete rast kthen mbrapsht invoice-in e fundit te ruajtur ne database per nje customer specifik
    @Query(value = "SELECT OBJECT(i) from InvoiceLog i where i.customerID = ?1 and i.id = (select max(i.id) from InvoiceLog i where i.customerID = ?1)")
    InvoiceLog getLatestInvoiceForCustomer(int id);
}
