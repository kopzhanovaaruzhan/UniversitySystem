package models;

public class GraduateStudent extends Student {
	private static final long serialVersionUID = 1L;

    private Teacher researchSupervisor;

    public GraduateStudent(String id, String name, String login, String password, Teacher supervisor) {
        super(id, name, login, password);
        this.setResearchSupervisor(supervisor);
    }

	public Teacher getResearchSupervisor() {
		return researchSupervisor;
	}

	public void setResearchSupervisor(Teacher researchSupervisor) {
		this.researchSupervisor = researchSupervisor;
	}
}