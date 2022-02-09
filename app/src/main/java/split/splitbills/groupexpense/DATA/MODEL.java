package split.splitbills.groupexpense.DATA;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MODEL {
    private int NUM;
    private String Title;
    private String name;
    private String Date;
    private float money;


    private String GSONSTRING;
    List<String> persons;
    private int ID_NAME;
    List<Integer> ID_PERSON = new ArrayList<>();

    public MODEL() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getNUM() {
        return NUM;
    }

    public void setNUM(int NUM) {
        this.NUM = NUM;
    }

    public String getGSONSTRING() {
        return GSONSTRING;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    public void setGSONSTRING(String GSONSTRING) {
        this.GSONSTRING = GSONSTRING;
    }

    public MODEL(String title, String name, String date, int Money) {
        this.Title = title;
        this.name = name;
        this.Date = date;
        this.money = Money;
    }

    public List<String> GetPERSONS() {

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        persons = gson.fromJson(GSONSTRING, type);
        return persons;
    }

}
