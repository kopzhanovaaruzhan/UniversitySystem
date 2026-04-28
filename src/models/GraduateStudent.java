package models;

public class GraduateStudent extends Student {
	private static final long serialVersionUID = 1L;

    private Teacher researchSupervisor;
    private GraduateLevel type;

    public GraduateStudent(String id, String name, String login, String password, GraduateLevel type, Teacher supervisor) {
        super(id, name, login, password);
        this.type = type;
        this.setResearchSupervisor(supervisor);
    }

	public Teacher getResearchSupervisor() {
		return researchSupervisor;
	}

	public void setResearchSupervisor(Teacher researchSupervisor) {
		this.researchSupervisor = researchSupervisor;
	}

	public GraduateLevel getType() {
		return type;
	}
}