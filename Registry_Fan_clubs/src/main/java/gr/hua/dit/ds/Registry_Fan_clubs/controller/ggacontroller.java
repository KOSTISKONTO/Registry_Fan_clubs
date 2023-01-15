package gr.hua.dit.ds.Registry_Fan_clubs.controller;


import gr.hua.dit.ds.Registry_Fan_clubs.dao.ggadao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.fandao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.lesxidao;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
@RestController
@RequestMapping("/aitisigga")
public class ggacontroller {

    @Autowired
    private ggadao ggadao;

    @Autowired
    private fandao fandao;

    @Autowired
    private lesxidao lesxidao;

    /*
    Εισάγω την νέα αίτηση με το id commander
     */
    @PostMapping("/newAitisi/{fid}")
    public AitisiGga newAitisi(@RequestBody AitisiGga aitisiGga, @PathVariable int fid) {

         //check if fan exists else throw//
        Fan afan = fandao.getfanbyid(fid);
        if(afan==null)
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "fan not found"
            );
        }
        else{
            List<Lesxi> lesxes=lesxidao.getLesxes();
            for (int i=0; i<lesxes.size(); i++)
            {

                if(lesxes.get(i).getId_commander()==afan.getId())
                {
                    if(!(lesxes.get(i).getName().equals(aitisiGga.getName_lesxis())))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fan is in other exist in other club");
                }
            }
        }




        //Τσεκάρω εάν υπάρχει ήδη η λέσχη και παίρνω σε θετική περίπτωση τις αιτήσεις που έκανε//
        boolean exist_lesxi=false;
        int id_lesxis=-1;
        String name_lesxis= aitisiGga.getName_lesxis();


        List<Lesxi> lesxes=lesxidao.getLesxes();

       List<AitisiGga> aitisis_lesxis=new ArrayList<>();
        Lesxi lesxi=new Lesxi();



        for (int i=0; i<lesxes.size(); i++)
        {
            if(lesxes.get(i).getName().equals(name_lesxis))
            {
                aitisis_lesxis=lesxes.get(i).getAitisisgga();
                lesxi=lesxidao.getlesxi(lesxes.get(i).getId());
                exist_lesxi=true;
                break;
            }

        }


        //Αν υπάρχει λέσχη και έχει ήδη κάνει αίτηση για έγκριση λέσχης, η οποία εκκρεμεί ή εκρίθηκε ή απορρίφθηκε//
        if(exist_lesxi==true)
        {
            for (int i=0; i<aitisis_lesxis.size(); i++)
            {
                //αν εκκρεμεί
                if(aitisis_lesxis.get(i).getResult().equals("pending_inspection")||aitisis_lesxis.get(i).getResult().equals("egkrithike"))
                {
                    throw new ResponseStatusException(
                            HttpStatus.ALREADY_REPORTED, "aitisi exist and is pending_inspection"
                    );
                }
                //αν εγκρίθηκε
                if(aitisis_lesxis.get(i).getResult().equals("approved"))
                {
                    throw new ResponseStatusException(
                            HttpStatus.ACCEPTED, "aitisi exist and is aproved"
                    );
                }
            }

            //αν απορρίφθηκε  δημιουργεί νέα αίτηση
            if(aitisiGga.getNumber_members_lesxis()<4)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Members must be >=4");
            }
            ggadao.newAitisi_in_Lesxi(aitisiGga);
            return aitisiGga;

        }


        //Αν δεν υπάρχει η λέσχη//
        if(aitisiGga.getNumber_members_lesxis()<4)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Members must be >=4");
        }
        Lesxi alesxi = new Lesxi();
        aitisiGga.setAM_commander(afan.getAM());
        aitisiGga.setFan_commander_name(afan.getName());
        aitisiGga.setFan_commander_surname(afan.getSurname());
        aitisiGga.setFan_commander_age(afan.getAge());
        ggadao.newAitisigga(aitisiGga);


       List<AitisiGga> aitisis=ggadao.aitisis();
       int id_aitisis=0;
       for (int i=0; i<aitisis.size(); i++ )
       {
           if(aitisis.get(i).getName_lesxis()== aitisiGga.getName_lesxis())
           {
               id_aitisis=aitisis.get(i).getId();
               break;
           }
       }
       AitisiGga newaitisi=ggadao.getAitisiggabyid(id_aitisis);


       alesxi.setName(newaitisi.getName_lesxis());
       alesxi.setId_commander(afan.getId());
       alesxi.addFan(afan);
       alesxi.addaitisigga(newaitisi);
       alesxi.setEnabled(false);
       lesxidao.saveLesxi(alesxi);
       return newaitisi;

    }

    /*
    Τσεκάρω την αίτηση
     */
    @PostMapping("/checkaitisi/{aid}")
    public  AitisiGga checkaitisi(@PathVariable int aid) {
        AitisiGga aitisi = ggadao.getAitisiggabyid(aid);
        Lesxi lesxi = lesxidao.getLesxi_by_id_aitisis(aid);

        if (aitisi.getStatus().equals("created") && aitisi.getResult().equals("pending_inspection")) {

                    aitisi.setResult("egkrithike");
                    aitisi.setStatus("oloklirothike");
                    lesxi.setEnabled(true);
        }

        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ekrremei akoma"
            );
        }
        aitisi.setLesxi_id_aitisigga(lesxi);
        ggadao.saveAitisi(aitisi);
        return aitisi;


    }

    /*
    Παίρνω την αίτηση
     */
    @GetMapping("/getAitisi/{aid}")
    public AitisiGga getAitisi(@PathVariable int aid)
    {
        AitisiGga aitisiGga=ggadao.getAitisiggabyid(aid);
        return aitisiGga;
    }


}






