package at.htlleonding;

public class Transactions implements Runnable {
    private BankAccount mLeftAccount;
    private BankAccount mRightAccount;

    public Transactions(BankAccount leftAccount, BankAccount rightAccount) {
        mLeftAccount = leftAccount;
        mRightAccount = rightAccount;
    }

    @Override
    public void run() {
        try {
            while(true) {
                doAction(System.nanoTime() + ": Thinking");
                synchronized (mLeftAccount) {
                    doAction(System.nanoTime() + ": Picked up left account");
                    synchronized (mRightAccount) {
                        doAction(System.nanoTime() + ": Picked up right account - transacting");
                        doAction(System.nanoTime() + ": Put down right account");
                    }

                    doAction(System.nanoTime() + ": Put down left account - back to thinking");
                }
            }
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        }
    }

    private void doAction(String action) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " " + action);
        Thread.sleep(((int) (Math.random() * 100)));
    }
}
