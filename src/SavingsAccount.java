/**
 * Savings accounts support accruing interest on the current balance.
 */
public class SavingsAccount extends Account {
    private final double annualInterestRate;

    public SavingsAccount(String ownerName, String accountNumber, double initialBalance, double annualInterestRate) {
        super(ownerName, accountNumber, initialBalance);
        if (annualInterestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        this.annualInterestRate = annualInterestRate;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    /**
     * Applies a single month's worth of interest to the balance.
     */
    public void applyMonthlyInterest() {
        double monthlyRate = annualInterestRate / 12.0;
        super.applyInterest(monthlyRate);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }
}
