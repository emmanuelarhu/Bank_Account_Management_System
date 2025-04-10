package banking;
import java.util.Date;

// Fixed Deposit Account implementation
class FixedDepositAccount extends BankAccount {
    private Date maturityDate;
    private boolean matured;

    public FixedDepositAccount(String accountNumber, double depositAmount, Date maturityDate) {
        super(accountNumber, depositAmount);
        this.maturityDate = maturityDate;
        this.matured = false;
    }

    @Override
    public boolean withdraw(double amount) {
        // Can only withdraw if matured
        if (!isMatured()) {
            return false;
        }

        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public void deposit(double amount) {
        // No additional deposits allowed in fixed deposit
    }

    public boolean isMatured() {
        // Check if current date is after maturity date
        matured = new Date().after(maturityDate);
        return matured;
    }

    public void applyMaturityInterest(double rate) {
        if (isMatured() && !matured) {
            double interest = balance * rate;
            balance += interest;
            addTransaction("Maturity Interest", interest);
            matured = true;
        }
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    @Override
    public String getAccountType() {
        return "Fixed Deposit Account";
    }
}