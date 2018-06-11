package momen.shahen.com.gps_cloudbaseddonationsystemproject;

/**
 * Created by fci on 16/01/18.
 */

public class money_noti_item {
    private String Name, content, date;

    public money_noti_item(String date, String content, String Name) {
        this.Name = Name;
        this.content = content;
        this.date = date;
    }

    public String getName() {
        return Name;
    }

    public String getBody() {
        return content;
    }

    public String getTime() {
        return date;
    }
}
