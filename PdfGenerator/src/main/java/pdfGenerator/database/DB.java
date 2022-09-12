package pdfGenerator.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//kjo klase lidhet me databsae
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
    //metoda e meposhtme shton nje rresht te ri te tabela "invoice_log" e database. Nje rresht i ri shtohet sa here nje invoice i ri krijohet per nje user
    // ne tabele ruhet id i customer dhe emr i file.
    public static void logInvoice(int customerID, String docName) throws SQLException, ClassNotFoundException {
        connect();
        String sql = "insert into invoice_log (customer_id, pdf_name) values(?,?)"; // query q shton nje rrjesht te tabela "invoice_log"
        PreparedStatement stmt =  con.prepareStatement(sql);
        stmt.setInt(1,customerID);
        stmt.setString(2,docName);
        stmt.execute();
        disconnect();
    }
}
