package gr.hua.dit.ds.Registry_Fan_clubs.controller;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.fandao;


import gr.hua.dit.ds.Registry_Fan_clubs.dao.ggadao;
import gr.hua.dit.ds.Registry_Fan_clubs.dao.lesxidao;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(value="/fan", headers="Accept=application/json")

public class fancontroller {
    @Autowired
    private fandao fandao;

    @Autowired
    private lesxidao lesxidao;

    @Autowired
    private ggadao ggadao;

    /*
    Προσθέτω fan στη λέσχη
     */
    @PostMapping("/newFan/{lid}")
    public Fan newFan(@RequestBody Fan fan, @PathVariable int lid)
    {

        Lesxi lesxi=lesxidao.getlesxi(lid);
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
            return  fan;
        }


    }

    /*
     Προσθέτω fan commander
     */
    @PostMapping("/addfancommand")
    public Fan newfancommander(@RequestBody Fan fan)
    {
        fan.setId(0);
        fandao.savefan(fan);
        return fan;
    }



    @GetMapping("/{fid}")
    public Fan getFan(@PathVariable int fid)
    {
        return fandao.getfanbyid(fid);
    }



    @DeleteMapping("{fid}")
    public void deletefan(@PathVariable int fid)
    {
        fandao.deletefan(fid);
    }

}
