package gr.hua.dit.ds.Registry_Fan_clubs.dao;


import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

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


}
