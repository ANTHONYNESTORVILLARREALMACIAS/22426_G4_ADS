gpackage org.example;

import java.util.List;

public class StudentController {
    private StudentDatabase database;
    private StudentView view;

    public StudentController(StudentView view) {
        this.view = view;
        this.database = StudentDatabase.getInstance();
    }

    public void start() {
        System.out.println("******************Fetching Data******************");
        this.fetchStudents();
        System.out.println("******************Creating Data******************");
        this.createStudent();
        System.out.println("******************Updating Data******************");
        this.updateStudent();
    }

    public void fetchStudents() {
        this.updateView();
    }

    public void createStudent() {
        Student student = this.view.inputStudent();
        this.database.postStudent(student);
        this.updateView();
    }

    public void updateStudent() {
        List<Student> students = this.database.getStudents();
        if (!students.isEmpty()) {
            Student oldStudent = students.get(0);
            System.out.println("Updating student: " + oldStudent.getName());
            Student updatedStudent = this.view.inputStudent();
            this.database.putStudent(oldStudent, updatedStudent);
            this.updateView();
        }
    }

    private void updateView() {
        List<Student> data = this.database.getStudents();
        for (Student student : data) {
            this.view.printStudentDetails(student);
        }
    }
}