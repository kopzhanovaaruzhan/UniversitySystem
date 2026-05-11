package exceptions;

/**
 * Бросается когда h-index научного руководителя меньше 3.
 * Требование задания: "supervisor h-index < 3 → custom exception"
 */
public class SupervisorIndexException extends Exception {
    public SupervisorIndexException(String supervisorName, int hIndex) {
        super("Cannot assign '" + supervisorName + "' as supervisor: " +
              "h-index is " + hIndex + " (minimum required: 3)");
    }
}
