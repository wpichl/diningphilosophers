package at.htlleonding;

public class Const {
    public static final String TransactionMsg = "transaction";

    public static final short NumberOfTransactions = 5;

    public static final boolean ShallPrintTransactionStates = false;
    public static final boolean ShallPrintAllActualTransactionNumbers = false;

    public static PossibleMode Mode = PossibleMode.Pattern;
    public enum PossibleMode{
        StaleMate,
        LaysDownAccWhenNotNeeded,
        Synchronized,
        Pattern
    }
}
