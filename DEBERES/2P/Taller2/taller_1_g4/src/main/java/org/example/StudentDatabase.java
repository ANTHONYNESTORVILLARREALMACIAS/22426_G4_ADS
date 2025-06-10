package org.example;

import java.util.List;
import java.util.ArrayList;

public class StudentDatabase {
    private static StudentDatabase instance;
    private List<Student> students;

    private StudentDatabase() {
        this.students = new ArrayList<>();
        this.students.add(new Student("Robert", "10"));
        this.students.add(new Student("Miguel", "11"));
        this.students.add(new Student("Ana", "12"));
        this.students.add(new Student("Jonh", "10"));
        this.students.add(new Student("Juan", "11"));
    }

    public static StudentDatabase getInstance() {
        if (instance == null) {
            instance = new StudentDatabase();
        }
        return instance;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public void postStudent(Student student) {
        this.students.add(student);
    }

    public void putStudent(Student oldStudent, Student updatedStudent) {
        int index = this.students.indexOf(oldStudent);
        this.students.remove(index);
        this.students.add(0, updatedStudent);
    }

    public void deleteStudent(Student student) {
        this.students.remove(student);
    }
}