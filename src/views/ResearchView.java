package views;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import models.ResearchDecorator;
import models.ResearchPaper;
import models.ResearchProject;
import core.DBContext;

public class ResearchView extends BaseView {

    public static void showMenu(ResearchDecorator researcher) throws IOException {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.printf ("║  %-37s║%n", researcher.getLanguageMessage("RESEARCH CABINET", "КАБИНЕТ ИССЛЕДОВАТЕЛЯ", "ЗЕРТТЕУШІ КАБИНЕТІ"));
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.printf ("║  %-37s║%n", researcher.getLanguageMessage("User: ", "Пользователь: ", "Пайдаланушы: ") + researcher.getName());
            System.out.printf ("║  %-37s║%n", "h-index: " + researcher.calculateHIndex());
            System.out.println("╠═══════════════════════════════════════╣");
            
            option("1", researcher.getLanguageMessage("View My Papers", "Мои научные статьи", "Ғылыми мақалаларым"));
            option("2", researcher.getLanguageMessage("Publish New Paper", "Опубликовать новую статью", "Жаңа мақала жариялау"));
            option("3", researcher.getLanguageMessage("View Research Projects", "Исследовательские проекты", "Зерттеу жобалары"));
            option("4", researcher.getLanguageMessage("Sort Papers", "Сортировать статьи", "Мақалаларды сұрыптау"));
            option("5", researcher.getLanguageMessage("Join a Project", "Вступить в проект", "Жобаға қосылу"));
            option("0", researcher.getLanguageMessage("Back to Main Menu", "Назад в главное меню", "Басты мәзірге қайту"));
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print(researcher.getLanguageMessage("Selection: ", "Выбор: ", "Таңдау: "));

            String choice = reader.readLine().trim();

            if (choice.equals("1")) viewPapers(researcher);
            else if (choice.equals("2")) publishPaperMenu(researcher);
            else if (choice.equals("3")) viewProjects(researcher);
            else if (choice.equals("4")) sortPapersMenu(researcher);
            else if (choice.equals("5")) joinProjectMenu(researcher);
            else if (choice.equals("0")) break;
            else System.out.println(researcher.getLanguageMessage("Invalid choice!", "Неверный выбор!", "Қате таңдау!"));
        }
    }

    private static void option(String key, String label) {
        System.out.printf("║  %s - %-33s║%n", key, label);
    }

    private static void viewPapers(ResearchDecorator researcher) {
        List<ResearchPaper> papers = researcher.getPapers();
        if (papers.isEmpty()) {
            System.out.println(researcher.getLanguageMessage("No papers published yet.", "Статей пока нет.", "Мақалалар әлі жоқ."));
            return;
        }
        System.out.println("\n--- " + researcher.getLanguageMessage("List of Papers", "Список статей", "Мақалалар тізімі") + " ---");
        for (int i = 0; i < papers.size(); i++) {
            System.out.println((i + 1) + ". " + papers.get(i).toString());
        }
    }

    private static void publishPaperMenu(ResearchDecorator researcher) throws IOException {
        System.out.print(researcher.getLanguageMessage("Title: ", "Название: ", "Атауы: "));
        String title = reader.readLine().trim();
        System.out.print(researcher.getLanguageMessage("Journal/Conference: ", "Журнал/Конференция: ", "Журнал/Конференция: "));
        String journal = reader.readLine().trim();
        System.out.print(researcher.getLanguageMessage("Pages: ", "Кол-во страниц: ", "Бет саны: "));
        
        try {
            int pages = Integer.parseInt(reader.readLine().trim());
            ResearchPaper paper = new ResearchPaper(title, researcher.getName(), journal, pages);
            researcher.publishPaper(paper);
            
            successMsg(researcher.getLanguageMessage("Paper published!", "Статья опубликована!", "Мақала жарияланды!"));
            DBContext.save();
        } catch (NumberFormatException e) {
            System.out.println(researcher.getLanguageMessage("Invalid number format.", "Неверный формат числа.", "Қате сан форматы."));
        }
    }

    private static void viewProjects(ResearchDecorator researcher) {
        List<ResearchProject> projects = DBContext.getProjects().stream()
                .filter(p -> p.getMembers().contains(researcher))
                .toList();

        if (projects.isEmpty()) {
            System.out.println(researcher.getLanguageMessage("Not participating in any projects.", "Вы не участвуете в проектах.", "Жобаларға қатыспайсыз."));
            return;
        }
        projects.forEach(p -> System.out.println("- " + p.getTopic()));
    }

    private static void sortPapersMenu(ResearchDecorator researcher) throws IOException {
        List<ResearchPaper> papers = researcher.getPapers();
        if (papers.isEmpty()) {
            System.out.println(researcher.getLanguageMessage("No papers to sort.", "Нет статей для сортировки.", "Сұрыптайтын мақалалар жоқ."));
            return;
        }

        System.out.println("1 - " + researcher.getLanguageMessage("By Date", "По дате", "Күні бойынша"));
        System.out.println("2 - " + researcher.getLanguageMessage("By Citations", "По цитированиям", "Дәйексөз бойынша"));
        System.out.print("> ");
        String choice = reader.readLine().trim();

        if (choice.equals("1")) {
            papers.sort(Comparator.comparing(ResearchPaper::getDatePublished).reversed());
        } else if (choice.equals("2")) {
            papers.sort(Comparator.comparingInt(ResearchPaper::getCitationsCount).reversed());
        }
        viewPapers(researcher);
    }
    
    private static void joinProjectMenu(ResearchDecorator researcher) throws IOException {
        List<ResearchProject> allProjects = DBContext.getProjects();
        
        List<ResearchProject> availableProjects = allProjects.stream()
                .filter(p -> !p.getMembers().contains(researcher))
                .toList();

        if (availableProjects.isEmpty()) {
            System.out.println(researcher.getLanguageMessage("No new projects available to join.", 
                                                              "Нет доступных новых проектов.", 
                                                              "Қосылуға болатын жаңа жобалар жоқ."));
            return;
        }

        System.out.println("\n--- " + researcher.getLanguageMessage("Available Projects", "Доступные проекты", "Қолайлы жобалар") + " ---");
        for (int i = 0; i < availableProjects.size(); i++) {
            System.out.println((i + 1) + ". " + availableProjects.get(i).getTopic());
        }
        System.out.println("0. " + researcher.getLanguageMessage("Cancel", "Отмена", "Бас тарту"));
        System.out.print(researcher.getLanguageMessage("Select project to join: ", "Выберите проект: ", "Жобаны таңдаңыз: "));
        
        try {
            String input = reader.readLine().trim();
            int choice = Integer.parseInt(input);

            if (choice == 0) return;

            if (choice > 0 && choice <= availableProjects.size()) {
                ResearchProject selectedProject = availableProjects.get(choice - 1);
                researcher.joinProject(selectedProject);
                
                successMsg(researcher.getLanguageMessage("Successfully joined the project!", 
                                                          "Вы успешно вступили в проект!", 
                                                          "Жобаға сәтті қосылдыңыз!"));
                DBContext.save();
            } else {
                System.out.println(researcher.getLanguageMessage("Invalid selection.", "Неверный выбор.", "Қате таңдау."));
            }
        } catch (NumberFormatException e) {
            System.out.println(researcher.getLanguageMessage("Please enter a number.", "Пожалуйста, введите число.", "Сан енгізіңіз."));
        }
    }
}




