package at.htlleonding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class Transactions implements Runnable {
    private BankAccount mLeftAccount;
    private BankAccount mRightAccount;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss:ms");
    private int _idx;
    List<BankAccount> _availableAccounts = new LinkedList<>();

    public Transactions(BankAccount leftAccount, BankAccount rightAccount, int idx) {
        mLeftAccount = leftAccount;
        mRightAccount = rightAccount;
        _idx = idx;
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
                if (Const.Mode == Const.PossibleMode.StaleMate) {
                    StaleMatePossibleVariation();
                } else if (Const.Mode == Const.PossibleMode.LaysDownAccWhenNotNeeded) {
                    StaleMateNotPossibleVariation();
                } else if (Const.Mode == Const.PossibleMode.Synchronized) {
                    SynchronizedVariation();
                } else if (Const.Mode == Const.PossibleMode.Pattern) {
                    PatternVariation();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private synchronized void PatternVariation() throws InterruptedException {
        if (Pattern.patternAllowsAction(_idx)) {

            if (mLeftAccount.canBeUsed()) {
                mLeftAccount.updateUsage(false);
                _availableAccounts.add(mLeftAccount);
                doAction(": Picked up left account");

                if (mRightAccount.canBeUsed()) {
                    mRightAccount.updateUsage(false);
                    _availableAccounts.add((mRightAccount));
                    doAction(": Picked up right account");

                    doAction(Const.TransactionMsg);

                    _availableAccounts.remove(mLeftAccount);
                    doAction(": transacted. Put down left account");
                    mLeftAccount.updateUsage(true);
                    _availableAccounts.remove(mRightAccount);
                    doAction(": transacted. Put down right account - back to thinking");
                    mRightAccount.updateUsage(true);
                } else {
                    _availableAccounts.remove(mLeftAccount);
                    doAction(": right could not be picked up, putting left back - back to thinking");
                    mLeftAccount.updateUsage(true);
                }

                Pattern.attemptMovePattern(_idx);
            }
        }
    }

    private synchronized void SynchronizedVariation() throws InterruptedException {
        if (mLeftAccount.canBeUsed()) {
            mLeftAccount.updateUsage(false);
            _availableAccounts.add(mLeftAccount);
            doAction(": Picked up left account");
            if (mRightAccount.canBeUsed()) {
                mRightAccount.updateUsage(false);
                _availableAccounts.add((mRightAccount));
                doAction(": Picked up right account");

                doAction(Const.TransactionMsg);

                _availableAccounts.remove(mLeftAccount);
                doAction(": transacted. Put down left account");
                mLeftAccount.updateUsage(true);
                _availableAccounts.remove(mRightAccount);
                doAction(": transacted. Put down right account - back to thinking");
                mRightAccount.updateUsage(true);
            } else {
                _availableAccounts.remove(mLeftAccount);
                doAction(": right could not be picked up, putting left back - back to thinking");
                mLeftAccount.updateUsage(true);
            }
        }
    }

    private void StaleMateNotPossibleVariation() throws InterruptedException {
        if (mLeftAccount.canBeUsed()) {
            mLeftAccount.updateUsage(false);
            _availableAccounts.add(mLeftAccount);
            doAction(": Picked up left account");

            if (mRightAccount.canBeUsed()) {
                mRightAccount.updateUsage(false);
                _availableAccounts.add((mRightAccount));
                doAction(": Picked up right account");

                doAction(Const.TransactionMsg);

                _availableAccounts.remove(mLeftAccount);
                doAction(": transacted. Put down left account");
                mLeftAccount.updateUsage(true);
                _availableAccounts.remove(mRightAccount);
                doAction(": transacted. Put down right account - back to thinking");
                mRightAccount.updateUsage(true);
            } else {
                _availableAccounts.remove(mLeftAccount);
                doAction(": right could not be picked up, putting left back - back to thinking");
                mLeftAccount.updateUsage(true);
            }
        }
    }

    private void StaleMatePossibleVariation() throws InterruptedException {
        if (mLeftAccount.canBeUsed()) {
            mLeftAccount.updateUsage(false);
            _availableAccounts.add(mLeftAccount);
            doAction(": Picked up left account");
        }
        if (mRightAccount.canBeUsed()) {
            mRightAccount.updateUsage(false);
            _availableAccounts.add((mRightAccount));
            doAction(": Picked up right account");
        }
        if (_availableAccounts.size() == 2) {
            doAction(Const.TransactionMsg);

            _availableAccounts.remove(mRightAccount);
            doAction(": Put down right account");
            mRightAccount.updateUsage(true);

            _availableAccounts.remove(mLeftAccount);
            doAction(": Put down left account - back to thinking");
            mLeftAccount.updateUsage(true);
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