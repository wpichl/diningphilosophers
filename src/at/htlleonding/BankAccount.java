package at.htlleonding;

public class BankAccount {
    private String mName = null;
    private String mShortName = null;
    private Integer mBalance = null;
    private boolean _canBeUsed;

    public BankAccount(String name,String shortName, Integer balance) {
        mName = name;
        mShortName = shortName;
        mBalance = balance;
        _canBeUsed = true;
    }

    public boolean canBeUsed()
    {
        return _canBeUsed;
    }
    public void updateUsage(boolean canBeUsedAgain)
    {
        _canBeUsed = canBeUsedAgain;
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
