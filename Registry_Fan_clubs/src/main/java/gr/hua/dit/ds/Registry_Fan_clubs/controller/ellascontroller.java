package gr.hua.dit.ds.Registry_Fan_clubs.controller;


import gr.hua.dit.ds.Registry_Fan_clubs.dao.ellasdao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.fandao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.lesxidao;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiEllas;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(("/elasaitisi"))
public class ellascontroller {


    @Autowired
    private ellasdao ellasdao;

    @Autowired
    private lesxidao lesxidao;

    @Autowired
    private fandao fandao;


    /*
    Παίρνω αίτηση ΕΛΑΣ με id
     */
    @GetMapping("/getaitisi/{aid}")
    public AitisiEllas getaitisi(@PathVariable int aid)
    {
        return ellasdao.getAitisiellasbyid(aid);
    }


    /*
    Παίρνω τα ΑΜ των μελών της Λέσχης
     */
    @GetMapping("/getAmMembers/{lid}")
    public List<String> getAmMembers(@PathVariable int lid)
    {
        Lesxi alesxi=lesxidao.getlesxi(lid);
        List<String> Am_Members=new ArrayList<String>();
        int size=alesxi.getFans().size();
        for (int i=0; i<size; i++)
        {
            Am_Members.add(alesxi.getFans().get(i).getAM());
        }
        return Am_Members;
    }


    /*
    Παίρνω τις εκκρεμείς αιτήσεις ΕΛΑΣ
     */
    @GetMapping("/ekkremeis")
    public List<AitisiEllas> get_ekrremeis()
    {
        return ellasdao.aitisis_ekkremeis();
    }




    /*
    Επεξερασία αίτησης: Αν κάποιο μέλος της Λέσχης διαπιστωθεί ότι εχει ποινικό μητρώο, το poiniko mitrwo γίνεται true.
     */
    @PostMapping("/processaitisi/{Am}")
    public Fan processaitisi(@PathVariable String Am)
    {
        Fan afan=new Fan();
        List<Fan> fans=fandao.getFans();
        for (int i=0; i<fans.size(); i++)
        {
            if(fans.get(i).getAM().equals(Am))
            {
                afan=fandao.getfanbyid(fans.get((i)).getId());
                break;
            }
        }
        afan.setPoiniko_mitrwo(true);
        fandao.savefan(afan);
        return afan;

    }


    /*
    Τσεκάρω την αίτηση της ΕΛΑΣ
     */
    @PostMapping("check/{aid}")
    public AitisiEllas chechaitisi(@PathVariable int aid)
    {
        AitisiEllas aitisiEllas=ellasdao.getAitisiellasbyid(aid);
        int id_lesxis=aitisiEllas.getId_lesxis();
        Lesxi alesxi=lesxidao.getlesxi(id_lesxis);
        List<Fan> fans=alesxi.getFans();
        int size=fans.size();
        int i;
        for (i=0; i<size; i++)
        {
            if (fans.get(i).isPoiniko_mitrwo())
            {
                aitisiEllas.setStatus("oloklirothike");
                aitisiEllas.setResult("aporiptetai");
               break;
            }
        }
        if(i==size)
        {

            aitisiEllas.setStatus("oloklirothike");
            aitisiEllas.setResult("egkrithike");
            alesxi.setEnabled_store(true);
            alesxi.setAddress(aitisiEllas.getAddress());
            alesxi.setNumber_address(aitisiEllas.getNumber_address());
            alesxi.setLocation(aitisiEllas.getLocation());
            alesxi.setCity(aitisiEllas.getCity());
            alesxi.setTK(aitisiEllas.getTK());
            lesxidao.saveLesxi(alesxi);
        }
        ellasdao.saveAitisi(aitisiEllas);
        return aitisiEllas;
    }




}