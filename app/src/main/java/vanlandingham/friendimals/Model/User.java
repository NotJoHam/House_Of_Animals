package vanlandingham.friendimals.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner on 12/14/2017.
 */

public class User implements Parcelable {

    //public Map<String, String> hashMap = new HashMap<>();
    private String username;
    private String email;
    private String uid;
    private int follower_count;
    private int following_count;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User() {


    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public User(Parcel in) {


        this.username = in.readString();
        this.email = in.readString();
        this.uid = in.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(uid);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        public User[] newArray(int size) {
            return new User[size];
        }
    };



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String username, String email, String uid) {
        this.username = username;
        this.email = email;
        this.uid = uid;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername(){
        return username;
    }


}
