package dataCollectionReceiver.model;

public class Customer {
    private int id;
    private String fName;
    private String lName;
    private String address;
    private int zip;
    private String country;

    public Customer(int id, String fName, String lName, String address, int zip, String country) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.zip = zip;
        this.country = country;
    }

    public Customer() {
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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
}
