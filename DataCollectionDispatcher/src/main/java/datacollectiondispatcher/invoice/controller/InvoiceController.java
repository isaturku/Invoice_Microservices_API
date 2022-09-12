package datacollectiondispatcher.invoice.controller;

import datacollectiondispatcher.activemq.MessageHandler;
import datacollectiondispatcher.invoice.model.Station;
import datacollectiondispatcher.invoice.service.InvoiceService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

//Klasa "invoiceController" perdoret per te programuar REST API. Kjo klase merret me http request qe vijne te
// te path "/invoices"
@RestController // annotation qe tregon qe kjo klase kontrollon REST API
@RequestMapping(path = "invoices/{customerID}") // annotation qe tregon se kjo klase merret me http requests qe vijne te path i specifikuar
public class InvoiceController {
    private final InvoiceService invoiceService;
    @Autowired // annotation pr dependency injection. Tregon q cdo objekt invoiceController duhet te kete nje objekt invoceService, dhe kjo behet automatikisht
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping// annotation qe trg se kjo metode perdoret per tu marre me HTTP GET requests.
    @ResponseBody // annotation q trg se nje pergjigje kthehet si HTTP Response
    public ResponseEntity<byte[]> invoiceGET(@PathVariable("customerID") int customerID) {
        HttpHeaders headers = new HttpHeaders(); // ketu vihen headers te http response. Headers jane detaje(meta-data) te http response
        byte[] contents = new byte[0]; // ketu do vihet body i http response, dmth pdf file, ose nje string "not-completed"
        String fileName = invoiceService.getFileName(customerID); // ketu merret emri i pdf file me ane te invoiceService
        if (fileName.equals("not-completed")){ // ketu kontrollohet nqs metoda invoiceService.getFileName() kthen mrapsht "not-completed"
            headers.setContentType(MediaType.TEXT_PLAIN); // ketu vendoset nje header ne http response qe thote se response eshte tekst i thjeshte
            contents = "not-completed".getBytes(); // body i response behet string "not-completed" ne forme array byte-sh
            return new ResponseEntity<byte[]>(contents,headers,HttpStatus.OK); // ketu response kthehet te client(ne kete rast te GUI). kjo response ka tre parametra
            //1. emr i parametrit: body; emr i vairables : contents; permbajtja e variables : string "not-completed"
            //2. emr i parametrit : headers; emr i variables: headers; permajtja e variables : "content-type: text/plain"
            //3. emr i parametrit : status; status : 200 qe simbolizon q http response eshte successful
        }
        if(fileName.equals("not-found")) { // ketu kontrollohet nqs metoda invoiceService.getFileName() kthen mrapsht "not-found"
            fileName = invoiceService.getPreviousDocName(customerID); // ketu metoda getPreviousDocName() e objektit "invoiceService"  kontrollon ne database per invoice te meparshem
            if (fileName.isBlank()) // nqs asnje invoice nuk gjendet nje http response kthehet me status 404, "error: file not found"
                return new ResponseEntity<byte[]>(null,null,HttpStatus.NOT_FOUND);
        }
        try {
            File file = new File(".\\invoices_server\\"+fileName); // ketu hapet file qe merret ose nga database ose nga pdfGenerator
            InputStream in = new FileInputStream(file); // file kthet ne inputstream
            contents = in.readAllBytes(); // inputstream kthehet ne array byte-sh
        } catch (IOException e) {
            e.printStackTrace();
        }
        headers.setContentType(MediaType.APPLICATION_PDF); // header "content-type" i http response vendoset si "application/pdf"
        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+fileName+"\""); // ketu vendoset emr i file
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0"); // header te tjere per response
        return new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK); // ketu response kthehet te client(ne kete rast te GUI). kjo response ka tre parametra
        //1. emr i parametrit: body; emr i vairables : contents; permbajtja e variables : byte array i invoice pdf file
        //2. emr i parametrit : headers; emr i variables: headers; permajtja e variables : "content-type: application/pdf","content-dispostion: attachment; filename=\fileName\,"cache-control:must-revalidate, post-check=0, pre-check=0"
        //3. emr i parametrit : status; status : 200 qe simbolizon q http response eshte successful
    }
    @PostMapping//annotation qe trg se kjo metode perdoret per tu marre me HTTP POST requests.
    public void invoicePOST(@PathVariable("customerID") int customerID){ //ketu fillohet puna per gjenerimin e pdf.
        invoiceService.sendMessage(customerID); // metoda sendMessage() e objektit invoiceService con mesazhe me activemq  drejt services te tjere
    }
}
