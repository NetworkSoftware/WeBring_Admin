package smart.network.patasuadmin.staff;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class Staff implements Serializable {
    String id;
    String name;
    String storeid;
    String storename;
    String password;

    public Staff() {
    }

    public Staff(String name, String storeid, String password) {
        this.name = name;
        this.storeid = storeid;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }
}