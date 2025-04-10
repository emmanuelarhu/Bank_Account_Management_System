// BankAccountManagementSystem.java
package banking;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BankAccountManagementSystem extends Application {

    private Map<String, BankAccount> accounts = new HashMap<>();
    private ListView<String> accountListView = new ListView<>();
    private ListView<String> transactionListView = new ListView<>();
    private Label statusLabel = new Label("Welcome to Bank Account Management System");
    private Label balanceLabel = new Label("Balance: $0.00");
    private ComboBox<String> accountTypeComboBox;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    // Method to update the transaction list display
    private void updateTransactionList(BankAccount account) {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (account.getLastNTransactions(10).isEmpty()) {
            items.add("No transactions found for this account");
        } else {
            for (Transaction transaction : account.getLastNTransactions(10)) {
                items.add(transaction.toString());
            }
        }
        transactionListView.setItems(items);
    }

    // Method to update the accounts list display
    private void updateAccountList() {
        ObservableList<String> accountItems = FXCollections.observableArrayList();
        if (accounts.isEmpty()) {
            accountItems.add("No accounts created yet");
        } else {
            for (BankAccount account : accounts.values()) {
                accountItems.add(account.toString());
            }
        }
        accountListView.setItems(accountItems);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setting up the main layout with a more organized structure
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        // Top section with title
        Label appTitle = new Label("Bank Account Management System");
        appTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox titleBox = new HBox(appTitle);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 20, 0));
        mainLayout.setTop(titleBox);

        // Create tabs for better organization
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Tab 1: Create Account
        Tab createAccountTab = new Tab("Create Account");
        createAccountTab.setContent(createAccountSection());

        // Tab 2: Account Operations
        Tab operationsTab = new Tab("Account Operations");
        operationsTab.setContent(createOperationsSection());

        tabPane.getTabs().addAll(createAccountTab, operationsTab);
        mainLayout.setCenter(tabPane);

        // Status bar at bottom
        HBox statusBar = new HBox(10);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(10, 0, 0, 0));
        statusBar.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1px 0 0 0;");

        statusLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #6c757d;");
        statusBar.getChildren().add(statusLabel);

        mainLayout.setBottom(statusBar);

        // Set up the scene with a responsive layout
        Scene scene = new Scene(mainLayout, 800, 700);

        // Add inline CSS instead of external file
        scene.getStylesheets().add("data:text/css," +
                ".primary-button { -fx-background-color: #4e73df; -fx-text-fill: white; }" +
                ".success-button { -fx-background-color: #1cc88a; -fx-text-fill: white; }" +
                ".danger-button { -fx-background-color: #e74a3b; -fx-text-fill: white; }" +
                ".info-button { -fx-background-color: #36b9cc; -fx-text-fill: white; }");

        primaryStage.setTitle("Bank Account Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(700);
        primaryStage.show();

        // Initialize lists
        updateAccountList();
    }

    private VBox createAccountSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));

        // Create account section
        Label sectionTitle = new Label("Create New Account");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane createAccountGrid = new GridPane();
        createAccountGrid.setHgap(15);
        createAccountGrid.setVgap(15);
        createAccountGrid.setPadding(new Insets(20));
        createAccountGrid.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5px;");

        // Account type selection
        accountTypeComboBox = new ComboBox<>();
        accountTypeComboBox.getItems().addAll("Savings Account", "Current Account", "Fixed Deposit Account");
        accountTypeComboBox.setValue("Savings Account");
        accountTypeComboBox.setMaxWidth(Double.MAX_VALUE);

        // Account number field with validation
        TextField accountNumberField = new TextField();
        accountNumberField.setPromptText("Enter account number");
        accountNumberField.setMaxWidth(Double.MAX_VALUE);

        // Add a tooltip
        Tooltip accountNumberTooltip = new Tooltip("Enter a unique account number (numeric only)");
        accountNumberField.setTooltip(accountNumberTooltip);

        // Add validation to allow only numbers
        accountNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                accountNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Initial balance field with currency formatting
        TextField initialBalanceField = new TextField();
        initialBalanceField.setPromptText("Enter initial balance");
        initialBalanceField.setMaxWidth(Double.MAX_VALUE);

        Tooltip initialBalanceTooltip = new Tooltip("Enter the initial deposit amount");
        initialBalanceField.setTooltip(initialBalanceTooltip);

        // Only allow numbers and decimal point
        initialBalanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                initialBalanceField.setText(oldValue);
            }
        });

        // Additional parameters for account types
        TextField minBalanceField = new TextField();
        minBalanceField.setPromptText("Minimum balance");
        minBalanceField.setMaxWidth(Double.MAX_VALUE);
        minBalanceField.setText("500"); // Default value

        Tooltip minBalanceTooltip = new Tooltip("Minimum balance required for a Savings Account");
        minBalanceField.setTooltip(minBalanceTooltip);

        // Only allow numbers and decimal point
        minBalanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                minBalanceField.setText(oldValue);
            }
        });

        TextField overdraftLimitField = new TextField();
        overdraftLimitField.setPromptText("Overdraft limit");
        overdraftLimitField.setDisable(true);
        overdraftLimitField.setMaxWidth(Double.MAX_VALUE);
        overdraftLimitField.setText("1000"); // Default value

        Tooltip overdraftTooltip = new Tooltip("Maximum overdraft allowed for a Current Account");
        overdraftLimitField.setTooltip(overdraftTooltip);

        // Only allow numbers and decimal point
        overdraftLimitField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                overdraftLimitField.setText(oldValue);
            }
        });

        DatePicker maturityDatePicker = new DatePicker();
        maturityDatePicker.setValue(LocalDate.now().plusMonths(6));
        maturityDatePicker.setDisable(true);
        maturityDatePicker.setMaxWidth(Double.MAX_VALUE);

        Tooltip maturityTooltip = new Tooltip("Date when the Fixed Deposit Account matures");
        maturityDatePicker.setTooltip(maturityTooltip);

        // Prevent selection of past dates
        maturityDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0);
            }
        });

        // Show/hide appropriate fields based on account type
        accountTypeComboBox.setOnAction(e -> {
            String selectedType = accountTypeComboBox.getValue();

            minBalanceField.setDisable(!selectedType.equals("Savings Account"));
            overdraftLimitField.setDisable(!selectedType.equals("Current Account"));
            maturityDatePicker.setDisable(!selectedType.equals("Fixed Deposit Account"));

            // Set default values based on account type
            if (selectedType.equals("Savings Account")) {
                minBalanceField.setText("500");
            } else if (selectedType.equals("Current Account")) {
                overdraftLimitField.setText("1000");
            } else if (selectedType.equals("Fixed Deposit Account")) {
                maturityDatePicker.setValue(LocalDate.now().plusMonths(6));
            }
        });

        // Create Account button with improved styling
        Button createAccountButton = new Button("Create Account");
        createAccountButton.setMaxWidth(Double.MAX_VALUE);
        createAccountButton.getStyleClass().add("primary-button");

        createAccountButton.setOnAction(e -> {
            try {
                String accountNumber = accountNumberField.getText().trim();

                // Validate account number
                if (accountNumber.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter an account number");
                    return;
                }

                if (accounts.containsKey(accountNumber)) {
                    showAlert(Alert.AlertType.ERROR, "Duplicate Account", "Account number already exists");
                    return;
                }

                // Validate initial balance
                if (initialBalanceField.getText().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter an initial balance");
                    return;
                }

                double initialBalance = Double.parseDouble(initialBalanceField.getText().trim());
                if (initialBalance < 0) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Initial balance cannot be negative");
                    return;
                }

                String selectedType = accountTypeComboBox.getValue();
                BankAccount newAccount = null;

                switch (selectedType) {
                    case "Savings Account":
                        // Validate minimum balance
                        if (minBalanceField.getText().trim().isEmpty()) {
                            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a minimum balance");
                            return;
                        }

                        double minBalance = Double.parseDouble(minBalanceField.getText().trim());
                        if (minBalance < 0) {
                            showAlert(Alert.AlertType.ERROR, "Input Error", "Minimum balance cannot be negative");
                            return;
                        }

                        if (initialBalance < minBalance) {
                            showAlert(Alert.AlertType.ERROR, "Balance Error",
                                    "Initial balance must be greater than or equal to minimum balance");
                            return;
                        }

                        newAccount = new SavingsAccount(accountNumber, initialBalance, minBalance);
                        break;

                    case "Current Account":
                        // Validate overdraft limit
                        if (overdraftLimitField.getText().trim().isEmpty()) {
                            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter an overdraft limit");
                            return;
                        }

                        double overdraftLimit = Double.parseDouble(overdraftLimitField.getText().trim());
                        if (overdraftLimit < 0) {
                            showAlert(Alert.AlertType.ERROR, "Input Error", "Overdraft limit cannot be negative");
                            return;
                        }

                        newAccount = new CurrentAccount(accountNumber, initialBalance, overdraftLimit);
                        break;

                    case "Fixed Deposit Account":
                        if (maturityDatePicker.getValue() == null) {
                            showAlert(Alert.AlertType.ERROR, "Input Error", "Please select a maturity date");
                            return;
                        }

                        Date maturityDate = Date.from(maturityDatePicker.getValue()
                                .atStartOfDay(ZoneId.systemDefault()).toInstant());

                        if (initialBalance < 1000) {
                            showAlert(Alert.AlertType.ERROR, "Balance Error",
                                    "Fixed Deposit requires a minimum initial balance of $1,000");
                            return;
                        }

                        newAccount = new FixedDepositAccount(accountNumber, initialBalance, maturityDate);
                        break;
                }

                // Add transaction for initial deposit
                newAccount.addTransaction("Initial Deposit", initialBalance);

                accounts.put(accountNumber, newAccount);
                statusLabel.setText("✅ " + selectedType + " created successfully!");

                // Clear fields
                accountNumberField.clear();
                initialBalanceField.clear();
                minBalanceField.setText("500");  // Reset to default
                overdraftLimitField.setText("1000");  // Reset to default
                maturityDatePicker.setValue(LocalDate.now().plusMonths(6));  // Reset to default

                // Update the account list
                updateAccountList();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Account Created",
                        selectedType + " with number " + accountNumber + " created successfully!");

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numeric values");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Reset button
        Button resetButton = new Button("Reset Form");
        resetButton.setMaxWidth(Double.MAX_VALUE);

        resetButton.setOnAction(e -> {
            accountTypeComboBox.setValue("Savings Account");
            accountNumberField.clear();
            initialBalanceField.clear();
            minBalanceField.setText("500");
            overdraftLimitField.setText("1000");
            maturityDatePicker.setValue(LocalDate.now().plusMonths(6));
        });

        HBox buttonBox = new HBox(10, createAccountButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(createAccountButton, Priority.ALWAYS);
        HBox.setHgrow(resetButton, Priority.ALWAYS);

        // Add components to create account grid
        int row = 0;
        createAccountGrid.add(new Label("Account Type:"), 0, row);
        createAccountGrid.add(accountTypeComboBox, 1, row);

        row++;
        createAccountGrid.add(new Label("Account Number:"), 0, row);
        createAccountGrid.add(accountNumberField, 1, row);

        row++;
        createAccountGrid.add(new Label("Initial Balance:"), 0, row);
        createAccountGrid.add(initialBalanceField, 1, row);

        row++;
        createAccountGrid.add(new Label("Minimum Balance:"), 0, row);
        createAccountGrid.add(minBalanceField, 1, row);

        row++;
        createAccountGrid.add(new Label("Overdraft Limit:"), 0, row);
        createAccountGrid.add(overdraftLimitField, 1, row);

        row++;
        createAccountGrid.add(new Label("Maturity Date:"), 0, row);
        createAccountGrid.add(maturityDatePicker, 1, row);

        row++;
        createAccountGrid.add(buttonBox, 0, row, 2, 1);

        // Set column constraints for responsiveness
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        createAccountGrid.getColumnConstraints().addAll(col1, col2);

        // Add the accounts list view
        Label accountsTitle = new Label("Created Accounts");
        accountsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        accountListView.setPrefHeight(200);
        accountListView.setPlaceholder(new Label("No accounts created yet"));

        section.getChildren().addAll(sectionTitle, createAccountGrid, accountsTitle, accountListView);
        return section;
    }

    private VBox createOperationsSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));

        // Transactions section
        Label operationsTitle = new Label("Account Operations");
        operationsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane operationsGrid = new GridPane();
        operationsGrid.setHgap(15);
        operationsGrid.setVgap(15);
        operationsGrid.setPadding(new Insets(20));
        operationsGrid.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5px;");

        TextField operationAccountField = new TextField();
        operationAccountField.setPromptText("Enter account number");
        operationAccountField.setMaxWidth(Double.MAX_VALUE);

        // Add validation to allow only numbers
        operationAccountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                operationAccountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        TextField operationAmountField = new TextField();
        operationAmountField.setPromptText("Enter amount");
        operationAmountField.setMaxWidth(Double.MAX_VALUE);

        // Only allow numbers and decimal point
        operationAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                operationAmountField.setText(oldValue);
            }
        });

        // Create styled buttons
        Button depositButton = new Button("Deposit");
        depositButton.getStyleClass().add("success-button");

        Button withdrawButton = new Button("Withdraw");
        withdrawButton.getStyleClass().add("danger-button");

        Button checkBalanceButton = new Button("Check Balance");
        checkBalanceButton.getStyleClass().add("info-button");

        // Make buttons the same width
        depositButton.setMaxWidth(Double.MAX_VALUE);
        withdrawButton.setMaxWidth(Double.MAX_VALUE);
        checkBalanceButton.setMaxWidth(Double.MAX_VALUE);

        // Button actions with improved error handling
        depositButton.setOnAction(e -> {
            try {
                String accountNumber = operationAccountField.getText().trim();

                if (accountNumber.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter an account number");
                    return;
                }

                BankAccount account = accounts.get(accountNumber);
                if (account == null) {
                    showAlert(Alert.AlertType.ERROR, "Account Error", "Account not found");
                    return;
                }

                if (operationAmountField.getText().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter an amount");
                    return;
                }

                double amount = Double.parseDouble(operationAmountField.getText().trim());

                // Validate deposit amount
                if (amount <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Amount Error", "Deposit amount must be positive");
                    return;
                }

                // Handle fixed deposit account case
                if (account instanceof FixedDepositAccount) {
                    FixedDepositAccount fdAccount = (FixedDepositAccount) account;
                    if (!fdAccount.isMatured()) {
                        // Ask for confirmation before proceeding
                        boolean confirmed = showConfirmationAlert("Fixed Deposit Account",
                                "This is a Fixed Deposit Account that has not yet matured.\n" +
                                        "Maturity Date: " + dateFormat.format(fdAccount.getMaturityDate()) + "\n\n" +
                                        "Are you sure you want to make additional deposits to this account?");

                        if (!confirmed) {
                            return;
                        }
                    }
                }

                account.deposit(amount);
                account.addTransaction("Deposit", amount);
                updateTransactionList(account);
                balanceLabel.setText("Balance: " + currencyFormat.format(account.checkBalance()));
                statusLabel.setText("✅ Deposit of " + currencyFormat.format(amount) + " successful");

                operationAmountField.clear();

                showAlert(Alert.AlertType.INFORMATION, "Deposit Successful",
                        "Successfully deposited " + currencyFormat.format(amount) +
                                " to account " + accountNumber);

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid amount");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        withdrawButton.setOnAction(e -> {
            try {
                String accountNumber = operationAccountField.getText().trim();

                if (accountNumber.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter an account number");
                    return;
                }

                BankAccount account = accounts.get(accountNumber);
                if (account == null) {
                    showAlert(Alert.AlertType.ERROR, "Account Error", "Account not found");
                    return;
                }

                if (operationAmountField.getText().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter an amount");
                    return;
                }

                double amount = Double.parseDouble(operationAmountField.getText().trim());

                // Validate withdrawal amount
                if (amount <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Amount Error", "Withdrawal amount must be positive");
                    return;
                }

                // Handle fixed deposit account case
                if (account instanceof FixedDepositAccount) {
                    FixedDepositAccount fdAccount = (FixedDepositAccount) account;
                    if (!fdAccount.isMatured()) {
                        showAlert(Alert.AlertType.WARNING, "Early Withdrawal",
                                "Cannot withdraw from Fixed Deposit before maturity date: " +
                                        dateFormat.format(fdAccount.getMaturityDate()) + "\n\n" +
                                        "Early withdrawals may be subject to penalties. Please visit a branch.");
                        return;
                    }
                }

                boolean success = account.withdraw(amount);

                if (success) {
                    account.addTransaction("Withdrawal", -amount);
                    updateTransactionList(account);
                    balanceLabel.setText("Balance: " + currencyFormat.format(account.checkBalance()));
                    statusLabel.setText("✅ Withdrawal of " + currencyFormat.format(amount) + " successful");
                    operationAmountField.clear();

                    showAlert(Alert.AlertType.INFORMATION, "Withdrawal Successful",
                            "Successfully withdrew " + currencyFormat.format(amount) +
                                    " from account " + accountNumber);
                } else {
                    if (account instanceof SavingsAccount) {
                        SavingsAccount savingsAccount = (SavingsAccount) account;
                        showAlert(Alert.AlertType.ERROR, "Withdrawal Failed",
                                "Withdrawal would violate minimum balance requirement of " +
                                        currencyFormat.format(savingsAccount.getMinimumBalance()) +
                                        "\nCurrent balance: " + currencyFormat.format(account.checkBalance()));
                    } else if (account instanceof CurrentAccount) {
                        CurrentAccount currentAccount = (CurrentAccount) account;
                        showAlert(Alert.AlertType.ERROR, "Withdrawal Failed",
                                "Withdrawal would exceed overdraft limit of " +
                                        currencyFormat.format(currentAccount.getOverdraftLimit()) +
                                        "\nCurrent balance: " + currencyFormat.format(account.checkBalance()));
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Withdrawal Failed",
                                "Insufficient funds. Current balance: " +
                                        currencyFormat.format(account.checkBalance()));
                    }
                }

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid amount");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        checkBalanceButton.setOnAction(e -> {
            try {
                String accountNumber = operationAccountField.getText().trim();

                if (accountNumber.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter an account number");
                    return;
                }

                BankAccount account = accounts.get(accountNumber);
                if (account == null) {
                    showAlert(Alert.AlertType.ERROR, "Account Error", "Account not found");
                    return;
                }

                String accountType = "Bank Account";
                String additionalInfo = "";

                if (account instanceof SavingsAccount) {
                    accountType = "Savings Account";
                    SavingsAccount savingsAccount = (SavingsAccount) account;
                    additionalInfo = "Minimum Balance: " + currencyFormat.format(savingsAccount.getMinimumBalance());
                } else if (account instanceof CurrentAccount) {
                    accountType = "Current Account";
                    CurrentAccount currentAccount = (CurrentAccount) account;
                    additionalInfo = "Overdraft Limit: " + currencyFormat.format(currentAccount.getOverdraftLimit());
                } else if (account instanceof FixedDepositAccount) {
                    accountType = "Fixed Deposit Account";
                    FixedDepositAccount fdAccount = (FixedDepositAccount) account;
                    additionalInfo = "Maturity Date: " + dateFormat.format(fdAccount.getMaturityDate());
                    additionalInfo += "\nStatus: " + (fdAccount.isMatured() ? "Matured" : "Not Matured");
                }

                balanceLabel.setText("Balance: " + currencyFormat.format(account.checkBalance()));
                updateTransactionList(account);
                statusLabel.setText("Balance checked for account " + accountNumber);

                showAlert(Alert.AlertType.INFORMATION, "Account Information",
                        "Account Number: " + accountNumber +
                                "\nAccount Type: " + accountType +
                                "\nCurrent Balance: " + currencyFormat.format(account.checkBalance()) +
                                (additionalInfo.isEmpty() ? "" : "\n" + additionalInfo));

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Add transaction components to grid with improved layout
        int row = 0;
        operationsGrid.add(new Label("Account Number:"), 0, row);
        operationsGrid.add(operationAccountField, 1, row);

        row++;
        operationsGrid.add(new Label("Amount:"), 0, row);
        operationsGrid.add(operationAmountField, 1, row);

        row++;
        HBox operationButtons = new HBox(10, depositButton, withdrawButton, checkBalanceButton);
        operationButtons.setAlignment(Pos.CENTER);
        HBox.setHgrow(depositButton, Priority.ALWAYS);
        HBox.setHgrow(withdrawButton, Priority.ALWAYS);
        HBox.setHgrow(checkBalanceButton, Priority.ALWAYS);
        operationsGrid.add(operationButtons, 0, row, 2, 1);

        row++;
        balanceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        operationsGrid.add(balanceLabel, 0, row, 2, 1);

        // Set column constraints for responsiveness
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        operationsGrid.getColumnConstraints().addAll(col1, col2);

        // Transaction history section with title
        Label transactionsTitle = new Label("Transaction History");
        transactionsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        transactionListView.setPrefHeight(250);
        transactionListView.setPlaceholder(new Label("No transactions to display"));

        section.getChildren().addAll(operationsTitle, operationsGrid, transactionsTitle, transactionListView);
        return section;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        return false;
    }
}