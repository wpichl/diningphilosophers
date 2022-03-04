package at.htlleonding;

public class Settings {
    public static final short NumberOfPhilosophers = 5;

    public static final boolean ShallPrintState = NumberOfPhilosophers < 10;

    public static final boolean ShallPrintAllActualNumbers = NumberOfPhilosophers < 10;

    public static Const.PossibleMode Mode = Const.PossibleMode.Pattern;

    public static Const.ExampleMode Example = Const.ExampleMode.Woodworker;
}