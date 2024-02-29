import com.example.service.BankCardService;
import com.example.service.ClientService;
import com.example.scheduler.NotificationScheduler;
import com.example.ui.BankAppSwing;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankAppSwing bankAppSwing = new BankAppSwing();
            bankAppSwing.setVisible(true);

            ClientService clientService = bankAppSwing.getBankAppPanel().getClientService();
            BankCardService bankCardService = bankAppSwing.getBankAppPanel().getBankCardService();

            // Вывод кеша в консоль (имена и номера карт)
//            Thread cacheLoggerThread = new Thread(new CacheLogger(clientService, bankCardService));
//            cacheLoggerThread.start();

            NotificationScheduler notificationScheduler = new NotificationScheduler(bankCardService, bankAppSwing);
            Thread notificationThread = new Thread(notificationScheduler);
            notificationThread.start();
        });
    }
}


