package gr.hua.dit.ds.Registry_Fan_clubs.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.engine.jdbc.BinaryStream;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.swing.text.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name="aitisi_gga")
public class AitisiGga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name ="status")
    private String status;

    @Column(name="result")
    private String result;

    @Column(name="number_members_lesxis")
    private int number_members_lesxis;

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


    //Mappings

    @ManyToOne(cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="lesxi_id_aitisigga")
    @JsonBackReference
    private Lesxi lesxi_id_aitisigga;


    //Constructor
    public AitisiGga() {
    }


    // getter setters//
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

    public Lesxi getLesxi_id_aitisigga() {
        return lesxi_id_aitisigga;
    }

    public void setLesxi_id_aitisigga(Lesxi lesxi_id_aitisigga) {
        this.lesxi_id_aitisigga = lesxi_id_aitisigga;
    }

    public int getNumber_members_lesxis() {
        return number_members_lesxis;
    }

    public void setNumber_members_lesxis(int number_members_lesxis) {
        this.number_members_lesxis = number_members_lesxis;
    }

    public String getName_lesxis() {
        return name_lesxis;
    }

    public void setName_lesxis(String name_lesxis) {
        this.name_lesxis = name_lesxis;
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

    public String getAM_commander() {
        return AM_commander;
    }

    public void setAM_commander(String AM_commander) {
        this.AM_commander = AM_commander;
    }




}
