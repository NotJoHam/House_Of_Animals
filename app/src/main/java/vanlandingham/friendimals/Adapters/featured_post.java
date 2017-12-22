package vanlandingham.friendimals.Adapters;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;

import vanlandingham.friendimals.Model.User;

/**
 * Created by Owner on 11/18/2017.
 */

public class featured_post {
    private String string;
    private String url;
    private User user;

    public featured_post() {}

    public featured_post(String string, String url ) {
        this.string = string;
        this.url = url;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public featured_post(@Nullable User user) {
        this.user = user;
    }

    public featured_post(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public String getUrl() {
        return url;
    }
}
