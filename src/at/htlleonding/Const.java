package at.htlleonding;

public class Const {
    public static final String TransactionMsg = "transaction";

    public static final short NumberOfTransactions = 5;

    public static PossibleMode Mode = PossibleMode.Synchronized;
    public enum PossibleMode{
        StaleMate,
        LaysDownAccWhenNotNeeded,
        Synchronized
    }
}
