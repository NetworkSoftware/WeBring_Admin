package smart.network.patasuadmin.offer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user_1 on 12-07-2018.
 */

public class Offer implements Serializable {
    public String id;
    public String name;
    public String image;
    public String startDate;
    public String endDate;
    ArrayList<String> image1;

    public Offer() {
    }

    public Offer(String name, String image, String startDate, String endDate) {
        this.name = name;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ArrayList<String> getImage1() {
        return image1;
    }

    public void setImage1(ArrayList<String> image1) {
        this.image1 = image1;
    }

    public Offer(String id, String name, String image, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
