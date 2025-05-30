package instructor;

import course.Course;
import java.util.List;

public class Instructor {

    private int instructorId;
    private String instructorFullName;
    private String email;
    private String password;
    private List<Course> enrolledLessons;

    public Instructor(){
    }

    public Instructor(int instructorId, String instructorFullName, String email, String password, List<Course> enrolledLessons) {
        this.instructorId = instructorId;
        this.instructorFullName = instructorFullName;
        this.email = email;
        this.password = password;
        this.enrolledLessons = enrolledLessons;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorFullName() {
        return instructorFullName;
    }

    public void setInstructorFullName(String instructorFullName) {
        this.instructorFullName = instructorFullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getEnrolledLessons() {
        return enrolledLessons;
    }

    public void setEnrolledLessons(List<Course> enrolledLessons) {
        this.enrolledLessons = enrolledLessons;
    }
}
