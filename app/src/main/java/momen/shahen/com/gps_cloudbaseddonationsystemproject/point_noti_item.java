package momen.shahen.com.gps_cloudbaseddonationsystemproject;

/**
 * Created by fci on 16/01/18.
 */

public class point_noti_item {
    private String Name, content, date, sponser;

    public point_noti_item(String date, String content, String sponser, String Name) {
        this.Name = Name;
        this.content = content;
        this.date = date;
        this.sponser = sponser;
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

    public String getSpon() {
        return sponser;
    }
}
