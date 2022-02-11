package at.htlleonding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Transactions implements Runnable {
    private BankAccount mLeftAccount;
    private BankAccount mRightAccount;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss:ms");
    private Semaphore _semaphore;
    private int _idx;
    List<BankAccount> _availableAccounts = new LinkedList<>();

    public Transactions(BankAccount leftAccount, BankAccount rightAccount, int idx) {
        mLeftAccount = leftAccount;
        mRightAccount = rightAccount;
        _idx = idx;
        _semaphore = new Semaphore(1);
    }

    public BankAccount getLeftAccount() {
        return mLeftAccount;
    }

    public BankAccount getRightAccount() {
        return mRightAccount;
    }

    @Override
    public void run() {
        try {
            doAction(": Thinking");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        }
        while (true) {
            _availableAccounts.clear();
            try {
                if (Settings.Mode == Const.PossibleMode.StaleMate) {
                    DeadLockVariation();
                } else if (Settings.Mode == Const.PossibleMode.LaysDownAccWhenNotNeeded) {
                    LayingDownIfNotNeededVariation();
                } else if (Settings.Mode == Const.PossibleMode.OnlyOneCanTransfer) {
                    SemaphoreVariation();
                } else if (Settings.Mode == Const.PossibleMode.Pattern) {
                    PatternVariation();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void PatternVariation() throws InterruptedException {
        if (Pattern.patternAllowsAction(_idx)) {
            if (mLeftAccount.canBeUsed()) {
                mLeftAccount.grab();
                _availableAccounts.add(mLeftAccount);
                doAction(": Picked up left account");

                if (mRightAccount.canBeUsed()) {
                    mRightAccount.grab();
                    _availableAccounts.add((mRightAccount));
                    doAction(": Picked up right account");

                    doAction(Const.TransactionMsg);

                    _availableAccounts.remove(mLeftAccount);
                    doAction(": transacted. Put down left account");
                    mLeftAccount.release();
                    _availableAccounts.remove(mRightAccount);
                    doAction(": transacted. Put down right account - back to thinking");
                    mRightAccount.release();
                } else {
                    _availableAccounts.remove(mLeftAccount);
                    doAction(": right could not be picked up, putting left back - back to thinking");
                    mLeftAccount.release();
                }

                Pattern.attemptMovePattern(_idx);
            }
        }
    }

    private synchronized void SemaphoreVariation() throws InterruptedException {
        if (_semaphore.availablePermits() > 0)
        {
            _semaphore.acquire();
            if (mLeftAccount.canBeUsed()) {
                mLeftAccount.grab();
                _availableAccounts.add(mLeftAccount);
                doAction(": Picked up left account");
                if (mRightAccount.canBeUsed()) {
                    mRightAccount.grab();
                    _availableAccounts.add((mRightAccount));
                    doAction(": Picked up right account");

                    doAction(Const.TransactionMsg);

                    _availableAccounts.remove(mLeftAccount);
                    doAction(": transacted. Put down left account");
                    mLeftAccount.release();
                    _availableAccounts.remove(mRightAccount);
                    doAction(": transacted. Put down right account - back to thinking");
                    mRightAccount.release();
                } else {
                    _availableAccounts.remove(mLeftAccount);
                    doAction(": right could not be picked up, putting left back - back to thinking");
                    mLeftAccount.release();
                }
            }
            _semaphore.release();
        }
    }

    private void LayingDownIfNotNeededVariation() throws InterruptedException {
        if (mLeftAccount.canBeUsed()) {
            mLeftAccount.grab();
            _availableAccounts.add(mLeftAccount);
            doAction(": Picked up left account");

            if (mRightAccount.canBeUsed()) {
                mRightAccount.grab();
                _availableAccounts.add((mRightAccount));
                doAction(": Picked up right account");

                doAction(Const.TransactionMsg);

                _availableAccounts.remove(mLeftAccount);
                doAction(": transacted. Put down left account");
                mLeftAccount.release();
                _availableAccounts.remove(mRightAccount);
                doAction(": transacted. Put down right account - back to thinking");
                mRightAccount.release();
            } else {
                _availableAccounts.remove(mLeftAccount);
                doAction(": right could not be picked up, putting left back - back to thinking");
                mLeftAccount.release();
            }
        }
    }

    private void DeadLockVariation() throws InterruptedException {
        if (mLeftAccount.canBeUsed()) {
            mLeftAccount.grab();
            _availableAccounts.add(mLeftAccount);
            doAction(": Picked up left account");
        }
        if (mRightAccount.canBeUsed()) {
            mRightAccount.grab();
            _availableAccounts.add((mRightAccount));
            doAction(": Picked up right account");
        }
        if (_availableAccounts.size() == 2) {
            doAction(Const.TransactionMsg);

            _availableAccounts.remove(mRightAccount);
            doAction(": Put down right account");
            mRightAccount.release();

            _availableAccounts.remove(mLeftAccount);
            doAction(": Put down left account - back to thinking");
            mLeftAccount.release();
        }
    }

    private void doAction(String action) throws InterruptedException {
        String accounts = " ";
        for (BankAccount curr : _availableAccounts
        ) {
            if (curr != null) {
                accounts += curr.getName() + " ";
            }
        }
        if (accounts == " ") {
            accounts = " No Accounts ";
        }
        boolean isTransactionMsg = false;
        if (action == Const.TransactionMsg) {
            isTransactionMsg = true;
        }

        PrintCurrentStates.updateState(_idx,
                Thread.currentThread().getName() + ":" + accounts,
                Thread.currentThread().getName() + " " + getCurrentTime() + action,
                isTransactionMsg);
        Thread.sleep(((int) (Math.random() * 100)));
    }

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}