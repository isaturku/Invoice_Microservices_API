package stationDataCollector.model;

import java.sql.Timestamp;

public class StationUsage {
    private int id;
    private int idStation;
    private int  idCustomer;
    private int kwh;
    private Timestamp datetime;

    public StationUsage(int id, int idStation, int idCustomer, int kwh, Timestamp datetime) {
        this.id = id;
        this.idStation = idStation;
        this.idCustomer = idCustomer;
        this.kwh = kwh;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public int getIdStation() {
        return idStation;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public int getKwh() {
        return kwh;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public void setKwh(int kwh) {
        this.kwh = kwh;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }
}
