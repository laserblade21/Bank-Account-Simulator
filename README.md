# Bank Account Simulator

A console-based Java application for practicing object-oriented programming concepts such as classes, inheritance, encapsulation, and polymorphism. The simulator allows you to create checking or savings accounts, perform deposits and withdrawals, apply interest on savings, and persist every transaction to a `transactions.txt` log file.

## Project Structure

```
bank-account-simulator/
├── src/
│   ├── Account.java
│   ├── CheckingAccount.java
│   ├── Main.java
│   ├── SavingsAccount.java
│   └── TransactionLogger.java
└── transactions.txt
```

## Features

- Create either a savings account with an annual interest rate or a checking account with a withdrawal fee.
- Perform deposits and withdrawals with validation against negative values and insufficient funds.
- View the current balance at any time.
- Apply monthly interest for savings accounts.
- Append all operations to `transactions.txt` with timestamps for easy auditing.

## Getting Started

1. **Compile the source files**

   ```bash
   javac src/*.java
   ```

2. **Run the simulator**

   ```bash
   java -cp src Main
   ```

3. **Follow the on-screen prompts** to set up an account and start performing transactions.

## Transaction Log

The simulator writes a comma-separated log entry for every account creation and transaction. The default log file is located at the repository root (`transactions.txt`). Each entry captures the timestamp, account number, owner, account type, action, amount, resulting balance, and any contextual details such as transaction fees or interest.

## Requirements

- Java 17 or newer (earlier versions that support the used APIs should also work).

Feel free to extend the simulator with additional account types, persistence options, or a graphical interface.
