package datacollectiondispatcher.invoice.model;

import javax.persistence.*;

//kjo klase perfaqeson nje rrjesht te tabeles station.
@Entity //annotation q trg se kjo klase perfaqeson nje rrjest te nje tabele
@Table // annotation q trg se kjo klase lidhet me nje klase ne database
public class Station {
    @Id // ky annotation trg q attributi posht esht primary key i tabeles
    private int station_id;
    private boolean available;
    private float latitude;
    private float longitude;

    public Station(int id, boolean available, float latitude, float longitude) {
        this.station_id = id;
        this.available = available;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Station() {

    }

    public int getId() {
        return station_id;
    }

    public boolean isAvailable() {
        return available;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.station_id = id;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
