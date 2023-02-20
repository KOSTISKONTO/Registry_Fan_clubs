package gr.hua.dit.ds.Registry_Fan_clubs.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="fan")
public class Fan {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="AM", unique=true, nullable=false)
    @Size (min=5, max=6)
    private String AM;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="age")
    private int age;

    //Ποινικό μητρώο σχετικό με οπαδικά παραπτώματα//
    @Column(name="poiniko_mitrwo")
    private boolean poiniko_mitrwo;

    @Column(name="username")
    private String username;

    @Column(name="iscommander")
    private boolean iscommander=false;
    


    //Mapings
    @ManyToOne(cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="lesxi_id")
    @JsonBackReference
    private Lesxi lesxi_id;


    //getter setter//
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAM() {
        return AM;
    }

    public void setAM(String AM) {
        this.AM = AM;
    }

    public boolean isPoiniko_mitrwo() {
        return poiniko_mitrwo;
    }

    public void setPoiniko_mitrwo(boolean poiniko_mitrwo) {
        this.poiniko_mitrwo = poiniko_mitrwo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }





    public boolean isIscommander() {
        return iscommander;
    }

    public void setIscommander(boolean iscommander) {
        this.iscommander = iscommander;
    }

    public Lesxi getLesxi_id() {
        return lesxi_id;
    }

    public void setLesxi_id(Lesxi lesxi_id) {
        this.lesxi_id = lesxi_id;
    }


}
