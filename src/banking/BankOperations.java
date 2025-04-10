package banking;

public interface BankOperations {
    void deposit(double amount);
    boolean withdraw(double amount); // ✅ change here
    double checkBalance(); // or getBalance() if you renamed it
}


