package org.example;

import java.util.Scanner;

public class StudentView {
    private Scanner scanner = new Scanner(System.in);

    public void printStudentDetails(Student student) {
        System.out.println("Student: ");
        System.out.println("Name: " + student.getName());
        System.out.println("Roll No: " + student.getRollNo());
        System.out.println("");
    }

    public Student inputStudent() {
        System.out.println("***Create: ");
        System.out.println("Student: ");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Roll No: ");
        String rollNo = scanner.nextLine();
        System.out.println("***End: ");
        System.out.println("");
        return new Student(name, rollNo);
    }
}