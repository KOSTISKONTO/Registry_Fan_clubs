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
import  java.util.List;
@RestController
@RequestMapping("/lesxi")
public class lesxicontroller {

    @Autowired
    lesxidao lesxidao;

    @Autowired
    private fandao fandao;

    @Autowired
    private ellasdao ellasdao;

        //getlesxi by id//
    @GetMapping("/{lid}")
    public Lesxi getLesxi(@PathVariable int lid)
    {
        return lesxidao.getlesxi(lid);
    }


    //delete lesxi by id//
    @DeleteMapping("/{lid}")
    public void deleteLesxi(@PathVariable int lid)
    {

        lesxidao.deleteLesxi(lid);
    }


/*
Δημιουργώ νέα αίτηση ΕΛΑΣ
 */
    @PostMapping("/newEllasAitisi/{lid}")
    public AitisiEllas newAitisiEllas(@RequestBody AitisiEllas aitisiEllas, @PathVariable int lid)
    {
        Lesxi alesxi=lesxidao.getlesxi(lid);
        if(!(alesxi.isEnabled()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lesxi is not enabled");
        }
        Fan afan=fandao.getfanbyid(alesxi.getId_commander());



        List<AitisiEllas> aitisis_lesxis=alesxi.getAitisisellas();
        int size_aitisis_lesxis=aitisis_lesxis.size();
        if(size_aitisis_lesxis>0) {
            AitisiEllas aitisiEllas_last = aitisis_lesxis.get(size_aitisis_lesxis - 1);

            if (aitisiEllas_last.getResult().equals("pending_inspection") || aitisiEllas_last.getResult().equals("egkrithike")) {
                throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "aitisi exist or completed and egkrithike");
            }

        }
        //fix this: Πρέπει να γίνει έλεγχος αν δοθούν ακριβώς ίδια στοιχεία διεύθυνσης με άλλη λέσχη.
        aitisiEllas.setId(0);
        aitisiEllas.setId_lesxis(alesxi.getId());
        aitisiEllas.setAM_commander(afan.getAM());
        aitisiEllas.setFan_commander_name(afan.getName());
        aitisiEllas.setFan_commander_surname(afan.getSurname());
        aitisiEllas.setFan_commander_age(afan.getAge());
        aitisiEllas.setName_lesxis(alesxi.getName());
       ellasdao.newAitisiEllas(aitisiEllas);

       List<AitisiEllas> aitisis=ellasdao.aitisis();
       int id_aitisis=0;
       for (int i=0; i<aitisis.size(); i++)
       {
           if(aitisis.get(i).getName_lesxis().equals(alesxi.getName()))
           {
               id_aitisis=aitisis.get(i).getId();
           }
       }

       AitisiEllas newaitisiellas=ellasdao.getAitisiellasbyid(id_aitisis);
       alesxi.addaitisiellas(newaitisiellas);
       lesxidao.saveLesxi(alesxi);
       return newaitisiellas;




    }


}

