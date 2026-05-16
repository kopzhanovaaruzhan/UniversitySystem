package models;

import enums.GraduateLevel;
import exceptions.SupervisorIndexException;
import java.util.ArrayList;
import java.util.List;

public class GraduateStudent extends Student {
    private static final long serialVersionUID = 1L;

    private Teacher researchSupervisor;
    private GraduateLevel type;
    private List<String> diplomaProjects;

    public GraduateStudent(String id, String name, String login, String password,
                           GraduateLevel type, Teacher supervisor) throws SupervisorIndexException {
        super(id, name, login, password);
        this.type = type;
        this.diplomaProjects = new ArrayList<>();
        assignSupervisor(supervisor);
    }

    public void assignSupervisor(Teacher supervisor) throws SupervisorIndexException {
        if (supervisor != null && supervisor.getHIndex() < 3)
            throw new SupervisorIndexException(supervisor.getName(), supervisor.getHIndex());
        this.researchSupervisor = supervisor;
    }

    public void addDiplomaProjectLogic(String title) {
        if (title != null && !title.isEmpty()) {
            diplomaProjects.add(title);
        }
    }

    public Teacher getResearchSupervisor()   { return researchSupervisor; }
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




