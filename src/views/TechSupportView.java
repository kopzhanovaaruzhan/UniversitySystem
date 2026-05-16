package views;

import java.io.IOException;
import java.util.List;
import models.TechSupportSpecialist;
import models.TechSupportRequest;
import controllers.SupportController;

public class TechSupportView extends BaseView {

    public static void show(TechSupportSpecialist specialist) throws IOException {
        boolean running = true;
        while (running) {
            printMenu(specialist);
            String choice = reader.readLine().trim();
            switch (choice) {
                case "1" -> viewAndManageRequests(specialist);
                case "2" -> switchLang(specialist);
                case "0" -> running = false;
                default  -> System.out.println("Unknown option.");
            }
        }
    }

    private static void printMenu(TechSupportSpecialist specialist) {
        String lang = specialist.getCurrentLanguage().name();
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.printf ("║  %-37s║%n", specialist.getName() + " [SUPPORT]");
        System.out.println("╠═══════════════════════════════════════╣");
        option("1", specialist.getLanguageMessage("View & Manage Requests", "Управление запросами", "Suraunystardy basqaru"));
        option("2", specialist.getLanguageMessage("Language: ", "Язык: ", "Tіl: ") + lang);
        option("0", specialist.getLanguageMessage("Logout", "Выйти", "Şyğu"));
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print(specialist.getLanguageMessage("Choice: ", "Выбор: ", "Tañdau: "));
    }

    private static void option(String key, String label) {
        System.out.printf("║  %s - %-33s║%n", key, label);
    }

    private static void viewAndManageRequests(TechSupportSpecialist specialist) throws IOException {
        // Получаем список запросов из модели (и обновляем статусы NEW -> VIEWED)
        List<TechSupportRequest> activeRequests = specialist.getAndResetRequests();

        if (activeRequests.isEmpty()) {
            System.out.println(specialist.getLanguageMessage(
                    "No support requests found.",
                    "Нет запросов в техподдержку.",
                    "Tehnıkalyq qoldaý suranystary joq."
            ));
            return;
        }

        System.out.println("\n" + specialist.getLanguageMessage("=== Active Requests ===", "=== Активные запросы ===", "=== Belsendі suranystar ==="));
        for (int i = 0; i < activeRequests.size(); i++) {
            System.out.println((i + 1) + ". " + activeRequests.get(i));
        }

        System.out.print(specialist.getLanguageMessage(
                "\nEnter request number to manage (0 to back): ", 
                "\nВведите номер запроса для управления (0 для отмены): ", 
                "\nBasqarý úşіn suranys nómіrіn engіzіñіz (0 bas tartý): "));
        
        try {
            int idx = Integer.parseInt(reader.readLine().trim()) - 1;
            if (idx == -1) return;

            if (idx >= 0 && idx < activeRequests.size()) {
                TechSupportRequest targetRequest = activeRequests.get(idx);
                manageRequestMenu(specialist, targetRequest);
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void manageRequestMenu(TechSupportSpecialist specialist, TechSupportRequest request) throws IOException {
        System.out.println("\n------------------------------------");
        System.out.println("Selected: " + request.getDescription());
        System.out.println("Current Status: " + request.getStatus());
        System.out.println("------------------------------------");
        System.out.println("1 - " + specialist.getLanguageMessage("Accept Request", "Принять в работу", "Jumysqa alu"));
        System.out.println("2 - " + specialist.getLanguageMessage("Reject Request", "Отклонить запрос", "Suranystan bas tartu"));
        System.out.println("3 - " + specialist.getLanguageMessage("Mark as DONE", "Выполнено", "Oryndaldy"));
        System.out.println("0 - " + specialist.getLanguageMessage("Back", "Назад", "Arqağa"));
        System.out.print("> ");

        switch (reader.readLine().trim()) {
            case "1" -> {
                SupportController.acceptRequest(specialist, request);
                successMsg(specialist.getLanguageMessage("Request accepted.", "Запрос принят в работу.", "Suranys qabyldandy."));
            }
            case "2" -> {
                SupportController.rejectRequest(specialist, request);
                successMsg(specialist.getLanguageMessage("Request rejected.", "Запрос отклонен.", "Suranystan bas tartyldy."));
            }
            case "3" -> {
                SupportController.markAsDone(specialist, request);
                successMsg(specialist.getLanguageMessage("Request marked as DONE!", "Запрос успешно выполнен!", "Suranys sáttі oryndaldy!"));
            }
            default -> System.out.println("No changes made.");
        }
    }
}




