package at.htlleonding;

public class Settings {
    public static final short NumberOfTransactions = 5;

    public static final boolean ShallPrintTransactionStates = NumberOfTransactions < 10;

    public static final boolean ShallPrintAllActualTransactionNumbers = NumberOfTransactions < 10;

    public static Const.PossibleMode Mode = Const.PossibleMode.LaysDownAccWhenNotNeeded;

    public static Const.ExampleMode Example = Const.ExampleMode.DiningPhilosophers;
}