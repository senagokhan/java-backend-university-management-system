package student;

import config.DataBaseConnectorConfig;
import course.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    public static void createStudentTable() {
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            String query = "DROP SEQUENCE IF EXISTS student_id_seq ;" +
                    "CREATE SEQUENCE student_id_seq INCREMENT BY 1 MINVALUE 0 MAXVALUE 214748367 START 1;" +
                    "CREATE TABLE IF NOT EXISTS students ( " +
                    "id INTEGER DEFAULT nextval('student_id_seq') PRIMARY KEY ," +
                    "student_name VARCHAR(255) ," +
                    "email VARCHAR(255)," +
                    "password VARCHAR(255))";
            statement.execute(query);
            System.out.println("Student table created successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Student saveStudent(Student student) {
        String query = "INSERT INTO students(student_name,email,password)  VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, student.getStudentName());
            preparedStatement.setString(2, student.getEmail());
            preparedStatement.setString(3, student.getPassword());
            preparedStatement.execute();
            System.out.println("Student saved successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return student;
    }
    public static void updateStudent(String studentName, String email,String password, int id) {
        String query = "UPDATE students SET student_name=?, email=?, password=? WHERE id=?";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, studentName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id=? ";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            System.out.println("Student deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void getStudent() {
        String query = "SELECT * FROM students";
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            ResultSet data = statement.executeQuery(query);
            while (data.next()) {
                System.out.println("ID :" + data.getInt("id"));
                System.out.println("Student_full_name :" + data.getString("student_name"));
                System.out.println("Email :" + data.getString("email"));
                System.out.println("Password :" + data.getString("password"));
                System.out.println("------------------");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Student viewStudentDetails(int studentID) {
        String query = "SELECT id, student_name, email, password FROM students WHERE id=? " ;
        Student student = new Student();
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1,studentID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                student.setStudentID(resultSet.getInt("id"));
                student.setStudentName(resultSet.getString("student_name"));
                student.setEmail(resultSet.getString("email"));
                student.setPassword(resultSet.getString("password"));
                System.out.println("ID :" + student.getStudentID());
                System.out.println("Student Name :" + student.getStudentName());
                System.out.println("Student Email :" + student.getEmail());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return student;
    }

    public List<Student> listAllStudent(){
        ArrayList<Student> allListStudents =new ArrayList<>();
        String query="SELECT * FROM students";
        try(Statement statement=DataBaseConnectorConfig.getConnection().createStatement()){
            ResultSet resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                Student student =new Student();
                allListStudents.add(student);
            }
        }catch (Exception e ){
            throw new RuntimeException(e);
        }
        return allListStudents;
    }

    public List<Student> getCourseEnrolledStudents(int courseId) {
        ArrayList<Student> studentArrayList = new ArrayList<>();
        String query = "SELECT students.id, students.student_name, students.email " +
                "FROM course_student_mapper " +
                "LEFT JOIN students ON course_student_mapper.student_id = students.id " +
                "WHERE course_student_mapper.course_id = " + courseId;

        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setStudentID(resultSet.getInt("id"));
                student.setStudentName(resultSet.getString("student_name"));
                student.setEmail(resultSet.getString("email"));
                studentArrayList.add(student);
                System.out.println(student.getStudentName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return studentArrayList;
    }

    public static Student getStudentById(int studentId){
        String query="SELECT * FROM students WHERE id =?";
        Student student=new Student();
        try(PreparedStatement preparedStatement=DataBaseConnectorConfig.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1,studentId);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                student=new Student();
                student.setStudentID(resultSet.getInt("id"));
                student.setStudentName(resultSet.getString("student_name"));
                student.setEmail(resultSet.getString("email"));
                student.setPassword(resultSet.getString("password"));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return student;

    }
    public List<String> getCourseNameById(long studentId) {
        List<String> courseNames = new ArrayList<>();
        String query = "SELECT c.course_name " +
                "FROM course_student_mapper m " +
                "JOIN course c ON m.course_id = c.id " +
                "WHERE m.student_id = ?";

        try (PreparedStatement statement = DataBaseConnectorConfig.getConnection().prepareStatement(query))
        {

            statement.setLong(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    courseNames.add(resultSet.getString("course_name"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving recommended courses for student", e);
        }

        return courseNames;
    }
    public List<Course> listRecommendedCoursesForStudent(long studentId) {
        List<Course> recommendedCourses = new ArrayList<>();

        // Query to select all recommended courses for a given student
        String query = "SELECT c.* " +
                "FROM course_student_mapper m " +
                "JOIN course c ON m.course_id = c.id " +
                "WHERE m.student_id = ?";

        try (Connection connection = DataBaseConnectorConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the studentId in the query
            statement.setLong(1, studentId);

            // Execute the query and process the result set
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setCourseId(resultSet.getInt("id")); // Assuming "id" is the primary key of the course
                    course.setCourseName(resultSet.getString("course_name")); // Adjust column name as needed
                    recommendedCourses.add(course);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving recommended courses", e);
        }

        return recommendedCourses;
    }
    public boolean validateEmailDuringStudentRegistration(String email) {

        String query = "SELECT * FROM students WHERE email = ?";

        try (PreparedStatement statement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("A record was found in the database with this email: " + email);
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isStudentEnrolledInCourse(int studentID, int courseID) {
        String query = "SELECT COUNT(*) FROM course_student_mapper WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, studentID);
            preparedStatement.setInt(2, courseID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
