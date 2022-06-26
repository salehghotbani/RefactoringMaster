package domain;

import java.sql.Date;

public class Term {
    private final String name;
    private final Date startDate;

    public Term(String name) {
        this.name = name;
        this.startDate = null;
    }

    public Term(String name, Date startDate) {
        this.name = name;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }


}
