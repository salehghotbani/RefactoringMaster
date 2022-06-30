package domain;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class EnrollCtrlTest {
    // Students name
    private Student bebe;
    private Student bebe2;
    // Courses name
    private Course prog, ap, dm, math1, math2, phys1, phys2, maaref, farsi, english, akhlagh, economy, karafarini;

    @Before
    public void setup() { // define courses and students
        math1 = new Course("4", "MATH1", 3);
        phys1 = new Course("8", "PHYS1", 3);
        prog = new Course("7", "PROG", 4);
        math2 = new Course("6", "MATH2", 3).withPre(math1);
        phys2 = new Course("9", "PHYS2", 3).withPre(math1, phys1);
        ap = new Course("2", "AP", 3).withPre(prog);
        dm = new Course("3", "DM", 3).withPre(prog);
        economy = new Course("1", "ECO", 3);
        maaref = new Course("5", "MAAREF", 2);
        farsi = new Course("12", "FA", 2);
        english = new Course("10", "EN", 2);
        akhlagh = new Course("11", "AKHLAGH", 2);
        karafarini = new Course("13", "KAR", 3);

        bebe = new Student("1", "Bebe");
    }

    private ArrayList<CSE> requestedOfferings(Course... courses) { // create CSE class, set exam time to lesson
        Calendar cal = Calendar.getInstance();
        ArrayList<CSE> result = new ArrayList<>();
        for (Course course : courses) {
            cal.add(Calendar.DATE, 1);
            result.add(new CSE(course, cal.getTime()));
        }
        return result;
    }

    private boolean hasTaken(Student s, Course... courses) { // check to see if lessons has taken correctly?!
        try {
            if (s == null) //If user not found show message
                throw new NullPointerException();

            Set<Course> coursesTaken = new HashSet<>();
            for (Student.CourseSection cs : s.getCurrentTerm())
                coursesTaken.add(cs.course);
            for (Course course : courses) {
                if (!coursesTaken.contains(course))
                    return false;
            }

        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Student Not Found Exception occurred!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // these @test classes names has complete information about their own class
    @Test
    public void canTakeBasicCoursesInFirstTerm(){
        new EnrollCtrl().enroll(bebe, requestedOfferings(math1, phys1, prog));
        assertTrue(hasTaken(bebe, math1, phys1, prog));
    }

    @Test
    public void canTakeNoOfferings() {
        new EnrollCtrl().enroll(bebe, new ArrayList<>());
        assertTrue(hasTaken(bebe));
    }

    @Test
    public void cannotTakeWithoutPreTaken() {
        new EnrollCtrl().enroll(bebe, requestedOfferings(math2, phys1, prog));
    }

    @Test
    public void cannotTakeWithoutPrePassed() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 18);
        bebe.addTranscriptRecord(prog, new Term("t1"), 12);
        bebe.addTranscriptRecord(math1, new Term("t1"), 8.4);
        new EnrollCtrl().enroll(bebe, requestedOfferings(math2, ap));
    }

    @Test
    public void canTakeWithPreFinallyPassed() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 18);
        bebe.addTranscriptRecord(prog, new Term("t1"), 12);
        bebe.addTranscriptRecord(math1, new Term("t1"), 8.4);

        bebe.addTranscriptRecord(phys2, new Term("t2"), 10);
        bebe.addTranscriptRecord(ap, new Term("t2"), 16);
        bebe.addTranscriptRecord(math1, new Term("t2"), 10.5);

        new EnrollCtrl().enroll(bebe, requestedOfferings(math2, dm));
        assertTrue(hasTaken(bebe, math2, dm));
    }

    @Test
    public void cannotTakeAlreadyPassed1() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 18);
        bebe.addTranscriptRecord(prog, new Term("t1"), 12);
        bebe.addTranscriptRecord(math1, new Term("t1"), 8.4);

        bebe.addTranscriptRecord(phys2, new Term("t2"), 10);
        bebe.addTranscriptRecord(ap, new Term("t2"), 16);
        bebe.addTranscriptRecord(math1, new Term("t2"), 10.5);

        new EnrollCtrl().enroll(bebe, requestedOfferings(math1, dm));
    }

    @Test
    public void cannotTakeAlreadyPassed2() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 18);
        bebe.addTranscriptRecord(prog, new Term("t1"), 12);
        bebe.addTranscriptRecord(math1, new Term("t1"), 8.4);

        bebe.addTranscriptRecord(phys2, new Term("t2"), 10);
        bebe.addTranscriptRecord(ap, new Term("t2"), 16);
        bebe.addTranscriptRecord(math1, new Term("t2"), 10.5);

        new EnrollCtrl().enroll(bebe, requestedOfferings(phys1, dm));
    }

    @Test
    public void cannotTakeOfferingsWithSameExamTime() {
        Calendar cal = Calendar.getInstance();
        new EnrollCtrl().enroll(bebe, List.of(
                new CSE(phys1, cal.getTime()),
                new CSE(math1, cal.getTime()),
                new CSE(phys1, cal.getTime())
        ));
    }

    @Test
    public void cannotTakeACourseTwice() {
        new EnrollCtrl().enroll(bebe, requestedOfferings(phys1, dm, phys1));
    }

    @Test
    public void canTake14WithGPA11() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 13);
        bebe.addTranscriptRecord(prog, new Term("t1"), 11);
        bebe.addTranscriptRecord(math1, new Term("t1"), 9);

        new EnrollCtrl().enroll(bebe, requestedOfferings(dm, math1, farsi, akhlagh, english, maaref));
        assertTrue(hasTaken(bebe, dm, math1, farsi, akhlagh, english, maaref));
    }

    @Test
    public void cannotTake15WithGPA11() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 13);
        bebe.addTranscriptRecord(prog, new Term("t1"), 11);
        bebe.addTranscriptRecord(math1, new Term("t1"), 9);

        new EnrollCtrl().enroll(bebe, requestedOfferings(dm, math1, farsi, akhlagh, english, ap));
        assertTrue(hasTaken(bebe, dm, math1, farsi, akhlagh, english, ap));
    }

    @Test
    public void canTake15WithGPA12() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 15);
        bebe.addTranscriptRecord(prog, new Term("t1"), 12);
        bebe.addTranscriptRecord(math1, new Term("t1"), 9);

        new EnrollCtrl().enroll(bebe, requestedOfferings(dm, math1, farsi, akhlagh, english, maaref));
        assertTrue(hasTaken(bebe, dm, math1, farsi, akhlagh, english, maaref));
    }

    @Test
    public void canTake15WithGPA15() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 15);
        bebe.addTranscriptRecord(prog, new Term("t1"), 15);
        bebe.addTranscriptRecord(math1, new Term("t1"), 15);

        new EnrollCtrl().enroll(bebe, requestedOfferings(dm, math2, farsi, akhlagh, english, maaref));
        assertTrue(hasTaken(bebe, dm, math2, farsi, akhlagh, english, maaref));
    }

    @Test
    public void cannotTake18WithGPA15() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 15);
        bebe.addTranscriptRecord(prog, new Term("t1"), 15);
        bebe.addTranscriptRecord(math1, new Term("t1"), 15);

        new EnrollCtrl().enroll(bebe, requestedOfferings(ap, dm, math2, farsi, akhlagh, english, ap));
        assertTrue(hasTaken(bebe, ap, dm, math2, farsi, akhlagh, english, ap));
    }

    @Test
    public void canTake20WithGPA16() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 16);
        bebe.addTranscriptRecord(prog, new Term("t1"), 16);
        bebe.addTranscriptRecord(math1, new Term("t1"), 16);

        new EnrollCtrl().enroll(bebe, requestedOfferings(ap, dm, math2, phys2, economy, karafarini, farsi));
        assertTrue(hasTaken(bebe, ap, dm, math2, phys2, economy, karafarini, farsi));
    }

    @Test
    public void cannotTake24() {
        bebe.addTranscriptRecord(phys1, new Term("t1"), 16);
        bebe.addTranscriptRecord(prog, new Term("t1"), 16);
        bebe.addTranscriptRecord(math1, new Term("t1"), 16);

        new EnrollCtrl().enroll(bebe, requestedOfferings(ap, dm, math2, phys2, economy, karafarini, farsi, akhlagh, english));
        assertTrue(hasTaken(bebe, ap, dm, math2, phys2, economy, karafarini, farsi, akhlagh, english));
    }
}