package vanlandingham.friendimals.Model;

/**
 * Created by Owner on 12/12/2017.
 */

public class Upload {

    private String username;
    private String filename;
    private String url;

    public Upload() {}

    public Upload(String username, String filename, String url) {
        this.username = username;
        this.filename = filename;
        this.url = url;
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
