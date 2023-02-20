package gr.hua.dit.ds.Registry_Fan_clubs.dao;
import java.util.List;
import gr.hua.dit.ds.Registry_Fan_clubs.entity.AitisiGga;

public interface ggadao {
    AitisiGga newAitisigga(AitisiGga aitisiGga);
    List<AitisiGga> aitisis();
    AitisiGga getAitisiggabyid(int id);
    AitisiGga saveAitisi(AitisiGga aitigga);
    AitisiGga checkAitisi(int id);
    AitisiGga newAitisi_in_Lesxi(AitisiGga aitigga);
    AitisiGga getGgabyAmcommander(String idcommander);
    List<AitisiGga> getekkremeis();

}
