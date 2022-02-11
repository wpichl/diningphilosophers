package at.htlleonding;

public class Const {
    public static String TransactionMsg;

    public enum PossibleMode{
        StaleMate,
        LaysDownAccWhenNotNeeded,
        OnlyOneCanTransfer,
        Pattern
    }

    public static enum ExampleMode{
        DiningPhilosophers(0),
        FactoryWorkers(1);
        private final int val;
        private ExampleMode(int givenVal)
        {
            val = givenVal;
        }
    }
    public static enum Alias{
        Philosopher(0),
        Fork(1),
        Forks(2),
        Eating(3),
        Eaten(4),
        Ate(5),
        ShortFork(6),
        Thinking(7),
        PickedUp(8),
        PutDown(9),
        Putting(10),
        back(11);
        private final int val;
        private Alias(int givenVal)
        {
            val = givenVal;
        }
    }
    public static String[][] languageArray;
    public static void initLanguagePackets()
    {
        languageArray = new String[ExampleMode.values().length][Alias.values().length];

        languageArray[ExampleMode.DiningPhilosophers.val][Alias.Philosopher.val] = "Philosopher";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.Fork.val] = "Fork";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.Forks.val] = "Forks";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.Eating.val] = "Eating";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.Eaten.val] = "Eaten";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.Ate.val] = "Ate";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.ShortFork.val] = "F";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.Thinking.val] = "Thinking";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.PickedUp.val] = "picked up";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.PutDown.val] = "put down";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.Putting.val] = "putting";
        languageArray[ExampleMode.DiningPhilosophers.val][Alias.back.val] = "back";

        TransactionMsg = eatenAlias;

        PhilosopherAlias = languageArray[Settings.Example.val][Alias.Philosopher.val];
        forkAlias = languageArray[Settings.Example.val][Alias.Fork.val];
        forksAlias = languageArray[Settings.Example.val][Alias.Forks.val];
        eatingAlias = languageArray[Settings.Example.val][Alias.Eating.val];
        eatenAlias = languageArray[Settings.Example.val][Alias.Eaten.val];
        ateAlias = languageArray[Settings.Example.val][Alias.Ate.val];
        shortForkAlias = languageArray[Settings.Example.val][Alias.ShortFork.val];
        thinkingAlias = languageArray[Settings.Example.val][Alias.Thinking.val];
        pickedUpAlias = languageArray[Settings.Example.val][Alias.PickedUp.val];
        putDownAlias = languageArray[Settings.Example.val][Alias.PutDown.val];
        puttingAlias = languageArray[Settings.Example.val][Alias.Putting.val];
        backAlias = languageArray[Settings.Example.val][Alias.back.val];
    }

    public static String PhilosopherAlias;
    public static String forkAlias;
    public static String forksAlias;
    public static String eatingAlias;
    public static String eatenAlias;
    public static String ateAlias;
    public static String shortForkAlias;
    public static String thinkingAlias;
    public static String pickedUpAlias;
    public static String putDownAlias;
    public static String puttingAlias;
    public static String backAlias;
}
