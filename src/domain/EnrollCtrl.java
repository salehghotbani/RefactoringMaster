package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;
import domain.exceptions.StudentNotFoundException;

import javax.swing.*;

public class EnrollCtrl {
    public void enroll(Student s, List<CSE> courses) {
        try {
            if (s == null)
                throw new StudentNotFoundException("Student not Found!");

            Map<Term, Map<Course, Double>> transcript = s.getTranscript(); //lists of courses that student passed
            for (CSE o : courses) {
                //check for student passed a lesson or not
                for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
                    for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                        if (r.getKey().equals(o.getCourse()) && r.getValue() >= 10)
                            new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));
                    }
                }
                //check prerequisite of courses that student requested
                List<Course> prereqs = o.getCourse().getPrerequisites();
                nextPre:
                for (Course pre : prereqs) {
                    for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
                        for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                            if (r.getKey().equals(pre) && r.getValue() >= 10)
                                continue nextPre;
                        }
                    }
                    new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), o.getCourse().getName()));
                }

                for (CSE o2 : courses) {
                    if (o == o2)
                        continue;
                    //check time of exam, to not same as another lessons exam time
                    if (o.getExamTime().equals(o2.getExamTime()))
                        new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
                    //check if request lesson was taken before
                    if (o.getCourse().equals(o2.getCourse()))
                        new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
                }
            }

            // numbers of units that requested
            int unitsRequested = 0;
            for (CSE o : courses)
                unitsRequested += o.getCourse().getUnits();

            // Grade point average
            double points = 0;
            int totalUnits = 0;
            for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
                for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                    points += r.getValue() * r.getKey().getUnits();
                    totalUnits += r.getKey().getUnits();
                }
            }
            double gpa = points / totalUnits;

            /*check that when student's gpa is less than 12 he(\she) cannot request unit greater than 14,
             * and when gpa is less than 16 he(\she) cannot request unit greater than 16,
             * and anyway it cannot greater than 20!*/
            if ((gpa < 12 && unitsRequested > 14) || (gpa < 16 && unitsRequested > 16) || (unitsRequested > 20))
                new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, gpa));
            for (CSE o : courses)
                s.takeCourse(o.getCourse(), o.getSection());

        } catch (StudentNotFoundException msg) {
            JOptionPane.showMessageDialog(null,"Student Not Found Exception occurred!\nMessage: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
