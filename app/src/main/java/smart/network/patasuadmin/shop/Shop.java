package smart.network.patasuadmin.shop;

import java.io.Serializable;

/**
 * Created by user_1 on 12-07-2018.
 */

public class Shop implements Serializable {
    public String id;
    public String contact;
    public String shopname;
    public String password;
    public String confirmPass;
    public String address;

    public Shop() {
    }

    public Shop(String id, String contact, String shopname, String password, String confirmPass, String address) {
        this.id = id;
        this.contact = contact;
        this.shopname = shopname;
        this.password = password;
        this.confirmPass = confirmPass;
        this.address = address;
       
    }

    public Shop( String contact, String shopname, String password, String confirmPass,  String address) {
        this.contact = contact;
        this.shopname = shopname;
        this.password = password;
        this.confirmPass = confirmPass;
        this.address = address;
        
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }


   
}
