import java.text.NumberFormat;
import java.util.Locale;

/**
 * Base class that represents a simple bank account. It encapsulates common state and
 * behaviour shared by all concrete account implementations.
 */
public abstract class Account {
    private final String ownerName;
    private final String accountNumber;
    private double balance;

    protected Account(String ownerName, String accountNumber, double initialBalance) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner name must be provided");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.ownerName = ownerName.trim();
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public synchronized double getBalance() {
        return balance;
    }

    /**
     * Adds the provided amount to the balance.
     *
     * @param amount the amount to deposit
     */
    public synchronized void deposit(double amount) {
        ensurePositiveAmount(amount);
        balance += amount;
    }

    /**
     * Attempts to withdraw the provided amount from the balance.
     *
     * @param amount the amount to withdraw
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     */
    public synchronized boolean withdraw(double amount) {
        ensurePositiveAmount(amount);
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    protected synchronized boolean withdrawWithFee(double amount, double fee) {
        ensurePositiveAmount(amount);
        if (fee < 0) {
            throw new IllegalArgumentException("Fee cannot be negative");
        }
        double total = amount + fee;
        if (total > balance) {
            return false;
        }
        balance -= total;
        return true;
    }

    protected synchronized void applyInterest(double rate) {
        if (rate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        balance += balance * rate;
    }

    private void ensurePositiveAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    public abstract String getAccountType();

    public String formatBalance() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(getBalance());
    }
}
