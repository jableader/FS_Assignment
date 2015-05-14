package server.internals;

/**
 * Created by Jableader on 13/5/2015.
 */
public enum RType {
    GetTicketGrantingTicket("TGT"),
    GetSessionKey("GSK"),
    Invalid("INV");

    public final String id;
    RType(String id) {
        this.id = id;
    }

    public static RType fromCode(String code) {
        for (RType r: RType.values())
            if (r.id.equals(code))
                return r;

        return Invalid;
    }
}
