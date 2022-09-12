package dataCollectionReceiver.database;

import dataCollectionReceiver.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//kjo klase lidhet me database
public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/disys";
    private static final String USER = "root";
    private static final String PASS = "";
    private static Connection con;

    //kjo metode ben lidhjen me databse
    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(URL,USER,PASS);
    }
    //kjo metode mbyll lidhjen me database
    private  static void disconnect() throws SQLException {
        con.close();
    }
    // kjo metode merr infot e nje customer nga database-i. Customer qe merret eshte ai me id te njejte me parametrin e metodes.
    public static Customer getCustomerInfo(int id) throws SQLException, ClassNotFoundException{
        Customer customer = new Customer();//objekti q do bohet return
        connect();
        String query = "SELECT * from customer where customer_id = ?"; // query q merr customer me customer_id specifik
        PreparedStatement stmt = con.prepareStatement(query);// ktu rijohet prepared statement
        stmt.setInt(1,id);// ktu vihet customer id si parameter
        ResultSet res = stmt.executeQuery();// ktu bohet run query dhe rezultati ruhet te "res"
        while (res.next()){// ketu inicializohet objekti customer q do bohet return
            customer = new Customer(// ketu merren atributet nga rezultati, me rradh sic jan t ruajtura n database
                    res.getInt(1), // kolona "customer_id"
                    res.getString(2), // kolona "fName"
                    res.getString(3), //kolona "lName"
                    res.getString(4), // kolona "address"
                    res.getInt(5), // kolona "zip"
                    res.getString(6) // kolona "country"
            );
        }
        disconnect();
        return customer;
    }
}
