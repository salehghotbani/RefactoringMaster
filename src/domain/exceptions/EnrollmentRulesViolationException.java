package domain.exceptions;

import javax.swing.*;

public class EnrollmentRulesViolationException {

    public EnrollmentRulesViolationException(String msg) {
        //show visual messagebox of error
        JOptionPane.showMessageDialog(null, "Enrollment Rules Violation Exception occurred!\nMessage: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
