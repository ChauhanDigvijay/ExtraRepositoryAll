package com.identity.arx.objectclass;

import java.util.List;

public class StudentMonthlyReportResponseObject {
    private List<String> Absent;
    private List<String> NoClass;
    private List<String> Present;

    public List<String> getPresent() {
        return this.Present;
    }

    public void setPresent(List<String> Present) {
        this.Present = Present;
    }

    public List<String> getAbsent() {
        return this.Absent;
    }

    public void setAbsent(List<String> Absent) {
        this.Absent = Absent;
    }

    public List<String> getNoClass() {
        return this.NoClass;
    }

    public void setNoClass(List<String> NoClass) {
        this.NoClass = NoClass;
    }
}
