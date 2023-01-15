package gr.hua.dit.ds.Registry_Fan_clubs.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="lesxi")
public class Lesxi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name="name", unique = true)
    private String name;


    @Column(name="id_commander", unique = true)
    private int id_commander;

    @Column(name="Enabled")
    private boolean enabled;

    @Column(name="Enabled_Store")
    private boolean enabled_store;


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

    

    //Mappings

    //fans
    @OneToMany(mappedBy="lesxi_id", cascade=CascadeType.ALL)
    @JsonManagedReference
    private List<Fan> fans;

    //aitisigga
    @OneToMany(mappedBy="lesxi_id_aitisigga", cascade={CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    private List<AitisiGga> aitisisgga;

    //aitisiellas
    @OneToMany(mappedBy="lesxi_id_aitisiellas", cascade={CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    private List<AitisiEllas> aitisisellas;



    //Constructors//
    public Lesxi() {

    }

    public Lesxi(int id, String name, String status, String result, int id_commander, List<Fan> fans) {
        this.id = id;
        this.name = name;
        this.id_commander = id_commander;
        this.fans = fans;
    }

    //ends constuctors//



    // getter/setter//

    public boolean isEnabled_store() {
        return enabled_store;
    }

    public void setEnabled_store(boolean enabled_store) {
        this.enabled_store = enabled_store;
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

    public List<AitisiEllas> getAitisisellas() {
        return aitisisellas;
    }

    public void setAitisisellas(List<AitisiEllas> aitisisellas) {
        this.aitisisellas = aitisisellas;
    }

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


    public int getId_commander() {
        return id_commander;
    }

    public void setId_commander(int id_commander) {
        this.id_commander = id_commander;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public List<Fan> getFans() {
        return fans;
    }

    public void setFans(List<Fan> fans) {
        this.fans = fans;
    }

    public List<AitisiGga> getAitisisgga() {
        return aitisisgga;
    }

    public void setAitisisgga(List<AitisiGga> aitisisgga) {
        this.aitisisgga = aitisisgga;
    }




    //Other methods//

    //add Fans//
    public void addFan(Fan afan)
    {
        if(fans==null)
        {
            fans=new ArrayList<Fan>();
        }
        fans.add(afan);
        afan.setLesxi_id(this);
    }

    //add commander//
    public  void addCommander(int id_fan)
    {
        boolean result=false;
        if(this.fans!=null) {
            for (int i = 0; i <fans.size(); i++)
            {
                if(fans.get(i).getId()==id_fan)
                {
                    setId_commander(id_fan);
                }
            }

        }
    }

    //addaitisigga
    public void addaitisigga(AitisiGga aitisiGga)
    {
        if(aitisisgga==null)
        {
            aitisisgga=new ArrayList<>();
        }
        aitisisgga.add(aitisiGga);
        aitisiGga.setLesxi_id_aitisigga(this);

    }

    //addaitisiellas

    public void addaitisiellas(AitisiEllas aitisiEllas)
    {
        if(aitisisellas==null)
        {
            aitisisellas=new ArrayList<>();
        }
        aitisisellas.add(aitisiEllas);
        aitisiEllas.setLesxi_id_aitisiellas(this);
    }


}
