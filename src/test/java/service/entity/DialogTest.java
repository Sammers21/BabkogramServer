package service.entity;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import service.repository.DialogRepository;

import static org.junit.Assert.*;


public class DialogTest {
    @Autowired
    DialogRepository dialogRepository;

    @Test
    public void generate() throws Exception {

    }

    @Test
    public void addUser() throws Exception {
        Dialog d = new Dialog();
        d.addUser("kek");
        assertTrue(d.getUserNameList().size() == 1);
        d.addUser("kek");
        assertTrue(d.getUserNameList().size() == 1);
        d.addUser("kek2");
        assertTrue(d.getUserNameList().size() == 2);
        d.addUser("kek3");
        assertTrue(d.getUserNameList().size() == 3);
        d.addUser("kek");
        assertTrue(d.getUserNameList().size() == 3);
    }

    @Test
    public void deleteUser() throws Exception {
        Dialog d = new Dialog();
        d.addUser("kek1");
        d.addUser("kek2");
        d.addUser("kek3");
        d.addUser("kek4");
        d.deleteUser("kek1");
        assertTrue(d.getUserNameList().size()==3);
        d.deleteUser("kek2");
        assertTrue(d.getUserNameList().size()==2);
        d.deleteUser("kek3");
        assertTrue(d.getUserNameList().size()==1);
        d.deleteUser("kek4");
        assertTrue(d.getUserNameList().size()==0);

    }

    @Test
    public void getUserNameList() throws Exception {

    }

}