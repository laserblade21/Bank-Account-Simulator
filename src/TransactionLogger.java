import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class that persists transactions to a simple text file.
 */
public final class TransactionLogger {
    private static final Path LOG_PATH = Path.of("transactions.txt");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String HEADER = "timestamp,accountNumber,owner,accountType,action,amount,balanceAfter,details";

    private TransactionLogger() {
    }

    public static void initialize() {
        try {
            if (Files.notExists(LOG_PATH)) {
                Files.createFile(LOG_PATH);
                try (BufferedWriter writer = Files.newBufferedWriter(LOG_PATH, StandardOpenOption.APPEND)) {
                    writer.write(HEADER);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to prepare transaction log: " + e.getMessage());
        }
    }

    public static void logTransaction(Account account, String action, double amount, double balanceAfter, String details) {
        String safeDetails = details == null ? "" : details;
        String entry = String.format(
                "%s,%s,%s,%s,%s,%.2f,%.2f,%s",
                LocalDateTime.now().format(FORMATTER),
                account.getAccountNumber(),
                account.getOwnerName(),
                account.getAccountType(),
                action,
                amount,
                balanceAfter,
                safeDetails.replace(',', ';'));
        try (BufferedWriter writer = Files.newBufferedWriter(LOG_PATH, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to log transaction: " + e.getMessage());
        }
    }
}
