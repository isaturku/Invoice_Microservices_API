package stationDataCollector;

import com.google.gson.Gson;
import stationDataCollector.activemq.MessageHandler;
import stationDataCollector.database.DB;
import stationDataCollector.model.StationUsage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//merr data nga queue "stationDataRequest", dhe per cdo stacion merr informacionet e karikimit
// per nje customer dhe stacion perkates nga tabela "station_usage". Keto data futen ne nje objekt "stationusage"
// , behet nje liste me to dhe kthehen ne json dhe cohen ne queue "stationDataResponse". Nqs nk ka
// nuk ka data per nje cutomer te nje stacion nje mesazh "no-data" cohet te service "DataCollectionReceiver"
public class Main {

    public static void main(String[] args) {
        Gson gson = new Gson();
        List<String> messages = MessageHandler.getAllMessages("stationDataRequest");// ketu merren te gjitha mesazhet qe vijne nga serveri nepermjet queue "stationDataRequest"
        for (String json : messages){ // loop qe merr te gjitha mesazhet e queue me rradhe
            int[] values = gson.fromJson(json,int[].class); // mesazhi kthehet nga json ne int array
            try {
                List<StationUsage> usageList = DB.getStationDataForCustomer(values[0],values[1]); /// ketu merret lista e objekteve StationUsage nga database-i
                if(usageList.isEmpty())// nqs lista eshte bosh nje mesazh "no-data" cohet te service "DataCollectoinReceiver" nepermjet queue "stationDataResponse"
                    MessageHandler.sendMessage("stationDataResponse","no-data");
                else// perndryshe lista e objekteve StationUsage kthehet ne json dhe cohet te service "DataCollectoinReceiver" nepermjet queue "stationDataResponse"
                    MessageHandler.sendMessage("stationDataResponse",gson.toJson(usageList.toArray(),StationUsage[].class));
            } catch (SQLException e) {
                System.out.println("Database Error!");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver Error!");
            }
        }
    }
}
