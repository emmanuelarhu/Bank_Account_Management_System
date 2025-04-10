package banking;

public class TransactionNode {
    public Transaction transaction;
    public TransactionNode next;

    public TransactionNode(Transaction transaction) {
        this.transaction = transaction;
        this.next = null;
    }
}
