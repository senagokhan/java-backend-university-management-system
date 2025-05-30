package student;

import course.Course;

import java.util.List;

public class Student {
    private String studentName;
    private int studentID;
    private String email;
    private List<Course> enrolledCourses;
    private String password;

    public Student(){
    }

    public Student(String studentName, int studentID, String email, List<Course> enrolledCourses, String password) {
        this.studentName = studentName;
        this.studentID = studentID;
        this.email = email;
        this.enrolledCourses = enrolledCourses;
        this.password = password;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public List<Course> getEnrolledCourses(){
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
