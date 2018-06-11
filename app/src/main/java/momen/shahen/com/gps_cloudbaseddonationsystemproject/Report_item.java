package momen.shahen.com.gps_cloudbaseddonationsystemproject;

/**
 * Created by fci on 16/01/18.
 */

public class Report_item {
    private String Name , date, photo_url;

    public Report_item(String date, String photo_url, String Name) {
        this.Name = Name;
        this.date = date;
        this.photo_url = photo_url;
    }

    public String getName() {
        return Name;
    }

    public String getTime() {
        return date;
    }

    public String getPhoto_url() {
        return photo_url;
    }
}
