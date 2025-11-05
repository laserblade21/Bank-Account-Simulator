/**
 * Checking accounts support frequent transactions with an optional per-withdrawal fee.
 */
public class CheckingAccount extends Account {
    private final double transactionFee;

    public CheckingAccount(String ownerName, String accountNumber, double initialBalance, double transactionFee) {
        super(ownerName, accountNumber, initialBalance);
        if (transactionFee < 0) {
            throw new IllegalArgumentException("Transaction fee cannot be negative");
        }
        this.transactionFee = transactionFee;
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    @Override
    public synchronized boolean withdraw(double amount) {
        return withdrawWithFee(amount, transactionFee);
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }
}
