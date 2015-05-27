package common;

/**
 * Fundamentals Of Security, Assignment 2
 * Created by Jacob Dunk
 */
public enum Services {
    Authentication("TGT"),
    TicketGranting("TGS"),
    ServerService("SER"),
    Invalid("INV");

    public final String id;

    Services(String id) {
        this.id = id;
    }

    public static Services fromCode(String code) {
        for (Services r : Services.values())
            if (r.id.equals(code))
                return r;

        return Invalid;
    }
}
