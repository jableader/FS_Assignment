package common;

/**
 * Created by Jableader on 13/5/2015.
 */
public enum Services {
    Authentication("TGT"),
    TicketGranting("GSK"),
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
