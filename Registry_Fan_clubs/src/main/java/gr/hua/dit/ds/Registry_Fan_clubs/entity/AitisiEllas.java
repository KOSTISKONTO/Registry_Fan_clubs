package gr.hua.dit.ds.Registry_Fan_clubs.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="aitisiellas")
public class AitisiEllas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name="id_lesxis")
    private int id_lesxis;

    @Column(name ="status")
    private String status;

    @Column(name="result")
    private String result;
    @Column(name="name_lesxis")
    private String name_lesxis;

    @Column(name="AM_commander")
    @Size(min=5, max=6)
    @NotBlank(message = "please insert a valid Number Identity")
    private String AM_commander;

    @Column(name="Fan_commander_name")
    private String Fan_commander_name;

    @Column(name="Fan_commander_surname")
    private String Fan_commander_surname;

    @Column(name="Fan_commander_age")
    private int Fan_commander_age;

    @Column(name="document_certificate")
    private String document_certificate;

    @Column(name="Address")
    private String Address;

    @Column(name="number_address")
    private int number_address;

    @Column(name="location")
    private String location;

    @Column(name="city")
    private String city;

    @Column(name="TK")
    private int TK;


    //Constructors

    public AitisiEllas(int id, String status, String result, String name_lesxis, String AM_commander, String fan_commander_name, String fan_commander_surname, int fan_commander_age, String document_certificate, Lesxi lesxi_id_aitisiellas) {
        this.id = id;
        this.status = status;
        this.result = result;
        this.name_lesxis = name_lesxis;
        this.AM_commander = AM_commander;
        Fan_commander_name = fan_commander_name;
        Fan_commander_surname = fan_commander_surname;
        Fan_commander_age = fan_commander_age;
        this.document_certificate = document_certificate;
        this.lesxi_id_aitisiellas = lesxi_id_aitisiellas;
    }

    public AitisiEllas() {

    }



    //Mappings
    @ManyToOne(cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="lesxi_id_aitisiellas")
    @JsonBackReference
    private Lesxi lesxi_id_aitisiellas;




    //getter setters

    public int getId_lesxis() {
        return id_lesxis;
    }

    public void setId_lesxis(int id_lesxis) {
        this.id_lesxis = id_lesxis;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getNumber_address() {
        return number_address;
    }

    public void setNumber_address(int number_address) {
        this.number_address = number_address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTK() {
        return TK;
    }

    public void setTK(int TK) {
        this.TK = TK;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName_lesxis() {
        return name_lesxis;
    }

    public void setName_lesxis(String name_lesxis) {
        this.name_lesxis = name_lesxis;
    }

    public String getAM_commander() {
        return AM_commander;
    }

    public void setAM_commander(String AM_commander) {
        this.AM_commander = AM_commander;
    }

    public String getFan_commander_name() {
        return Fan_commander_name;
    }

    public void setFan_commander_name(String fan_commander_name) {
        Fan_commander_name = fan_commander_name;
    }

    public String getFan_commander_surname() {
        return Fan_commander_surname;
    }

    public void setFan_commander_surname(String fan_commander_surname) {
        Fan_commander_surname = fan_commander_surname;
    }

    public int getFan_commander_age() {
        return Fan_commander_age;
    }

    public void setFan_commander_age(int fan_commander_age) {
        Fan_commander_age = fan_commander_age;
    }

    public String getDocument_certificate() {
        return document_certificate;
    }

    public void setDocument_certificate(String document_certificate) {
        this.document_certificate = document_certificate;
    }

    public Lesxi getLesxi_id_aitisiellas() {
        return lesxi_id_aitisiellas;
    }

    public void setLesxi_id_aitisiellas(Lesxi lesxi_id_aitisiellas) {
        this.lesxi_id_aitisiellas = lesxi_id_aitisiellas;
    }
}
