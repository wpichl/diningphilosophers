package at.htlleonding;

public class Main {
    public static void main(String[] args) {
        Transactions[] transactors = new Transactions[5];
        BankAccount[] bankaccounts = new BankAccount[transactors.length];

        for (int i = 0; i < bankaccounts.length; i++) {
            bankaccounts[i] = new BankAccount();
        }

        for (int i = 0; i < transactors.length; i++) {
            BankAccount left = bankaccounts[i];
            BankAccount right = bankaccounts[i + 1 % bankaccounts.length];

            // no check = deadlock
            if (i == transactors.length - 1) {
                transactors[i] = new Transactions(right, left);
            }
            else {
                transactors[i] = new Transactions(left, right);
            }
            Thread t = new Thread(transactors[i], "Transactor " + (i + 1));
            t.start();
        }
    }
}
