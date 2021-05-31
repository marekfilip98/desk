package sample.entities;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


public class Reservation {
    private int id;
    private String jmeno_prijmeni;
    private Date datum;
    private LocalTime cas;
    private String spz;
    private int id_pracovnik;
    private String zavada;

    public Reservation(Date datum, LocalTime cas, String spz, String zavada, String jmeno_prijmeni) {
        this.datum = datum;
        this.cas = cas;
        this.spz = spz;
        this.zavada = zavada;
        this.jmeno_prijmeni = jmeno_prijmeni;
    }

    public Reservation(Date datum, LocalTime cas, String spz,int id_pracovnik, String zavada, String jmeno_prijmeni) {
        this.datum = datum;
        this.cas = cas;
        this.spz = spz;
        this.id_pracovnik = id_pracovnik;
        this.zavada = zavada;
        this.jmeno_prijmeni = jmeno_prijmeni;
    }

    public Reservation(int id, Date datum, LocalTime cas, String spz,String zavada, String jmeno_prijmeni) {
        this.id = id;
        this.datum = datum;
        this.cas = cas;
        this.spz = spz;
        this.zavada = zavada;
        this.jmeno_prijmeni = jmeno_prijmeni;
    }

    public String getZavada() {
        return zavada;
    }

    public void setZavada(String zavada) {
        this.zavada = zavada;
    }

    public String getJmeno_prijmeni() {
        return jmeno_prijmeni;
    }

    public void setJmeno_prijmeni(String jmeno_prijmeni) {
        this.jmeno_prijmeni = jmeno_prijmeni;
    }

    public String getSpz() {
        return spz;
    }

    public void setSpz(String spz) {
        this.spz = spz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public LocalTime getCas() {
        return cas;
    }

    public void setCas(LocalTime cas) {
        this.cas = cas;
    }

    public int getId_pracovnik() {
        return id_pracovnik;
    }

    public void setId_pracovnik(int id_pracovnik) {
        this.id_pracovnik = id_pracovnik;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", datum=" + datum +
                ", cas=" + cas +
                ", spz='" + spz + '\'' +
                '}';
    }
}
