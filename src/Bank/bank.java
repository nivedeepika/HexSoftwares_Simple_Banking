package Bank;

import java.util.HashMap;
import java.util.Scanner;

class Account {
    private String accountNumber;
    private int pin;
    private double balance;
    private boolean isLocked;

    public Account(String accountNumber, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.isLocked = false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public boolean checkPin(int enteredPin) {
        return pin == enteredPin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void lockAccount() {
        isLocked = true;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void transferTo(Account otherAccount, double amount) {
        if (this.withdraw(amount)) {
            otherAccount.deposit(amount);
        }
    }
}

public class bank{
    private static HashMap<String, Account> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\nWelcome to the ATM Simulator");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Delete Account");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume the newline character
            
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    deleteAccount();
                    break;
                case 4:
                    System.out.println("Thank you for using the ATM Simulator. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void login() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Account does not exist.");
            return;
        }
        
        if (account.isLocked()) {
            System.out.println("Account is locked due to multiple failed login attempts.");
            return;
        }

        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter PIN: ");
            int enteredPin = scanner.nextInt();
            scanner.nextLine();  // consume the newline character
            
            if (account.checkPin(enteredPin)) {
                showMenu(account);
                return;
            } else {
                System.out.println("Incorrect PIN.");
                attempts++;
            }
        }

        account.lockAccount();
        System.out.println("Too many failed attempts. Your account has been locked.");
    }

    private static void createAccount() {
        System.out.print("Enter New Account Number: ");
        String accountNumber = scanner.nextLine();
        
        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account already exists.");
            return;
        }

        System.out.print("Create PIN: ");
        int pin = scanner.nextInt();
        scanner.nextLine();  // consume the newline character

        Account newAccount = new Account(accountNumber, pin, 0.0);
        accounts.put(accountNumber, newAccount);
        System.out.println("Account created successfully.");
    }

    private static void deleteAccount() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        if (!accounts.containsKey(accountNumber)) {
            System.out.println("Account does not exist.");
            return;
        }

        System.out.print("Enter PIN: ");
        int enteredPin = scanner.nextInt();
        scanner.nextLine();  // consume the newline character

        Account account = accounts.get(accountNumber);
        if (account.checkPin(enteredPin)) {
            accounts.remove(accountNumber);
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Incorrect PIN. Account deletion failed.");
        }
    }

    private static void showMenu(Account account) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume the newline character
            
            switch (choice) {
                case 1:
                    System.out.println("Your current balance is: $" + account.getBalance());
                    break;
                case 2:
                    depositMoney(account);
                    break;
                case 3:
                    withdrawMoney(account);
                    break;
                case 4:
                    transferMoney(account);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void depositMoney(Account account) {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // consume the newline character
        
        account.deposit(amount);
        System.out.println("Deposit successful. Your new balance is: $" + account.getBalance());
    }

    private static void withdrawMoney(Account account) {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // consume the newline character
        
        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful. Your new balance is: $" + account.getBalance());
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }

    private static void transferMoney(Account fromAccount) {
        System.out.print("Enter destination account number: ");
        String destinationAccountNumber = scanner.nextLine();
        
        Account toAccount = accounts.get(destinationAccountNumber);
        if (toAccount == null) {
            System.out.println("Destination account does not exist.");
            return;
        }

        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // consume the newline character
        
        if (amount > 0 && fromAccount.getBalance() >= amount) {
            fromAccount.transferTo(toAccount, amount);
            System.out.println("Transfer successful. Your new balance is: $" + fromAccount.getBalance());
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }
}

