package course;

import instructor.Instructor;
import student.Student;
import java.util.List;

public class Course {
    private int courseId;
    private String courseName;
    private long courseNumber;
    private Instructor instructor;
    private int creditNumber;
    private int maxStudents;
    private int attendanceLimit;
    private List<Student> enrolledStudents;

    public Course(int courseId,String courseName, long courseNumber,List<Student> enrolledStudents,Instructor instructor, int creditNumber) {
        this.courseId = courseId;

    }

    public int getAttendanceLimit() {
        return attendanceLimit;
    }

    public void setAttendanceLimit(int attendanceLimit) {
        this.attendanceLimit = attendanceLimit;
    }

    public Course(int courseId, String courseName, long courseNumber, int maxStudents, List<Student> enrolledStudents, Instructor instructor, int creditNumber, int attendanceLimit) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseNumber = courseNumber;
        this.maxStudents = maxStudents;
        this.enrolledStudents = enrolledStudents;
        this.instructor = instructor;
        this.attendanceLimit=attendanceLimit;
        this.creditNumber = creditNumber;
    }

    public Course() {

    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public long getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(long courseNumber) {
        this.courseNumber = courseNumber;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public int getInstructorId() {
        return instructor.getInstructorId();
    }

    public int getCreditNumber() {
        return creditNumber;
    }
    public void setCreditNumber(int creditNumber){
        this.creditNumber = creditNumber;
    }
}
