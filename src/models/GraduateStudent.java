package models;

import enums.GraduateLevel;
import exceptions.SupervisorIndexException;
import java.util.ArrayList;
import java.util.List;

public class GraduateStudent extends Student {
    private static final long serialVersionUID = 1L;

    private Teacher researchSupervisor;
    private GraduateLevel type;

    /** Список диплом. работ (требование: "diploma projects = published research papers") */
    private List<String> diplomaProjects;

    /**
     * @throws SupervisorIndexException если h-index руководителя меньше 3
     */
    public GraduateStudent(String id, String name, String login, String password,
                           GraduateLevel type, Teacher supervisor)
            throws SupervisorIndexException {
        super(id, name, login, password);
        this.type = type;
        this.diplomaProjects = new ArrayList<>();
        assignSupervisor(supervisor);
    }

    /**
     * Назначить/сменить научного руководителя.
     * @throws SupervisorIndexException если h-index < 3
     */
    public void assignSupervisor(Teacher supervisor) throws SupervisorIndexException {
        if (supervisor != null && supervisor.getHIndex() < 3)
            throw new SupervisorIndexException(supervisor.getName(), supervisor.getHIndex());
        this.researchSupervisor = supervisor;
    }

    public void addDiplomaProject(String title) {
        diplomaProjects.add(title);
        System.out.println(getLanguageMessage("Diploma project added: ", "Диплом. работа добавлена: ", "Диплом жұмысы қосылды: ") + title);
    }

    public void viewDiplomaProjects() {
        System.out.println("\n" + getLanguageMessage("=== Diploma Projects ===", "=== Дипломные работы ===", "=== Диплом жұмыстары ==="));
        if (diplomaProjects.isEmpty()) {
            System.out.println(getLanguageMessage("None yet.", "Пока нет.", "Әзірге жоқ.")); return;
        }
        for (int i = 0; i < diplomaProjects.size(); i++)
            System.out.println((i + 1) + ". " + diplomaProjects.get(i));
    }

    public Teacher getResearchSupervisor()     { return researchSupervisor; }
    public GraduateLevel getType()             { return type; }
    public List<String> getDiplomaProjects()   { return diplomaProjects; }

    @Override
    public String toString() {
        return String.format("GraduateStudent[ID='%s', Name='%s', Level=%s, Supervisor=%s, GPA=%.2f]",
                getStudentID(), getName(), type,
                researchSupervisor != null ? researchSupervisor.getName() : "None",
                getGpa());
    }
}



