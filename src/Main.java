import java.util.Scanner;
import java.util.UUID;

/**
 * Entry point for the console based bank account simulator.
 */
public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        TransactionLogger.initialize();
        System.out.println("========================================");
        System.out.println("      Welcome to Bank Simulator 3000    ");
        System.out.println("========================================\n");

        Account account = createAccount();
        boolean running = true;
        while (running) {
            printMenu(account);
            int choice = promptForInt("Select an option: ");
            switch (choice) {
                case 1 -> handleDeposit(account);
                case 2 -> handleWithdraw(account);
                case 3 -> displayBalance(account);
                case 4 -> {
                    if (account instanceof SavingsAccount savingsAccount) {
                        applyInterest(savingsAccount);
                    } else {
                        System.out.println("Invalid option. Please choose again.");
                    }
                }
                case 0 -> {
                    running = false;
                    System.out.println("Thank you for using the Bank Account Simulator. Goodbye!");
                }
                default -> System.out.println("Invalid option. Please choose again.");
            }
        }
        SCANNER.close();
    }

    private static Account createAccount() {
        System.out.println("Let's create your account.");
        System.out.print("Enter the account holder's name: ");
        String owner = SCANNER.nextLine().trim();
        while (owner.isEmpty()) {
            System.out.print("Name cannot be blank. Please enter the account holder's name: ");
            owner = SCANNER.nextLine().trim();
        }

        int type = promptForAccountType();
        double initialDeposit = promptForPositiveDouble("Enter the initial deposit amount: ");
        String accountNumber = generateAccountNumber();

        if (type == 1) {
            double interestRate = promptForPositiveDouble("Enter the annual interest rate (e.g. 2.5 for 2.5%): ") / 100.0;
            SavingsAccount account = new SavingsAccount(owner, accountNumber, initialDeposit, interestRate);
            TransactionLogger.logTransaction(account, "ACCOUNT_CREATED", initialDeposit, account.getBalance(), "Savings account opened");
            return account;
        } else {
            double fee = promptForNonNegativeDouble("Enter the transaction fee for withdrawals: ");
            CheckingAccount account = new CheckingAccount(owner, accountNumber, initialDeposit, fee);
            TransactionLogger.logTransaction(account, "ACCOUNT_CREATED", initialDeposit, account.getBalance(), "Checking account opened");
            return account;
        }
    }

    private static int promptForAccountType() {
        while (true) {
            System.out.println("Select account type:");
            System.out.println("1. Savings Account");
            System.out.println("2. Checking Account");
            int choice = promptForInt("Your choice: ");
            if (choice == 1 || choice == 2) {
                return choice;
            }
            System.out.println("Please choose 1 or 2.");
        }
    }

    private static void printMenu(Account account) {
        System.out.println("\n----------------------------------------");
        System.out.printf("Account: %s (%s)%n", account.getAccountNumber(), account.getAccountType());
        System.out.println("----------------------------------------");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. View Balance");
        if (account instanceof SavingsAccount) {
            System.out.println("4. Apply Monthly Interest");
        }
        System.out.println("0. Exit");
    }

    private static void handleDeposit(Account account) {
        double amount = promptForPositiveDouble("Enter deposit amount: ");
        try {
            account.deposit(amount);
            System.out.printf("Successfully deposited %.2f. New balance: %s%n", amount, account.formatBalance());
            TransactionLogger.logTransaction(account, "DEPOSIT", amount, account.getBalance(), "");
        } catch (IllegalArgumentException ex) {
            System.out.println("Deposit failed: " + ex.getMessage());
        }
    }

    private static void handleWithdraw(Account account) {
        double amount = promptForPositiveDouble("Enter withdrawal amount: ");
        try {
            boolean success = account.withdraw(amount);
            if (success) {
                String detail = "";
                if (account instanceof CheckingAccount checkingAccount) {
                    detail = String.format("Includes %.2f transaction fee", checkingAccount.getTransactionFee());
                }
                System.out.printf("Successfully withdrew %.2f. New balance: %s%n", amount, account.formatBalance());
                TransactionLogger.logTransaction(account, "WITHDRAWAL", amount, account.getBalance(), detail);
            } else {
                System.out.println("Insufficient funds for this withdrawal.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Withdrawal failed: " + ex.getMessage());
        }
    }

    private static void displayBalance(Account account) {
        System.out.println("Current balance: " + account.formatBalance());
    }

    private static void applyInterest(SavingsAccount account) {
        double startingBalance = account.getBalance();
        account.applyMonthlyInterest();
        double interestEarned = account.getBalance() - startingBalance;
        System.out.printf("Monthly interest of %.2f applied. New balance: %s%n", interestEarned, account.formatBalance());
        TransactionLogger.logTransaction(account, "INTEREST", interestEarned, account.getBalance(), "Monthly interest applied");
    }

    private static String generateAccountNumber() {
        return "AC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private static int promptForInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double promptForPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine();
            try {
                double value = Double.parseDouble(input.trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be greater than zero.");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double promptForNonNegativeDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine();
            try {
                double value = Double.parseDouble(input.trim());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Value cannot be negative.");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
