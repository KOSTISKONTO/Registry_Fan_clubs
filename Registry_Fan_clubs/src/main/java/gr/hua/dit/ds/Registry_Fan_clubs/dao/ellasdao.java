package gr.hua.dit.ds.Registry_Fan_clubs.dao;

import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiEllas;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;

import java.util.List;

public interface ellasdao {

    AitisiEllas newAitisiEllas(AitisiEllas aitisiellas);
    List<AitisiEllas> aitisis();
    AitisiEllas getAitisiellasbyid(int id);
    AitisiEllas saveAitisi(AitisiEllas aitisiellas);
    List<AitisiEllas> aitisis_ekkremeis();
}
