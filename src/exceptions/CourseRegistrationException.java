package exceptions;

/**
 * Бросается при нарушении правил регистрации на курс:
 * - превышение 21 кредита
 * - 3+ провалов по курсу
 * - курс переполнен
 * - уже записан
 */
public class CourseRegistrationException extends Exception {
    public CourseRegistrationException(String message) {
        super(message);
    }
}
