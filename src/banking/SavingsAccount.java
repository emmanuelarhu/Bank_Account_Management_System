package banking;


// Savings Account implementation
class SavingsAccount extends BankAccount {
    private double minimumBalance;

    public SavingsAccount(String accountNumber, double initialBalance, double minimumBalance) {
        super(accountNumber, initialBalance);
        this.minimumBalance = minimumBalance;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= minimumBalance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public void calculateInterest(double rate) {
        double interest = balance * rate;
        balance += interest;
        addTransaction("Interest Credit", interest);
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }
}
