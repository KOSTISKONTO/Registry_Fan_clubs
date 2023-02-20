package gr.hua.dit.ds.Registry_Fan_clubs.dao;

import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;
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
public class lesxidaoimpl implements lesxidao{

    @Autowired
    private EntityManager em;

    @Autowired
    private fandao fandao;

    @Override
    public List<Lesxi> getLesxes() {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from Lesxi", Lesxi.class);
        List<Lesxi> lesxes = query.getResultList();
        return lesxes;
    }

    @Transactional
    @Override
    public Lesxi saveLesxi(Lesxi lesxi) {
        Lesxi alesxi= em.merge(lesxi);
        return alesxi;
    }


    @Transactional
    @Override
    public Lesxi getlesxi(int id) {
        return em.find(Lesxi.class, id);
    }


    @Transactional
    @Override
    public void deleteLesxi(int id) {
        Lesxi alesxi=em.find(Lesxi.class, id);
        em.remove(alesxi);


    }

    @Override
    public Lesxi getLesxi_by_id_aitisis(int id_aitisis) {
        AitisiGga aitisiGga=em.find(AitisiGga.class, id_aitisis);
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from Lesxi", Lesxi.class);
        List<Lesxi> lesxes = query.getResultList();
        String name=aitisiGga.getName_lesxis();
        for (int i=0; i<lesxes.size(); i++)
        {
            if(lesxes.get(i).getName().equals(name))
            {
                return lesxes.get(i);

            }
        }
        return null;
    }

    @Override
    public Lesxi getLesxi_by_name_aitisis(String name) {

        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from Lesxi", Lesxi.class);
        List<Lesxi> lesxes = query.getResultList();
        for (int i=0; i<lesxes.size(); i++)
        {
           if(lesxes.get(i).getName().equals(name))
           {
               Lesxi alesxi=lesxes.get(i);
               System.out.println(alesxi.getId()+alesxi.getFans().toString()+alesxi.getName());
               return lesxes.get(i);

           }
        }

        return null;
    }

    @Override
    public Lesxi getLesxibyidcommander(int id) {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from Lesxi", Lesxi.class);
        List<Lesxi> lesxes = query.getResultList();
        for (int i=0; i<lesxes.size(); i++)
        {
            if(lesxes.get(i).getId_commander()==id)
            {
                return lesxes.get(i);
            }
        }
        return null;
    }


}
