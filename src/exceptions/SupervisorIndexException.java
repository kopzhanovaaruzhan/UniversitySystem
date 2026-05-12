package exceptions;

/**
 * Бросается когда h-index научного руководителя меньше 3.
 * Требование задания: "supervisor h-index < 3 → custom exception"
 */
public class SupervisorIndexException extends Exception {
	private static final long serialVersionUID = 1L;

	public SupervisorIndexException(String supervisorName, int hIndex) {
        super("Cannot assign '" + supervisorName + "' as supervisor: " +
              "h-index is " + hIndex + " (minimum required: 3)");
    }
}