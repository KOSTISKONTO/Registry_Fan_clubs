package gr.hua.dit.ds.Registry_Fan_clubs.dao;
import java.util.List;

import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.Lesxi;

public interface lesxidao {
    List<Lesxi> getLesxes();
    Lesxi saveLesxi(Lesxi lesxi);
    Lesxi getlesxi(int id);
    void deleteLesxi(int id);
    Lesxi getLesxi_by_id_aitisis(int id_aitisis);
    Lesxi getLesxi_by_name_aitisis(String name);

}
