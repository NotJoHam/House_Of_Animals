package vanlandingham.friendimals.Model;

/**
 * Created by Owner on 11/16/2017.
 */

public class Post {

    private String message;
    private String username;

    public Post() {}

    public Post(String message,String username) {

        this.message=message;
        this.username=username;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
