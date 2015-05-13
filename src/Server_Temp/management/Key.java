package Server_Temp.management;

import java.util.Date;

/**
 * Created by Jableader on 11/05/2015.
 */
public class Key {
    public final byte[] key;
    public final Date expiry;

    public Key(Date expirey, byte[] key) {
        this.key = key;
        this.expiry = expirey;
    }

    public boolean hasExpired() {
        return new Date().compareTo(expiry) >= 0;
    }

    public String toString() {
        return Common.Tools.toHexString(key);
    }
}
