package datacollectiondispatcher.invoice.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import datacollectiondispatcher.activemq.MessageHandler;
import datacollectiondispatcher.invoice.model.Station;
import datacollectiondispatcher.invoice.repository.InvoiceLogRepository;
import datacollectiondispatcher.invoice.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service  // annotation q trg se kjo klase eshte service dhe perdoret nga klasa t tjera
public class InvoiceService {
    private final StationRepository stationRepository;
    private final InvoiceLogRepository invoiceLogRepository;
    @Autowired // annotation pr dependency injection. Tregon q cdo objekt invoiceService duhet te kete nje objekt stationRepository, dhe kjo behet automatikisht
    public InvoiceService(StationRepository stationRepository,InvoiceLogRepository invoiceLogRepository) {
        this.stationRepository = stationRepository;
        this.invoiceLogRepository = invoiceLogRepository;
    }
    //metode qe merr gjithe stacionet me available "true".
    private List<Station> getAvailableStations(){
        return stationRepository.findAllByAvailable(true);
    }
    //kjo metode shef ne database per invoice te meparshem te nje customer.
    public String getPreviousDocName(int id){
        return invoiceLogRepository.getLatestInvoiceForCustomer(id).getPdfName();
    }
    // kjo metode con mesazhe drejt services te tjere
    public void sendMessage(int customerID){
        Gson gson = new Gson(); // gson perdoret per te kthyer objekte te ndryshme ne json dhe e anasjellta
        List<Station> stations = this.getAvailableStations(); // ketu merren te gjitha stacionet "available"
        for (Station station : stations) { // per cdo stacion te disponueshem dodhinaksionet e meposhtme
            int[] ids = new int[] {customerID,station.getId()}; // krijohet nje array ka 2 id, i pari esht id i customer dhe i dyti id i stacionit
            MessageHandler.sendMessage("stationDataRequest",gson.toJson(ids,int[].class)); // cohet nje mesazh te queue "stationDataRequest" me array e mespierm te kthyer ne json
        }
        int[] job = new int[] {customerID,stations.size()}; // ketu krijohet nje array me id te customer dhe numrin e stacioneve
        MessageHandler.sendMessage("gatheringJob", gson.toJson(job,int[].class)); // array i mesiperm cohet drejt queue "gatheringJob".
        // Service "dataCollectionReceiver" merr mesazhin e mesiperm.
    }
    public String getFileName(int customerID){
        Gson gson = new Gson();
        String msg = MessageHandler.getMessage("invoiceResponse"+customerID); // ketu merret mesazhi te queue "invoiceResponse{customerID}"
        if(msg.isEmpty()) // nqs queue "invoiceResponse{customerID}" esht bosh metoda kthen mrapsht "not-completed"
            return "not-completed";
        if(msg.equals("not-found")) // nqs queue "invoiceResponse{customerID}" ka mesazh "not-found", athere "404 kthehet mrapsht"
            return msg;
        Map invoiceResponse = gson.fromJson(msg,Map.class); // nqs queue permaban mesazhin qe duam, ky mesazh kthehet ne map. map ka dy vlera
        // 1. customerID - id i customer te cilit i perket invoice
        // 2. fileName - emr i pdf file
        return invoiceResponse.get("docName").toString(); // ktu behet return emr i pdf file.
    }
}
