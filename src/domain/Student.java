package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
    private final String id;
    private final String name;
    private final Map<Term, Map<Course, Double>> transcript; // courses that passed with this student
    private final List<CourseSection> currentTerm; // lists of courses in current term

    double getGPA() {
        Map<Term, Map<Course, Double>> transcript = getTranscript();
        double points = 0;
        int totalUnits = 0;
        for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
            for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                points += r.getValue() * r.getKey().getUnits();
                totalUnits += r.getKey().getUnits();
            }
        }
        return points / totalUnits;
    }

    static class CourseSection {
        Course course;
        int section;

        CourseSection(Course course, int section) {
            this.course = course;
            this.section = section;
        }
    }

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.transcript = new HashMap<>();
        this.currentTerm = new ArrayList<>();
    }

    public void takeCourse(Course c, int section) {
        currentTerm.add(new CourseSection(c, section));
    }

    public Map<Term, Map<Course, Double>> getTranscript() {
        return transcript;
    }

    public void addTranscriptRecord(Course course, Term term, double grade) {
        if (!transcript.containsKey(term))
            transcript.put(term, new HashMap<>());
        transcript.get(term).put(course, grade);
    }

    public List<CourseSection> getCurrentTerm() {
        return currentTerm;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
