package dataCollectionReceiver;

import com.google.gson.Gson;
import dataCollectionReceiver.activemq.MessageHandler;
import dataCollectionReceiver.database.DB;
import dataCollectionReceiver.model.Customer;
import dataCollectionReceiver.model.InvoiceData;
import dataCollectionReceiver.model.StationUsage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

// ky service merr datat qe vine nga service "stationdatacollector" nepermjet queue "stationDataResponse".
// Kto data grupohen ne nje StationUsage array per nje customer specifik me te gjitha perdorimet e te gjitha stacioneve.
// Ky service arrin te beje grupimin duke marre numrin e mesazheve per nje customer specifik nga mesazhi qe vjen nga serveri nepermjet queue "gatheringJob".
// Ky service pastaj merr te dhenat e customer nga database dhe i bashkon te gjitha the dhenat e perdorimit te stacioneve te nje customer
// ne nje objekt InvoiceData. Pasi krijohet ky objekt kthehet ne json dhe cohet te service "pdfGenerator"
// nepermjet queue "invoiceData".
public class Main {

    public static void main(String[] args) {
        Gson gson = new Gson(); // onjekti gson ben kthimin nga json ne objekt dhe e anasjellta
        List<String> messages = MessageHandler.getAllMessages("gatheringJob"); // ketu merren te gjitha mesazhet nga queue "gatheringJob". Ktu jane te gjitha kerkesat te ardhura nga serveri per te krijuar info per ivoice
        for (String gatheringMsg : messages){ // loop q behet run per cdo mesazh i marre nga queue
            int[] jobDesc = gson.fromJson(gatheringMsg, int[].class); // ktu mesazhi kthehet nga json ne int array ku int te index 0 esht id i customer,
            // dhe int te index 1 mban numrin e stacioneve. Kjo dmth q numri i dyt tregon se sa mesazhe nga queue "stationDataRequest" duhet te kontrollohen
            StationUsage[] usages = new StationUsage[0]; // ktu ruhen StationUsage array-t qe vijne nga service "StationDataCollector" mepermjet queue "stationDataResponse"
            // duke i kombinuar te gjitha perdorimet e stacioneve te ndryshme ne nje array per nje customer specifik me te gjitha stacionet.
            boolean dataFound = false; // kjo variabel boolean perdoret per te pare nqs data u gjenden per nje customer/gatheringJob specifik
            for (int i = 0; i < jobDesc[1]; i++) {// loop qe behet run aq here sa thot numri q ndodhet te pozicioni 1 i mesazhit "gatheringMsg"
                String stationMsg = MessageHandler.getMessage("stationDataResponse"); //ketu merret nje mesazh nga queue "stationDataResponse"
                //, ky mesazh permban informacionet e perdorimeve te nje stacioni nga nje customer
                if (!stationMsg.equals("no-data")) { // kontrollon qe mesazhi mos te jete eshte "no-data"
                    dataFound = true; // dataFound behet true, kjo dmth qe kjo dataGatheringJob gjeti disa data per te nisur drejt service "pdfGenerator"
                    StationUsage[] sus = gson.fromJson(stationMsg, StationUsage[].class); // ketu mesazhi i marre kthehet ne nje StationUsage array.
                    usages = (StationUsage[]) Stream.concat(Arrays.stream(sus), Arrays.stream(usages)).toArray(StationUsage[]::new); // arrray i marre nga mesazhi shtohet te array usages
                }
            }
            if(!dataFound) {
                MessageHandler.sendMessage("invoiceResponse"+jobDesc[0],"not-found"); // nqs nuk eshte gjendur asnje informacion nga service "statoinDataCollector"
                //nje mesazh cohet direkt drejt serverit, nepermjet queue "invoiceResponse{customerID}"
                continue; //ne kete rast loop kalon direkt te mesazhi/gatheringJob tjeter, sepse nuk ka gje per tu derguar tek service "pdfGenerator"
            }
            InvoiceData invoiceData = new InvoiceData(); // objekti invoiceData mbushet me te dhenat qe vijne nga service "stationDataCollector", si dhe nga database-i
            try {
                List<Integer> kwhs = new ArrayList<Integer>(); //ktu krijohet lista e kilovateve te perdorur ne nje karikim
                List<Integer> stationIDs = new ArrayList<Integer>(); // ktu krijohet lista e stacioneve te pedorur nga klienti
                List<Timestamp> datetimes = new ArrayList<Timestamp>(); // lista e kohes se pedorimit
                for (StationUsage usage : usages) { // per cdo StationUsage object ne array e mesiperm, informacionet shtohen te listat e mesiperme
                    kwhs.add(usage.getKwh());
                    stationIDs.add(usage.getIdStation());
                    datetimes.add(usage.getDatetime());
                }
                // poshte invoiceData mbushet me listat e ndryshme si dhe informacionet e customer-it
                invoiceData.setKwhs(kwhs);
                invoiceData.setStationIDs(stationIDs);
                invoiceData.setDatetimes(datetimes);
                Customer customer = DB.getCustomerInfo(usages[0].getIdCustomer());
                invoiceData.setCustomerID(customer.getId());
                invoiceData.setfName(customer.getfName());
                invoiceData.setlName(customer.getlName());
                invoiceData.setAddress(customer.getAddress());
                invoiceData.setZip(customer.getZip());
                invoiceData.setCountry(customer.getCountry());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            MessageHandler.sendMessage("invoiceData", gson.toJson(invoiceData, InvoiceData.class)); // objekti invoiceData kthehet ne json dhe cohet te service "pdfGenerator" nepermjet queue "invoiceData"
        }
    }
}
