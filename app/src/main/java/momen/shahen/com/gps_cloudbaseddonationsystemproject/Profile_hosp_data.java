package momen.shahen.com.gps_cloudbaseddonationsystemproject;


/**
 * Created by fci on 31/01/18.
 */

public class Profile_hosp_data {
    String Name,Email, CityName;

    public Profile_hosp_data(String Name, String Email, String CityName) {
        this.Name = Name;
        this.Email = Email;
        this.CityName = CityName;
    }

    public String getCity_name() {
        return CityName;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

}
