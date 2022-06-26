package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Course {
    private final String id;
    private final String name;
    private final int units;

    List<Course> prerequisites;

    public Course(String id, String name, int units) {
        this.id = id;
        this.name = name;
        this.units = units;
        prerequisites = new ArrayList<>();
    }

    public void addPre(Course c) {
        getPrerequisites().add(c);
    }

    public Course withPre(Course... pres) {
        prerequisites.addAll(Arrays.asList(pres));
        return this;
    }

    public List<Course> getPrerequisites() {
        return prerequisites;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" {");
        for (Course pre : getPrerequisites()) {
            sb.append(pre.getName());
            sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public int getUnits() {
        return units;
    }

    public String getId() {
        return id;
    }


    public boolean equals(Object obj) {
        Course other = (Course) obj;
        return id.equals(other.id);
    }
}
