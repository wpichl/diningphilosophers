package at.htlleonding;

public class Main {
    public static void main(String[] args) {
        Transactions[] transactors = new Transactions[Const.NumberOfTransactions];
        BankAccount[] bankaccounts = new BankAccount[Const.NumberOfTransactions];

        for (int i = 0; i < Const.NumberOfTransactions; i++) {
            bankaccounts[i] = new BankAccount("Acc #"+(i+1),"A"+(i+1) ,(int)Math.random()*1000);
        }

        for (int i = 0; i < Const.NumberOfTransactions; i++) {
            BankAccount left = bankaccounts[i];
            BankAccount right = bankaccounts[(i + 1) % Const.NumberOfTransactions];

            // no check = deadlock
            if (i == Const.NumberOfTransactions - 1) {
                transactors[i] = new Transactions(right, left,i);
            }
            else {
                transactors[i] = new Transactions(left, right,i);
            }
            Thread t = new Thread(transactors[i],
                    "Transactor " + (i + 1) +
                            "(needs: "+transactors[i].getLeftAccount().getShortName()+
                            ", "+transactors[i].getRightAccount().getShortName()+")");
            t.start();
        }
    }
}
