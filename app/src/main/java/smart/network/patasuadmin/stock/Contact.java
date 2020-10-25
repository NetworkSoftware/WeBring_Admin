package smart.network.patasuadmin.stock;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class Contact implements Serializable{
    String id;
    String title;
    String items;
    String price;
    String shopid;

    public Contact() {
    }

    public Contact(String title, String items, String price, String shopid) {
        this.title = title;
        this.items = items;
        this.price = price;
        this.shopid = shopid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
}
