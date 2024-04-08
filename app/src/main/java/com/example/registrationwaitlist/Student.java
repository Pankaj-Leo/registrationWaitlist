package com.example.registrationwaitlist;

public class Student {
    private long id;
    private String name;
    private String courseId;
    private int yearOfStudy; // Added field for year of study

    public Student(long id, String name, String courseId, int yearOfStudy) {
        this.id = id;
        this.name = name;
        this.courseId = courseId;
        this.yearOfStudy = yearOfStudy;
    }

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    // Priority is determined by year of study
    public int getPriority() {
        switch (yearOfStudy) {
            case 1: return 5; // 1st Year
            case 2: return 4; // 2nd Year
            case 3: return 3; // 3rd Year
            case 4: return 2; // 4th Year
            default: return 1; // Graduate
        }
    }
}
