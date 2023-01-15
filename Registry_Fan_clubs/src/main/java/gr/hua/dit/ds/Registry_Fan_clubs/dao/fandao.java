package gr.hua.dit.ds.Registry_Fan_clubs.dao;
import java.util.*;

import gr.hua.dit.ds.Registry_Fan_clubs.entity.Fan;

public interface fandao {

    List<Fan> getFans();
    Fan savefan(Fan fan);
    Fan getfanbyid(int id);
    void deletefan(int id);


}
