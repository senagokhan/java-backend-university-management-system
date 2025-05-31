package student;

import course.Course;
import java.util.List;

public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        System.out.println("Student" + student.getStudentName() + "has been created. ");
        return studentRepository.saveStudent(student);
    }

    public void deleteStudent(int studentId) {
        Student student = StudentRepository.getStudentById(studentId);
        if (student != null) {
            System.out.println("Student has been deleted");
            studentRepository.deleteStudent(studentId);
        } else {
            System.out.println("Student not found with student id " + studentId);
        }
    }

    public void updateStudent(String studentName, String email, String password, int id) {
        Student student = StudentRepository.getStudentById(id);
        if(student!=null){
            if(student.getStudentID()==id){
                System.out.println("Student has been updated");
                StudentRepository.updateStudent(studentName,email,password,id);
            }else{
                System.out.println("Student ID  does not match for given student id");
            }
        }else{
            System.out.println("Student not found with student id " + id);
        }
    }

    public void listRecommendedCourseForStudent(long studentId){
        List<Course> courses = studentRepository.listRecommendedCoursesForStudent(studentId);
        if (courses.isEmpty()){
            System.out.println("No recommended course for student:" + studentId);
        }else {
            System.out.println("Recommended course for student:" + studentId);
            for (Course course : courses){
                System.out.println("Recommended course is:" + course.getCourseName());
            }
        }
    }
    public boolean validateEmailDuringStudentRegistration(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        if (!email.matches(emailRegex)) {
            System.out.println("Invalid email format: " + email);
            return false;
        }

        return studentRepository.validateEmailDuringStudentRegistration(email);
    }
}
