package banking;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

// Abstract class for bank accounts
abstract class BankAccount implements BankOperations {
    protected String accountNumber;
    protected double balance;
    protected TransactionNode head; // Head of transaction linked list

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.head = null;

        if (initialBalance > 0) {
            addTransaction("Initial Deposit", initialBalance);
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public double checkBalance() {
        return balance;
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    // Abstract method that will be implemented differently by each account type
    @Override
    public abstract boolean withdraw(double amount);

    // Add transaction to linked list (at the beginning - most recent first)
    public void addTransaction(String type, double amount) {
        Transaction transaction = new Transaction(type, amount, new Date()); // add current date
        TransactionNode newNode = new TransactionNode(transaction);

        if (head == null) {
            head = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
    }

    // Get last N transactions using the linked list
    public List<Transaction> getLastNTransactions(int n) {
        List<Transaction> transactions = new ArrayList<>();
        TransactionNode current = head;
        int count = 0;

        while (current != null && count < n) {
            transactions.add(current.transaction);
            current = current.next;
            count++;
        }

        return transactions;
    }

    // Method to get account type
    public abstract String getAccountType();

    @Override
    public String toString() {
        return getAccountType() + " - " + accountNumber + " - Balance: $" + balance;
    }
}
