package exceptions;

/**
 * Бросается при нарушении правил регистрации на курс:
 * - превышение 21 кредита
 * - 3+ провалов по курсу
 * - курс переполнен
 * - уже записан
 */
public class CourseRegistrationException extends Exception {
	private static final long serialVersionUID = 1L;

	public CourseRegistrationException(String message) {
        super(message);
    }
}