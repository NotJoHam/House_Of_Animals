package vanlandingham.friendimals.Model;

import java.util.Map;

/**
 * Created by Owner on 12/12/2017.
 */

public class Upload {


    private String username;
    private String filename;
    private String message;
    private String url;
    private long timestamp;

    public Upload() {}

    public Upload(String username,String filename,String url,long timestamp,String message) {
        this.username = username;
        this.filename = filename;
        this.url = url;
        this.timestamp = timestamp;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
