package at.htlleonding;

public class Main {
    public static void main(String[] args) {
        Const.initLanguagePackets();
        Philosopher[] transactors = new Philosopher[Settings.NumberOfTransactions];
        Fork[] bankaccounts = new Fork[Settings.NumberOfTransactions];

        if (Settings.Mode == Const.PossibleMode.Pattern) {
            Pattern.init();
        }

        for (int i = 0; i < Settings.NumberOfTransactions; i++) {
            bankaccounts[i] = new Fork(Const.forkAlias+" #" + (i + 1), Const.shortForkAlias + (i + 1), (int) Math.random() * 1000);
        }

        for (int i = 0; i < Settings.NumberOfTransactions; i++) {
            Fork left = bankaccounts[i];
            Fork right = bankaccounts[(i + 1) % Settings.NumberOfTransactions];

            // no check = deadlock
            if (i == Settings.NumberOfTransactions - 1) {
                transactors[i] = new Philosopher(right, left, i);
            } else {
                transactors[i] = new Philosopher(left, right, i);
            }
            Thread t = new Thread(transactors[i],
                    Const.PhilosopherAlias+" " + (i + 1) +
                            "(needs: " + transactors[i].getLeft().getShortName() +
                            ", " + transactors[i].getRight().getShortName() + ")");
            t.start();
        }
    }
}
