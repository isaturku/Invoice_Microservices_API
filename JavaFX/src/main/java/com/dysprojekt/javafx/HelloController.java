package com.dysprojekt.javafx;

import com.dysprojekt.javafx.http.API;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.List;


public class HelloController{
    @FXML
    public TextField IdField;
    @FXML
    private Label status;
    @FXML
    private Hyperlink fileLink;
    private String path; // path ku do te ruhet file i shkarkuar nga serveri


    private final String apiURL = "http://localhost:8080/invoices/";

    @FXML // kjo metode thirret ne momentin qe shtypet butoni
    public void sendPostRequest() throws IOException, URISyntaxException, InterruptedException {
        fileLink.setVisible(false); // linku i file fshihet ne rast se me perpara eshte bere nje request tjeter
        String customerID =IdField.getText(); // ketu ruhet inputi i user\
        if(customerID.isBlank()){ // nqs inputi eshte bosh kur useri shtyp butonin ather GUI thot se request nuk mund te behet pa nje id, dhe metoda ndalon
            status.setText("The customer ID is required!");
            return;
        }
        API.generateInvoice(customerID); // kjo eshte metoda qe con http post request te rest api
        IdField.clear(); // text i input fshihet
        new Thread(()->{ // te ky thread cohet http get request 40 her cd0 2 sekonda nepermjet metodes requestFile
            // , ose deri ne momentin kur metoda ben return true
            int i = 0; //  counter qe numeron sa her eshte kerkuar pdf nga serveri
            boolean done = false; // variabel boolean qe shef nqs gui ka marre nje pergjigje nga serveri.
            do {
                try {
                    Thread.sleep(2000); // kjo i ben pauze 2 sekonda programit ne menyre qe serveri te kete kohe para request tjeter
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                done = requestFile(customerID); // ktu done merr return value qe shef nqs metoda requestFile ben return true
                i++; // counter inkrementohet me 1
            }while (!done && i<40);
            if(!done){ // nqs done ashte akoma false, dmth se gui nuk ka marre response(error 404 ose pdf file) nga serveri
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    status.setText("Timeout!");
                } // nqs done esht akoma false pas gjithe requests, ather GUI shfaq nje mesazh "Timeout!"
            });
            }
        }).start();
    }
    //kjo metode con nje http get request te rest api
    private boolean requestFile(String id){
        boolean result = false;  // vlera q behet return, eshte default false

        HttpRequest getrequest = HttpRequest.newBuilder() // ketu krijohet nje get request
                .uri(URI.create(apiURL + id)) // ketu vihet url, specifikisht "http://localhost:8080/invoices/{customerID}"
                .GET() // ketu vihet metoda e http request, ne kete rast get
                .build(); // metoda build jep mrapsht nje objekt httprequest
            try {
                HttpResponse<InputStream> getresponse = HttpClient.newHttpClient() // ketu get rquest cohet tek serveri dhe priter nje response
                        .send(getrequest, responseInfo ->
                                HttpResponse.BodySubscribers.ofInputStream());
                if (getresponse.statusCode() == 200) { // ky if kontrollon nqs http response ka nje status prej 200, q dmth se http response ka ardhur pa errrore
                    if ( getresponse.headers().map().get("Content-Type").get(0).equals("application/pdf")){ // ketu kontrollohet nqs response eshte nje pdf file
                        result = true; // ne kete rast result behet true, qe dmth qe kjo metode ben return true, dhe GUI ndalon se uari request drejt serverit
                        path = "invoices_client/invoice_"+new Date().getTime()+".pdf"; // path ku pdf file response do te ruhet
                        Files.copy(getresponse.body(),Paths.get(path)); // ky rresht kodi kopjon pdf file qe vjen nga serveri dhe e downloadon te path i specifikuar siper
                        Platform.runLater(()->{  // ketu GUI i tregon userit se pdf file eshte marre nga serveri dhe gjithashtu shfaqet linku qe hap pdf file
                            status.setText("Open Invoice-PDF with link below:");
                            fileLink.setVisible(true); // ky kod shfaq linkun qe me pare nuk dukesh
                            fileLink.setText("Open PDF");
                        });
                    }else { // nqs response nuk eshte file pdf, athere GUI kupton se pdf nuk eshte krijuar akoma, dhe i thote userit te prese pasi pdf eshte duke u krijuar
                        Platform.runLater(() -> {
                                status.setText("Invoice is being generated.");
                        });
                    }
                }
                else if (getresponse.statusCode() == 404) { // nqs GUI merr nje error 404 nga serveri, athere GUI pushon se serguari request drejt serverit
                    // dhe i thote userit se nje invoice nuk u gjet per ate customer, zakonisht kjo dmth se customer nuk ekziston ose customer nuk ka perdorur kurre nje nga stacionet
                    result = true; // ktu return value behet true, qe i sinjalizon GUI-t te ndaloje se deguari request drejt serverit
                    Platform.runLater(() -> { // ketu GUI i thote userit se invoice nuk u gjet
                        status.setText("No Invoice found");
                    });
                }
                else{ // ketu kontrollohet per errore te tjera qe mund te ndodhin gjate http request
                    result = true; // nqs nje error i panjohur ndodh, athere GUI ndalon se derguari http request drejt serverit
                    Platform.runLater(() -> { // Gui i thote userit se nje error i panjohur ka ndodhur gjate nisjes te http request
                        status.setText("Unidentefied error!");
                    });
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return result;
    }
    @FXML // metoda qe lejon linkun te hape pdf file qe eshte shkarkuar nga GUI
    protected void onPdfLinkClick(){
        try
        {
            File file = new File(path); // ketu hapet file i shkarkuar nga serveri
            if(!Desktop.isDesktopSupported()) // ketu shihet nqs GUI mund te perdore funksione te PC (ne kete rast hapjen e nje file)
            {
                System.out.println("not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop(); // ketu merret nje instance i klases qe ndihmon me hapjen e file-it
            if(file.exists())
                desktop.open(file); // ketu hapet pdf file
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}





