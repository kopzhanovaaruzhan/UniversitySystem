package models;
import java.util.*;
public class Teacher extends Employee implements Researcher {
    private TeacherType type;
    private List<Complaint> complaints = new ArrayList<>();
    private boolean researcher;
    public Teacher(String id, String name, String login, String password, double salary, TeacherType type) {
        super(id, name, login, password, salary);
        this.type = type;

        if (type == TeacherType.PROFESSOR) {
            this.researcher = true;
        }
    }
    public void putMark(Student student, Course course, Mark mark) {
        student.addMark(course, mark);

        System.out.println(getLanguageMessage(
                "Mark was successfully added.",
                "Оценка успешно поставлена.",
                "Баға сәтті қойылды."
        ));
    }
    public Complaint createComplaint(Student student, String text, UrgencyLevel level) {
        Complaint complaint = new Complaint(this, student, text, level);
        complaints.add(complaint);

        System.out.println(getLanguageMessage(
                "Complaint was sent to dean.",
                "Жалоба была отправлена декану.",
                "Шағым деканға жіберілді."
        ));

        return complaint;
    }
    public boolean isResearcher() {
        return researcher;
    }

    public void publishPaper(String title) {
        if (researcher) {
            System.out.println(getLanguageMessage(
                    "Research paper published: " + title,
                    "Научная статья опубликована: " + title,
                    "Ғылыми мақала жарияланды: " + title
            ));
        } else {
            System.out.println(getLanguageMessage(
                    "This teacher is not a researcher.",
                    "Этот преподаватель не является исследователем.",
                    "Бұл оқытушы зерттеуші емес."
            ));
        }
    }
}