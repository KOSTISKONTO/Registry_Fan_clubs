package gr.hua.dit.ds.Registry_Fan_clubs.controller;

import gr.hua.dit.ds.Registry_Fan_clubs.dao.ellasdao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.fandao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.ggadao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.lesxidao;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiEllas;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
public class fanformcontroller {
    @Autowired
    private fandao fandao;
    @Autowired
    private lesxidao lesxidao;
    @Autowired
    private ggadao ggadao;
    @Autowired
    private ellasdao ellasdao;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/fans")
    public String fancommand(Model model)
    {
        List<Fan> fans = fandao.getFans();
        model.addAttribute("fans", fans);
        return "fans";
    }




    @GetMapping("/addfancommand")
    public String showfancommandform(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        Fan afan=fandao.getfanbyuser();
        if(afan!=null) {
            if (afan.isIscommander()) {
                throw new ResponseStatusException(
                        HttpStatus.ALREADY_REPORTED, "Έχετε ήδη δηλωθεί ως Αρχηγός Λέσχης!"
                );

            }
        }
        Fan fan=new Fan();
        model.addAttribute("fan", fan);
        return "addfancommand";
    }





    @RequestMapping(value="formfancommand", method={RequestMethod.GET, RequestMethod.POST})
    public String anewfan( @RequestParam String name,
                          @RequestParam String surname, @RequestParam int age,
                          @RequestParam String AM, Model model)

    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        Fan fan=new Fan();
        fan.setName(name);
        fan.setSurname(surname);
        fan.setAge(age);
        fan.setAM(AM);
        fan.setUsername(username);
        fan.setIscommander(true);
        fan.setId(0);
        fandao.savefan(fan);
        model.addAttribute("name", fan.getName());
        model.addAttribute("surname", fan.getSurname());
        model.addAttribute("age", fan.getAge());
        model.addAttribute("AM", fan.getAM());
        model.addAttribute("id", fan.getId());
        return "fancommand";

    }


    @GetMapping("/deletefan")
    public String deletefan(@RequestParam int fanid)
    {
        Fan afan=fandao.getfanbyid(fanid);
        Lesxi alesxi=lesxidao.getlesxi(afan.getLesxi_id().getId());

        if(afan.getId()==alesxi.getId_commander())
        {
                        throw new ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE, "Ο Αρχηγός δεν μπορεί να διαγραφεί"
                );

        }
        fandao.deletefan(fanid);
        if (alesxi.getFans().size()<4)
        {
            alesxi.setEnabled(false);
            lesxidao.saveLesxi(alesxi);
        }

        return "redirect:/fans";
    }


    @GetMapping("/resultaitisigga")
    public String resultgga(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        Fan afan=fandao.getfanbyuser();
        if (afan==null)
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ΔΕΝ ΕΧΕΤΕ ΔΗΛΩΘΕΙ ΩΣ ΑΡΧΗΓΟΣ ΛΕΣΧΗΣ ΚΑΙ ΔΕΝ ΕΧΕΤΕ ΔΗΜΙΟΥΡΓΗΣΕΙ ΑΙΤΗΣΗ"
            );
        }
        List<AitisiGga> aitisis=new ArrayList<>();
        List<AitisiGga> existaitisis=ggadao.aitisis();
        for (int i=0; i<existaitisis.size(); i++)
        {
            if (existaitisis.get(i).getAM_commander().equals(afan.getAM()))
            {
                aitisis.add(existaitisis.get(i));
            }
        }

       if(aitisis.isEmpty())
       {
           throw new ResponseStatusException(
                   HttpStatus.BAD_REQUEST, "ΔΕΝ ΕΧΕΤΕ ΔΗΜΙΟΥΡΓΗΣΕΙ ΑΙΤΗΣΗ"
           );
       }
       model.addAttribute("aitisis", aitisis);
       return "aitisigga";

    }


    @GetMapping("/resultaitisielas")
    public String resultelas(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        Fan afan=fandao.getfanbyuser();
        if (afan==null)
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ΔΕΝ ΕΧΕΤΕ ΔΗΛΩΘΕΙ ΩΣ ΑΡΧΗΓΟΣ ΛΕΣΧΗΣ ΚΑΙ ΔΕΝ ΕΧΕΤΕ ΔΗΜΙΟΥΡΓΗΣΕΙ ΑΙΤΗΣΗ"
            );
        }
        List<AitisiEllas> aitisis=new ArrayList<>();
        List<AitisiEllas> existaitisis= ellasdao.aitisis();
        for (int i=0; i<existaitisis.size(); i++)
        {
            if (existaitisis.get(i).getAM_commander().equals(afan.getAM()))
            {
                aitisis.add(existaitisis.get(i));
            }
        }

        if(aitisis.isEmpty())
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ΔΕΝ ΕΧΕΤΕ ΔΗΜΙΟΥΡΓΗΣΕΙ ΑΙΤΗΣΗ"
            );
        }
        model.addAttribute("aitisis", aitisis);
        return "aitisiellasresult";

    }




}


