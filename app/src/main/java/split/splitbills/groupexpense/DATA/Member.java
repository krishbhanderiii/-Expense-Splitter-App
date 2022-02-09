package split.splitbills.groupexpense.DATA;

public class Member {

    private int ID;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    private String Name;
    private String uri;
    private String NUMBER;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Member(String name, String uri, String number) {
        this.uri = uri;
        this.Name = name;
        this.NUMBER = number;
    }
}
