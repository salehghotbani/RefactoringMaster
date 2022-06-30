package domain.exceptions;

public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        //show visual messagebox of error
        super(msg);
    }
}
