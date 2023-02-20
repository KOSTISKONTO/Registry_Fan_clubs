package gr.hua.dit.ds.Registry_Fan_clubs.dao;


import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.ArrayList;

@Repository
public class fandaoimpl implements fandao{
    @Autowired
    private EntityManager em;


@Transactional
    @Override
    public List<Fan> getFans() {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from Fan", Fan.class);
        List<Fan> fans = query.getResultList();
        return fans;
    }

    @Transactional
    @Override

    public Fan savefan(Fan fan) {

       Fan afan=em.merge(fan);
       return afan;
    }

    @Transactional
    @Override
    public Fan getfanbyid(int id) {
        Fan afan=em.find(Fan.class, id);
        return afan;
    }


    @Transactional
    @Override
    public void deletefan(int id) {
        Fan afan=em.find(Fan.class, id);
        em.remove(afan);
    }

    @Transactional
    @Override
    public Fan getfanbyam(String am) {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from Fan", Fan.class);
        List<Fan> fans = query.getResultList();

        for (int i=0; i<fans.size(); i++)
        {
            if(fans.get(i).getAM().equals(am))
            {
                return fans.get(i);
            }
        }
        return null;
    }

    @Override
    public Fan getfanbyuser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        List<Fan> fans = new ArrayList<>();
        fans=getFansCommander();
        Fan afan=new Fan();
        for (int i=0; i<fans.size(); i++)
        {

                if (fans.get(i).getUsername().equals(username)) {
                    afan = fans.get(i);
                    return afan;
                }

        }
        return null;
    }

    @Override
    public List<Fan> getFansCommander() {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from Fan", Fan.class);
        List<Fan> fans = query.getResultList();
        List<Fan> commanders=new ArrayList<Fan>();
        for (int i=0; i<fans.size(); i++)
        {
            if(fans.get(i).isIscommander())
            {
                commanders.add(fans.get(i));
            }
        }
        return commanders;
    }


}
