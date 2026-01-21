package hu.zmesko.Appointment.exception;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException() {
        super("Id not found");
    }
}
