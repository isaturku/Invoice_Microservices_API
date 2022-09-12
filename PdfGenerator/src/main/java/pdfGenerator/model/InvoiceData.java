package pdfGenerator.model;

import java.sql.Timestamp;
import java.util.List;

public class InvoiceData {
    private int customerID;
    private String fName;
    private String lName;
    private String address;
    private int zip;
    private String country;
    private List<Integer> stationIDs;
    private List<Integer> kwhs;
    private List<Timestamp> datetimes;

    public InvoiceData() {
    }
    public InvoiceData(int customerID,String fName, String lName, String address, int zip, String country, List<Integer> stationIDs, List<Integer> kwhs, List<Timestamp> datetimes) {
        this.customerID = customerID;
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.zip = zip;
        this.country = country;
        this.stationIDs = stationIDs;
        this.kwhs = kwhs;
        this.datetimes = datetimes;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getAddress() {
        return address;
    }

    public int getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public List<Integer> getStationIDs() {
        return stationIDs;
    }

    public List<Integer> getKwhs() {
        return kwhs;
    }

    public List<Timestamp> getDatetimes() {
        return datetimes;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setStationIDs(List<Integer> stationIDs) {
        this.stationIDs = stationIDs;
    }

    public void setKwhs(List<Integer> kwhs) {
        this.kwhs = kwhs;
    }

    public void setDatetimes(List<Timestamp> datetimes) {
        this.datetimes = datetimes;
    }
}

