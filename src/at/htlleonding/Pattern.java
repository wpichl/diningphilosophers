package at.htlleonding;

import java.util.LinkedList;
import java.util.List;

public class Pattern {
    private static boolean[] allowingMovement = new boolean[Const.NumberOfTransactions];
    private static List<Integer> currentPattern = new LinkedList<>();

    public static void init()
    {
        int attempt = 0;
        boolean everythingCalcded = false;
        while(!everythingCalcded)
        {
            if(attempt >= Const.NumberOfTransactions-1)
            {
                everythingCalcded = true;
            }
            else{
                currentPattern.add(attempt);
                attempt+=2;
            }
        }

        currentPattern.add(0);
        currentPattern.add(2);

        updateBoolPattern();
    }

    public static synchronized void attemptMovePattern(int i)
    {
        allowingMovement[i] = true;

        if(movingIsAllowed())
        {
            move();
            updateBoolPattern();
        }
    }
    public synchronized static boolean patternAllowsAction(int i)
    {
        if(currentPattern.contains(i) && !allowingMovement[i])
        {
            return true;
        }
        else{
            return false;
        }
    }
    private synchronized static void move()
    {
        List<Integer> workingList = new LinkedList<>();
        workingList.addAll(currentPattern);
        for (Integer curr: workingList
             ) {
            int next = curr+1;

            if(next >= Const.NumberOfTransactions)
            {
                next = 0;
            }

            currentPattern.remove(curr);
            currentPattern.add(next);
        }
    }


    private synchronized static boolean movingIsAllowed()
    {
        for (boolean curr: allowingMovement
             ) {
            if(!curr)
            {
                return false;
            }
        }
        return true;
    }

    private synchronized static void updateBoolPattern()
    {
        for(int i = 0; i < allowingMovement.length; i ++)
        {
            if(currentPattern.contains(i))
            {
                allowingMovement[i] = false;
            }
            else{
                allowingMovement[i] = true;
            }
        }
    }
}
