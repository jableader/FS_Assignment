package AuthenticationServer;

import java.util.Date;

/**
 * Created by Jableader on 11/05/2015.
 */
public class Key {
    public final byte[] key;
    public final Date expirey;

    public Key(Date expirey, byte[] key) {
        this.key = key;
        this.expirey = expirey;
    }

    public boolean hasExpired() {
        return new Date().compareTo(expirey) >= 0;
    }

    public String toString() {
        return Common.Tools.toHexString(key);
    }
}
