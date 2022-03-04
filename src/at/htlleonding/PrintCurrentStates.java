package at.htlleonding;

public class PrintCurrentStates {
    static String _currentStates[] = new String[Settings.NumberOfPhilosophers];
    static long _transactionTransactions[] = new long[Settings.NumberOfPhilosophers];
    static long _iteration = 0;
    static long _actualTransactionCount = 0;
    static double _firstTransactionTime;

    static void updateState(int i, String state, String msg, boolean isTransaction) {
        if (_iteration == 0) {
            _firstTransactionTime = System.currentTimeMillis();

        }
        if (isTransaction) {
            _actualTransactionCount++;
            _transactionTransactions[i]++;
        }
        _currentStates[i] = state;
        printAllStates(i, msg);
    }

    private synchronized static void printAllStates(int currentChange, String msg) {
        _iteration++;
        System.out.println("Iteration: " + _iteration);
        System.out.println("\u001B[32m" + msg + "\u001B[0m");
        System.out.println();
        if (Settings.ShallPrintState) {
            for (int i = 0; i < Settings.NumberOfPhilosophers; i++) {
                String current = _currentStates[i];
                if (i == currentChange) {
                    current = "\u001B[32m" + current + "\u001B[0m";
                }
                System.out.println(current);
            }
        }
        System.out.println();
        double milsElapsed = (double) System.currentTimeMillis() - (double) _firstTransactionTime;
        double minutesElapsed = milsElapsed / 1000 / 60;
        System.out.println(Const.eatenAlias + " per minute: \u001B[33m" +
                Math.round(_actualTransactionCount / minutesElapsed) +
                "\u001B[0m");
        System.out.println(Const.eatenAlias + " per minute per " + Const.PhilosopherAlias + ": \u001B[34m" +
                Math.round(_actualTransactionCount / minutesElapsed / Settings.NumberOfPhilosophers) +
                "\u001B[0m");
        System.out.println(highestDiff());
        System.out.println("----------------------------");
    }

    private static String highestDiff() {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        String retVal = "";
        if (Settings.ShallPrintAllActualNumbers) {
            retVal = "Actual "+Const.eatenAlias+": ";
        }
        for (long curr : _transactionTransactions
        ) {
            if (curr < min) {
                min = curr;
            }
            if (curr > max) {
                max = curr;
            }
            if (Settings.ShallPrintAllActualNumbers) {
                retVal += curr + ",";
            }
        }
        retVal += "MaxDifference: \u001B[35m";
        retVal += String.valueOf(max - min);
        retVal += "\u001B[0m";
        return retVal;
    }
}