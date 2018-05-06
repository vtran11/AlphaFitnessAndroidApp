package vytran.cs175.sjsu.alphafitnessandroidapp;

/**
 * Created by ThaoVy on 4/20/18.
 */

public class UserInfo {
    private int myId;
    private String myName;
    private String myGender;
    private float myWeight;

    public UserInfo(int id, String name, String gender, float weight) {
        myId = id;
        myName = name;
        myGender = gender;
        myWeight = weight;
    }

    public UserInfo(){}

    public void setUserId(int Id) { this.myId = Id;}
    public int getUserId() {return myId;}

    public void setUserName(String name) {myName = name;}
    public String getUserName() {return myName;}

    public void setUserGender(String gender) {myGender = gender;}
    public String getUserGender() {return myGender;}

    public void setUserWeight(float weight) {myWeight = weight;}
    public float getUserWeight() {return myWeight;}

    public String toString() {
        return "Username: " + getUserName() + "\n" +
                "Gender: " + getUserGender() + "\n" +
                "Weight: " + getUserWeight();
    }
}
