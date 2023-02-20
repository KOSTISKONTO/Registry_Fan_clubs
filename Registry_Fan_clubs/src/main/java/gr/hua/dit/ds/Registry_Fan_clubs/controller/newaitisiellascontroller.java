package gr.hua.dit.ds.Registry_Fan_clubs.controller;

import gr.hua.dit.ds.Registry_Fan_clubs.dao.ellasdao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.fandao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.lesxidao;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiEllas;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
public class newaitisiellascontroller {

@Autowired
private lesxidao lesxidao;
@Autowired
private fandao fandao;
    @Autowired
    private ellasdao ellasdao;


    @GetMapping("/formaitisiellas")
    public String form(){
        Fan fan=fandao.getfanbyuser();
        if(fan==null)
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ΔΕΝ ΕΧΕΙΣ ΕΙΣΑΓΕΙ ΤΑ ΣΤΟΧΕΙΑ ΣΟΥ ΩΣ ΑΡΧΗΓΟΣ ΛΕΣΧΗΣ"
            );
        }

        Lesxi alesxi=lesxidao.getLesxibyidcommander(fan.getId());
        if(alesxi==null)
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ΔΕΝ ΕΧΕΙΣ ΔΗΜΙΟΥΡΓΗΣΕΙ ΑΙΤΗΣΗ ΓΙΑ ΛΕΣΧΗ"
            );
        }
        if(!(alesxi.isEnabled()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lesxi is not enabled");
        }
        return "aitisiellas";
    }

    @RequestMapping(value="ellasaitisi", method={RequestMethod.GET, RequestMethod.POST})
    public String anewfan(@RequestParam int club_id, @RequestParam String address,
                          @RequestParam int number_address, @RequestParam int tk,
                          @RequestParam String location,  @RequestParam String city, Model model)
    {
        Lesxi alesxi=lesxidao.getlesxi(club_id);
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
        AitisiEllas aitisiEllas=new AitisiEllas();
        //fix this: Πρέπει να γίνει έλεγχος αν δοθούν ακριβώς ίδια στοιχεία διεύθυνσης με άλλη λέσχη.
        aitisiEllas.setId(0);
        aitisiEllas.setAddress(address);
        aitisiEllas.setNumber_address(number_address);
        aitisiEllas.setLocation(location);
        aitisiEllas.setTK(tk);
        aitisiEllas.setCity(city);

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
        return "redirect:/";

    }


    @GetMapping("/checkellas")
    public String checkellas(){
        return "checkellas";
    }


    @RequestMapping(value="checkellasaitisi", method={RequestMethod.GET, RequestMethod.POST})
    public String checkellas(@RequestParam int id_aitisis, Model model)
    {

        int id=-1;
        List<AitisiEllas> aitisis= ellasdao.aitisis();
        int j;
        for (j=0; j<aitisis.size(); j++)
        {
            if(id_aitisis==aitisis.get(j).getId())
            {
                id=id_aitisis;
                break;
            }
        }
        if(j==aitisis.size())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Η αίτηση δεν υπάρχει");
        }
        AitisiEllas aitisiEllas=ellasdao.getAitisiellasbyid(id);


        if (aitisiEllas.getStatus().equals("oloklirothike"))
        {

                throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED,
                        "Η αίτηση έχει ολοκληρωθεί και το αποτέλεσμα είναι " + aitisiEllas.getResult());

        }
        int id_lesxis=aitisiEllas.getId_lesxis();
        Lesxi alesxi=lesxidao.getlesxi(id_lesxis);

        //
        List<Fan> Am_Members=alesxi.getFans();

        model.addAttribute("fans", Am_Members);
        model.addAttribute("id", id_aitisis);
        return "proellas";




        /*
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
        return "redirect:/";

         */
    }


    @GetMapping("/proccesselas")
    public String proccesselas(@RequestParam String am)
    {
        Fan afan=fandao.getfanbyam(am);
        afan.setPoiniko_mitrwo(true);
        fandao.savefan(afan);
        return "redirect:/";
    }

    @GetMapping("/result")
    public String resultElas(@RequestParam int id)
    {
        AitisiEllas aitisiEllas=ellasdao.getAitisiellasbyid(id);
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
        return "redirect:/";

    }

    @GetMapping("/elascreated")
    public String ekkremeis(Model model)
    {
        List<AitisiEllas> ekkremeis=ellasdao.aitisis_ekkremeis();
        model.addAttribute("ekkremmeis", ekkremeis);
        return "ekkremeis";
    }




}
