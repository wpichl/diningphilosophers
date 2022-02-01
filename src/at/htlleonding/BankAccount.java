package at.htlleonding;

public class BankAccount {
    private String mName = null;
    private Integer mBalance = null;

    public BankAccount(String name, Integer balance) {
        mName = name;
        mBalance = balance;
    }

    public BankAccount() {

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
}
