package at.htlleonding;

import java.util.concurrent.Semaphore;

public class BankAccount {
    private String mName = null;
    private String mShortName = null;
    private Integer mBalance = null;
    private Semaphore _semaphore;

    public BankAccount(String name,String shortName, Integer balance) {
        mName = name;
        mShortName = shortName;
        mBalance = balance;
        _semaphore = new Semaphore(1);
    }

    public boolean canBeUsed()
    {
        return _semaphore.availablePermits() > 0;
    }
    public void grab()
    {
        try {
            _semaphore.acquire();
        }
        catch (Exception e) {
            throw new RuntimeException("Cant aquire semaphore");
        }
    }
    void release() {
        _semaphore.release();
    }
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Integer getBalance() {
        return mBalance;
    }

    public void setBalance(Integer balance) {
        mBalance = balance;
    }

    public String getShortName() {
        return mShortName;
    }
}
