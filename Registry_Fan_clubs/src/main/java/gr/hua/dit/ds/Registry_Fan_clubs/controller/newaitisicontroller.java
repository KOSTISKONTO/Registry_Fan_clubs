package gr.hua.dit.ds.Registry_Fan_clubs.controller;

import gr.hua.dit.ds.Registry_Fan_clubs.dao.fandao;

import gr.hua.dit.ds.Registry_Fan_clubs.dao.ggadao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.lesxidao;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;



@Controller
public class newaitisicontroller {
    @Autowired
    private fandao fandao;
    @Autowired
    private ggadao ggadao;
    @Autowired
    private lesxidao lesxidao;

    @GetMapping("/newaitisigga")
    public String showGGAForm(Model model) {


        Fan afan = fandao.getfanbyuser();

        if (afan == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ΔΕΝ ΕΧΕΙΣ ΕΙΣΑΓΕΙ ΤΑ ΣΤΟΧΕΙΑ ΣΟΥ ΩΣ ΑΡΧΗΓΟΣ ΛΕΣΧΗΣ"
            );
        }
        else {
                AitisiGga aitisiGga = ggadao.getGgabyAmcommander(afan.getAM());
                if(aitisiGga!=null) {
                    if (aitisiGga.getResult().equals("pending_inspection") || aitisiGga.getResult().equals("egkrithike")) {
                        throw new ResponseStatusException(
                                HttpStatus.ALREADY_REPORTED, "aitisi exist and is pending_inspection"
                        );
                    }
                }

            AitisiGga aitisigga = new AitisiGga();
            model.addAttribute("aitisigga", aitisigga);
            return "newaitisigga";
            }
    }

    @PostMapping(path = "/newaitisigga")
    public String newaitisigga(@ModelAttribute("aitisigga") AitisiGga aitisigga, Model model) {



        Fan afan=fandao.getfanbyuser();
        //Τσεκάρω εάν υπάρχει ήδη η λέσχη και παίρνω σε θετική περίπτωση τις αιτήσεις που έκανε//
        boolean exist_lesxi=false;
        int id_lesxis=-1;
        String name_lesxis= aitisigga.getName_lesxis();


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
                if(aitisis_lesxis.get(i).getResult().equals("pending_inspection"))
                {
                    throw new ResponseStatusException(
                            HttpStatus.ALREADY_REPORTED, "aitisi exist and is pending_inspection"
                    );
                }
                //αν εγκρίθηκε
                if(aitisis_lesxis.get(i).getResult().equals("egkrithike"))
                {
                    throw new ResponseStatusException(
                            HttpStatus.ACCEPTED, "aitisi exist and is aproved"
                    );
                }
            }

            //αν απορρίφθηκε  δημιουργεί νέα αίτηση
            if(aitisigga.getNumber_members_lesxis()<4)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Members must be >=4");
            }
            ggadao.newAitisi_in_Lesxi(aitisigga);
            return "redirect:/";

        }


        //Αν δεν υπάρχει η λέσχη//
        if(aitisigga.getNumber_members_lesxis()<4)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Members must be >=4");
        }
        Lesxi alesxi = new Lesxi();
        aitisigga.setAM_commander(afan.getAM());
        aitisigga.setFan_commander_name(afan.getName());
        aitisigga.setFan_commander_surname(afan.getSurname());
        aitisigga.setFan_commander_age(afan.getAge());
        ggadao.newAitisigga(aitisigga);


        List<AitisiGga> aitisis=ggadao.aitisis();
        int id_aitisis=0;
        for (int i=0; i<aitisis.size(); i++ )
        {
            if(aitisis.get(i).getName_lesxis()== aitisigga.getName_lesxis())
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
        fandao.savefan(afan);
        Lesxi aalesxi=lesxidao.getLesxi_by_name_aitisis(aitisigga.getName_lesxis());
        model.addAttribute("lesxi", aalesxi);
        return "craitisigga";
    }



    @GetMapping("/form")
    public String form(){

        Fan afan=fandao.getfanbyuser();
        if (afan==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ΔΕΝ ΕΧΕΙΣ ΚΑΤΑΧΩΡΗΘΕΙ ΩΣ ΑΡΧΗΓΟΣ ΛΕΣΧΗΣ");
        }
        Lesxi alesxi=lesxidao.getLesxibyidcommander(afan.getId());
        if(alesxi==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Δεν έχεις δημιουργήσει Αίτηση για ΓΓΑ");

        }
        return "form";
    }

    @RequestMapping(value="anewfan", method={RequestMethod.GET, RequestMethod.POST})
    public String anewfan(@RequestParam String name,
                          @RequestParam String surname, @RequestParam int age,
                          @RequestParam String am, Model model)
    {

        System.out.println("request");
        Fan afan=fandao.getfanbyuser();
        Lesxi lesxi=lesxidao.getLesxibyidcommander(afan.getId());
        //Lesxi lesxi=lesxidao.getLesxi_by_name_aitisis(club_name);
        Fan fan=new Fan();
        fan.setName(name);
        fan.setSurname(surname);
        fan.setAge(age);
        fan.setAM(am);
        List<Fan> fans=lesxi.getFans();
        List<AitisiGga> aitisis_lesxis=lesxi.getAitisisgga();
        int size= aitisis_lesxis.size();
        int id_aitisis= aitisis_lesxis.get(size-1).getId();
        AitisiGga aitisiGga=ggadao.getAitisiggabyid(id_aitisis);
        int declare_members=aitisiGga.getNumber_members_lesxis();
        if(fans.size()==declare_members)
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "gemato"
            );
        }
        else{
            if(fan.getAge()<18)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "age of fan, must be >18");
            }

            fan.setId(0);
            lesxi.addFan(fan);
            fandao.savefan(fan);
            if(lesxi.getFans().size()==declare_members)
            {
                aitisiGga.setStatus("created");
                aitisiGga.setLesxi_id_aitisigga(lesxi);
                lesxidao.saveLesxi(lesxi);
            }
            model.addAttribute("name", fan.getName());
            model.addAttribute("surname",fan.getSurname());
            model.addAttribute("am", fan.getAM());
            return  "succesneefan";
        }

    }


    @GetMapping("/checkgga")
    public String ggaform(){

        return "checkgga";
    }

    @RequestMapping(value="checkgga", method= RequestMethod.POST)
    public String anewfan(@RequestParam int aid)
    {
        AitisiGga aitisi = ggadao.getAitisiggabyid(aid);
        Lesxi lesxi = lesxidao.getLesxi_by_id_aitisis(aid);

        if (aitisi.getStatus().equals("created") && aitisi.getResult().equals("pending_inspection")) {

            aitisi.setResult("egkrithike");
            aitisi.setStatus("oloklirothike");
            lesxi.setEnabled(true);
        }
        else if(aitisi.getStatus().equals("oloklirothike"))
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "oloklirothike"
            );
        }

        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ekrremei akoma"
            );
        }
        aitisi.setLesxi_id_aitisigga(lesxi);
        ggadao.saveAitisi(aitisi);
        return "redirect:/";
    }

    @GetMapping("/ekkremeisgga")
    public String getekkremmeisgga(Model model)
    {
        List<AitisiGga> ekkremeis=ggadao.getekkremeis();
        if(ekkremeis.isEmpty())
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Δεν υπάρχουν εκκρεμείς αιτήσεις"
            );
        }
        model.addAttribute("ekkremeis", ekkremeis);
        return "ekkremeisgga";
    }


    @GetMapping("/getlesxi")
    public String getlesxi(Model model)
    {
        return "getlesxi";
    }

    @RequestMapping(value="getlesxiresult", method= RequestMethod.POST)
    public String getlesxiresult(@RequestParam int lid, Model model)
    {

        Lesxi alesxi=lesxidao.getlesxi(lid);
        if(alesxi==null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ΔΕΝ ΥΠΑΡΧΕΙ Η ΛΕΣΧΗ");
        }

        if(alesxi.isEnabled())
        {
            model.addAttribute("id", alesxi.getId());
            model.addAttribute("name", alesxi.getName());
            model.addAttribute("idcommander", alesxi.getId_commander());
            List<Fan> fans=alesxi.getFans();
            model.addAttribute("fans", fans);
            if(alesxi.isEnabled_store())
            {
                model.addAttribute("Address", alesxi.getAddress());
                model.addAttribute("number", alesxi.getNumber_address());
                model.addAttribute("location", alesxi.getLocation());
                model.addAttribute("city", alesxi.getCity());
                model.addAttribute("TK", alesxi.getTK());
            }
            else {
                model.addAttribute("Address", "not enabled");
                model.addAttribute("number", "not enabled");
                model.addAttribute("location","not enabled");
                model.addAttribute("city", "not enabled");
                model.addAttribute("TK", "not enabled");
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "YΠΟ ΔΗΜΙΟΥΡΓΙΑ");
        }


        return "lesxi";
    }


    @GetMapping("/stoixeialesxis")
    public String deletefan(@RequestParam int aitisiid, Model model)
    {
        Lesxi lesxi=lesxidao.getLesxi_by_id_aitisis(aitisiid);
        List<Fan> fans=lesxi.getFans();
        model.addAttribute("fans", fans);
        return "stoixeialesxis";
    }
}



