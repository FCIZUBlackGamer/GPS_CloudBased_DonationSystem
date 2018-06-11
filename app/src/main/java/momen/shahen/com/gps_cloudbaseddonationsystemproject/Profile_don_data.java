package momen.shahen.com.gps_cloudbaseddonationsystemproject;


/**
 * Created by fci on 31/01/18.
 */

public class Profile_don_data {
    String name, Age, Last_Donation, Email, points;

    public Profile_don_data(String name, String Age, String Last_Donation, String Email, String points) {
        this.name = name;
        this.Age = Age;
        this.Last_Donation = Last_Donation;
        this.Email = Email;
        this.points = points;
    }

    public String getAge() {
        return Age;
    }

    public String getEmail() {
        return Email;
    }

    public String getLast_don() {
        return Last_Donation;
    }

    public String getName() {
        return name;
    }

    public String getPoints() {
        return points;
    }
}
