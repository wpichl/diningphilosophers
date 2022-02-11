package at.htlleonding;

public class PrintCurrentStates {
    static String _currentStates[] = new String[Const.NumberOfTransactions];
    static long _transactionTransactions[] = new long[Const.NumberOfTransactions];
    static long _iteration = 0;
    static long _actualTransactionCount = 0;
    static double _firstTransactionTime;
    static void updateState(int i, String state, String msg, boolean isTransaction)
    {
        if(_iteration == 0)
        {
            _firstTransactionTime = System.currentTimeMillis();

        }
        if(isTransaction)
        {
            _actualTransactionCount++;
            _transactionTransactions[i]++;
        }
        _currentStates[i] = state;
        printAllStates(i,msg);
    }

    private synchronized static void printAllStates(int currentChange,String msg) {
        _iteration++;
        System.out.println("Iteration: "+_iteration);
        System.out.println("\u001B[34m"+msg+"\u001B[0m");
        System.out.println();
        for (int i = 0; i < Const.NumberOfTransactions; i++) {
            String current = _currentStates[i];
            if(i == currentChange)
            {
                current = "\u001B[32m"+current+"\u001B[0m";
            }
            System.out.println(current);
        }
        System.out.println();
        double milsElapsed = (double)System.currentTimeMillis() - (double)_firstTransactionTime;
        double minutesElapsed = milsElapsed/1000/60;
        System.out.println("Current transactions per minute: "+"\u001B[33m"+Math.round(_actualTransactionCount/minutesElapsed)+"\u001B[0m");
        System.out.println(highestDiff());
        System.out.println("----------------------------");
    }
    private static String highestDiff()
    {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        String retVal = "Actual transactions: ";
        for (long curr: _transactionTransactions
             ) {
            if(curr < min)
            {
                min = curr;
            }
            else if(curr > max)
            {
                max = curr;
            }
            retVal += curr+",";
        }
        retVal+="MaxDifference: \u001B[35m";
        retVal+=String.valueOf(max-min);
        retVal+="\u001B[0m";
        return retVal;
    }
}