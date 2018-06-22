package momen.shahen.com.gps_cloudbaseddonationsystemproject;

/**
 * Created by fci on 16/01/18.
 */

public class blood_noti_item {
    private String doctorName, content, date, requestState, Name;
    String num_care, type;

    public blood_noti_item(String date, String content, String Name, String doctorName, String requestState) {
        this.doctorName = doctorName;
        this.content = content;
        this.date = date;
        this.requestState = requestState;
        this.Name = Name;
    }

    public blood_noti_item(String doctorName, String content, String date, String requestState, String name, String num_care, String type) {
        this.doctorName = doctorName;
        this.content = content;
        this.date = date;
        this.requestState = requestState;
        this.Name = name;
        this.type = type;
        this.num_care = num_care;
    }

    public String getType() {
        return type;
    }

    public String getNum_care() {
        return num_care;
    }

    public String getDocName() {
        return doctorName;
    }

    public String getBody() {
        return content;
    }

    public String getTime() {
        return date;
    }

    public String getState() {
        return requestState;
    }

    public String getHosname() {
        return Name;
    }
}
