package org.example;

public class Student {
    private String rollNo;
    private String name;

    public Student() {
        this("", "");
    }

    public Student(String name, String rollNo) {
        this.rollNo = rollNo;
        this.name = name;
    }

    public Student(Student student) {
        this.rollNo = student.getRollNo();
        this.name = student.getName();
    }

    public Student(String name) {
        this.name = name;
        this.rollNo = "0";
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}