package at.htlleonding;

public class Const {
    public static final String TransactionMsg = "transaction";

    public enum PossibleMode{
        StaleMate,
        LaysDownAccWhenNotNeeded,
        OnlyOneCanTransfer,
        Pattern
    }
}
