package gr.hua.dit.ds.Registry_Fan_clubs.dao;

import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiEllas;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.*;

@Repository
public class ellasdaoimpl implements ellasdao{
    @Autowired
    private EntityManager em;



    @Transactional
    @Override
    public AitisiEllas newAitisiEllas(AitisiEllas aitisiellas) {
        aitisiellas.setStatus("created");
        aitisiellas.setResult("pending_inspection");
        return em.merge(aitisiellas);

    }

    @Transactional
    @Override
    public List<AitisiEllas> aitisis() {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from AitisiEllas", AitisiEllas.class);
        List<AitisiEllas> aitisis = query.getResultList();
        return aitisis;
    }

    @Transactional
    @Override
    public AitisiEllas getAitisiellasbyid(int id) {
        AitisiEllas aitisi=em.find(AitisiEllas.class, id);
        return aitisi;
    }

    @Transactional
    @Override
    public AitisiEllas saveAitisi(AitisiEllas aitisiellas) {
     AitisiEllas   anaitisiellas=em.merge(aitisiellas);
        return anaitisiellas;
    }

    /*
    Επιστρέφει τις εκκρεμείς αιτήσεις της ΕΛΑΣ
     */

    @Transactional
    @Override
    public List<AitisiEllas> aitisis_ekkremeis() {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from AitisiEllas", AitisiEllas.class);
        List<AitisiEllas> aitisis = query.getResultList();
        List<AitisiEllas> ekkremeis=new ArrayList<>();
        for(int i=0; i<aitisis.size(); i++)
        {
            if(aitisis.get(i).getStatus().equals("created"))
            {
                ekkremeis.add(aitisis.get(i));
            }
        }
        return ekkremeis;
    }
}
