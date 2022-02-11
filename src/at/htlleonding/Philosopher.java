package at.htlleonding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Philosopher implements Runnable {
    private Fork _left;
    private Fork _right;
    private DateTimeFormatter _dateTimeFormat = DateTimeFormatter.ofPattern("mm:ss:ms");
    private Semaphore _semaphore;
    private int _idx;
    List<Fork> _availableAccounts = new LinkedList<>();

    public Philosopher(Fork givenLeft, Fork givenRight, int idx) {
        _left = givenLeft;
        _right = givenRight;
        _idx = idx;
        _semaphore = new Semaphore(1);
    }

    public Fork getLeft() {
        return _left;
    }

    public Fork getRight() {
        return _right;
    }

    @Override
    public void run() {
        try {
            doAction(Const.thinkingAlias);
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
            if (_left.canBeUsed()) {
                _left.grab();
                _availableAccounts.add(_left);
                doAction(Const.pickedUpAlias+ " left " + Const.forkAlias);

                if (_right.canBeUsed()) {
                    _right.grab();
                    _availableAccounts.add((_right));
                    doAction(Const.pickedUpAlias+" right " + Const.forkAlias);

                    doAction(Const.TransactionMsg);

                    _availableAccounts.remove(_left);
                    doAction(Const.ateAlias + ". "+Const.putDownAlias+" left " + Const.forkAlias);
                    _left.release();
                    _availableAccounts.remove(_right);
                    doAction(Const.ateAlias + ". "+Const.putDownAlias+" right " + Const.forkAlias +
                            " - back to " + Const.thinkingAlias);
                    _right.release();
                } else {
                    _availableAccounts.remove(_left);
                    doAction("Right " + Const.forkAlias +
                            " could not be " + Const.pickedUpAlias + ", "+Const.puttingAlias+" left " + Const.forkAlias +
                            " "+Const.backAlias+" - back to " + Const.thinkingAlias);
                    _left.release();
                }

                Pattern.attemptMovePattern(_idx);
            }
        }
    }

    private synchronized void SemaphoreVariation() throws InterruptedException {
        if (_semaphore.availablePermits() > 0) {
            _semaphore.acquire();
            if (_left.canBeUsed()) {
                _left.grab();
                _availableAccounts.add(_left);
                doAction(Const.pickedUpAlias+" left " + Const.forkAlias);
                if (_right.canBeUsed()) {
                    _right.grab();
                    _availableAccounts.add((_right));
                    doAction(Const.pickedUpAlias+" right " + Const.forkAlias);

                    doAction(Const.TransactionMsg);

                    _availableAccounts.remove(_left);
                    doAction(Const.ateAlias + ". "+Const.putDownAlias+" left " + Const.forkAlias);
                    _left.release();
                    _availableAccounts.remove(_right);
                    doAction(Const.ateAlias + ". "+Const.putDownAlias +" right " + Const.forkAlias +
                            " - back to " + Const.thinkingAlias);
                    _right.release();
                } else {
                    _availableAccounts.remove(_left);
                    _left.release();
                    doAction("Right " + Const.forkAlias +
                            " could not be " + Const.pickedUpAlias + ", "+Const.puttingAlias+" left " + Const.forkAlias +
                            " "+Const.backAlias+" - back to " + Const.thinkingAlias);
                }
            }
            _semaphore.release();
        }
    }

    private void LayingDownIfNotNeededVariation() throws InterruptedException {
        if (_left.canBeUsed()) {
            _left.grab();
            _availableAccounts.add(_left);
            doAction(Const.pickedUpAlias+" left " + Const.forkAlias);

            if (_right.canBeUsed()) {
                _right.grab();
                _availableAccounts.add((_right));
                doAction(Const.pickedUpAlias+" right " + Const.forkAlias);

                doAction(Const.TransactionMsg);

                _availableAccounts.remove(_left);
                doAction(Const.ateAlias + ". "+Const.putDownAlias +" left " + Const.forkAlias);
                _left.release();
                _availableAccounts.remove(_right);
                doAction(Const.ateAlias + ". "+Const.putDownAlias +" right " + Const.forkAlias + " - back to " + Const.thinkingAlias);
                _right.release();
            } else {
                _availableAccounts.remove(_left);
                doAction("Right " + Const.forkAlias +
                        " could not be " + Const.pickedUpAlias + ", putting left " + Const.forkAlias +
                        " back - back to " + Const.thinkingAlias);
                _left.release();
            }
        }
    }

    private void DeadLockVariation() throws InterruptedException {
        if (_left.canBeUsed()) {
            _left.grab();
            _availableAccounts.add(_left);
            doAction(Const.pickedUpAlias+" left " + Const.forkAlias);
        }
        if (_right.canBeUsed()) {
            _right.grab();
            _availableAccounts.add((_right));
            doAction(Const.pickedUpAlias+" right " + Const.forkAlias);
        }
        if (_availableAccounts.size() == 2) {
            doAction(Const.TransactionMsg);

            _availableAccounts.remove(_right);
            doAction(Const.putDownAlias+"right " + Const.forkAlias);
            _right.release();

            _availableAccounts.remove(_left);
            doAction(Const.putDownAlias+" left " + Const.forkAlias + " - back to " + Const.thinkingAlias);
            _left.release();
        }
    }

    private void doAction(String action) throws InterruptedException {
        String accounts = " ";
        for (Fork curr : _availableAccounts
        ) {
            if (curr != null) {
                accounts += curr.getName() + " ";
            }
        }
        if (accounts == " ") {
            accounts = " No " + Const.forkAlias;
        }
        boolean isTransactionMsg = false;
        if (action == Const.TransactionMsg) {
            isTransactionMsg = true;
        }

        PrintCurrentStates.updateState(_idx,
                Thread.currentThread().getName() + ":" + accounts,
                Thread.currentThread().getName() + " " + getCurrentTime() + "; " + action,
                isTransactionMsg);
        Thread.sleep(((int) (Math.random() * 100)));
    }

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        return _dateTimeFormat.format(now);
    }
}