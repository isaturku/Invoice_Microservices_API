package stationDataCollector.database;

import stationDataCollector.model.StationUsage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/disys";
    private static final String USER = "root";
    private static final String PASS = "";
    private static Connection con;

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(URL,USER,PASS);
    }

    private  static void disconnect() throws SQLException {
        con.close();
    }
    //kjo metode mrr nga database-i perdorimet e nje stacioni specifik nga nje customer specifik, ku accountedFor eshte 0/false
    // , qe dmth se te dhenat e perdorimit nuk jane llogaritur me perpara.
    public static List<StationUsage> getStationDataForCustomer(int customerID,int stationID) throws SQLException, ClassNotFoundException {
        List<StationUsage> result = new ArrayList<StationUsage>(); // liste objektesh StationUsage ku do te futet rezultati i query-t te meposhtem
        List<Integer> ids = new ArrayList<Integer>(); // liste id-sh qe do te mbushet me id-te e gjithe rreshtave qe vijne si rezultat nga query i meposhtem
        // dhe do te perdoren si parameter per metoden e meposhtme logUsedData()
        connect();
        String query = "SELECT * from station_usage where customer_id = ? and station_id = ? and accountedFor = 0";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1,customerID);
        stmt.setInt(2,stationID);
        ResultSet res = stmt.executeQuery();
        if(!res.next())
            return new ArrayList<>();
        else {
            do{
                int id = res.getInt(1);
                ids.add(id); //ketu futen id-te e rreshtave te lista "ids"
                result.add(new StationUsage(
                        id,
                        res.getInt(2),
                        res.getInt(3),
                        res.getInt(4),
                        res.getTimestamp(5)
                ));
            }
            while (res.next());
        }
        logUsedData(ids);//thirrja e metodes logUsedData() me parameter listen "ids"
        disconnect();
        return result; // return i listes se objekete StatoinUsage
    }
    //kjo metode i ben update tabeles station_usage duke e kthyer accountedFor ne 1/true.
    // Ne kete menyre rreshtat e perdorur nje here nuk perdoren per here te dyte.
    // Rreshtat qe ndryshohen kane id brenda te lista ids qe jepet si parameter i metodes.
    private static void logUsedData(List<Integer> ids) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("?, ".repeat(ids.size()));
        String placeholders = builder.deleteCharAt(builder.length()-2).toString();
        String sql = "UPDATE station_usage set accountedFor = 1 where customer_id in ("+placeholders+");";
        PreparedStatement stmt = con.prepareStatement(sql);
        int i = 1;
        for (int id : ids)
            stmt.setInt(i++,id);
        stmt.execute();
    }
}
