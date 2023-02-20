package gr.hua.dit.ds.Registry_Fan_clubs.dao;

import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.ArrayList;

@Repository

public class ggadaoimpl implements ggadao{
    @Autowired
    private EntityManager em;

@Autowired
    private lesxidao lesxidao;

@Autowired
private fandao fandao;

    @Transactional
    @Override
    public AitisiGga newAitisigga(AitisiGga aitisiGga) {
        aitisiGga.setStatus("under_construction");
        aitisiGga.setResult("pending_inspection");
        return em.merge(aitisiGga);
    }


@Transactional
    @Override
    public List<AitisiGga> aitisis() {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from AitisiGga", AitisiGga.class);
        List<AitisiGga> aitisis = query.getResultList();
        return aitisis;
    }

    @Transactional
    @Override
    public AitisiGga getAitisiggabyid(int id) {
        AitisiGga aitisi=em.find(AitisiGga.class, id);
        return aitisi;
    }


    /*
        Τσεκάρει μια αίτηση για την ΓΓΑ
     */
    @Transactional
    @Override
    public AitisiGga checkAitisi(int id) {
        AitisiGga aitisi=em.find(AitisiGga.class, id);
        Lesxi lesxi=lesxidao.getLesxi_by_id_aitisis(id);

        //Αν η αίτηση έχει δημιουργηθεί και είναι υπό αξιολόγηση
        if(aitisi.getStatus().equals("created") && aitisi.getResult().equals("pending_inspection"))
        {
            int size=aitisi.getNumber_members_lesxis();
            if(size<4)
            {
                aitisi.setResult("aporiptetai. members<4");
            }
            else {
                int i;
                for ( i=0; i<size; i++)
                {
                    if(lesxi.getFans().get(i).getAge()<18)
                    {
                        aitisi.setResult("aporiptetai. membre's age<18");
                        break;
                    }
                }
                if (i==size)
                {
                    aitisi.setResult("egkrithike");
                }
            }
        }
        return aitisi;
    }

    /*
    Εισάγει νέα αίτηση ΓΓΑ της Λέσχης
     */

    @Override
    public AitisiGga newAitisi_in_Lesxi(AitisiGga aitisigga) {

        Lesxi alesxi=lesxidao.getLesxi_by_name_aitisis(aitisigga.getName_lesxis());
        Fan afan=new Fan();
        List<Fan> fans=fandao.getFans();
        for (int i=0; i<fans.size(); i++)
        {
            if (fans.get(i).getId()==alesxi.getId_commander())
            {
                afan=fans.get(i);
            }
        }
        aitisigga.setStatus("under_construction");
        aitisigga.setResult("pending_inspection");
        aitisigga.setAM_commander(afan.getAM());
        aitisigga.setFan_commander_age(afan.getAge());
        aitisigga.setFan_commander_name(afan.getName());
        aitisigga.setFan_commander_surname(afan.getSurname());
        alesxi.addaitisigga(aitisigga);
        aitisigga.setLesxi_id_aitisigga(alesxi);
        lesxidao.saveLesxi(alesxi);
        return aitisigga;
    }

    @Override
    public AitisiGga getGgabyAmcommander(String idcommander) {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from AitisiGga", AitisiGga.class);
        List<AitisiGga> aitisis = query.getResultList();
        for (int i=0; i<aitisis.size(); i++)
        {
            if(aitisis.get(i).getAM_commander().equals(idcommander))
            {
                return aitisis.get(aitisis.size()-1);
            }
        }
        return null;
    }

    @Transactional
    @Override
    public List<AitisiGga> getekkremeis() {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from AitisiGga", AitisiGga.class);
        List<AitisiGga> aitisis = query.getResultList();
        List<AitisiGga> ekkremeis=new ArrayList<>();
        for (int i=0; i<aitisis.size(); i++ )
        {
            AitisiGga aitisi=aitisis.get(i);
            if(aitisi.getStatus().equals("created") && aitisi.getResult().equals("pending_inspection"))
            {
                ekkremeis.add(aitisi);
            }
        }
       return ekkremeis;
    }

    @Transactional
    @Override
    public AitisiGga saveAitisi(AitisiGga aitisigga) {
        aitisigga=em.merge(aitisigga);
        return aitisigga;
    }

}
