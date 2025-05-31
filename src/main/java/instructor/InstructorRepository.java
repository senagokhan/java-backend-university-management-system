package instructor;

import config.DataBaseConnectorConfig;
import course.Course;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InstructorRepository {
    public static void createInstructorTable() {
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            String query = "DROP SEQUENCE IF EXISTS instructor_id_seq; " +
                    "CREATE SEQUENCE instructor_id_seq INCREMENT BY 1 MINVALUE 0 MAXVALUE 214748367  START 1;" +
                    "CREATE TABLE IF NOT EXISTS instructors (" +
                    "id INTEGER DEFAULT nextval('instructor_id_seq') PRIMARY KEY," +
                    "instructor_full_name VARCHAR(255)," +
                    "email VARCHAR(255)," +
                    "password VARCHAR(255))";
            statement.execute(query);
            System.out.println("Instructor table created successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Instructor saveInstructor(Instructor instructor) {
        String query = "INSERT INTO instructors(instructor_full_name,email,password) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, instructor.getInstructorFullName());
            preparedStatement.setString(2, instructor.getEmail());
            preparedStatement.setString(3, instructor.getPassword());
            preparedStatement.execute();
            System.out.println("Instructor saved successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instructor;
    }

    public static void updateInstructor(String instructor_full_name, String email, String password, int id) {
        String query = "UPDATE instructors SET instructor_full_name=?,email=?,password=? WHERE id=?";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, instructor_full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteInstructor(int id) {
        String query = "DELETE FROM instructors WHERE id=?";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void getInstructor() {
        String query = "SELECT * FROM instructors";
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            ResultSet data = statement.executeQuery(query);
            while (data.next()) {
                System.out.println("ID :" + data.getInt("id"));
                System.out.println("Instructor_full_name :" + data.getString("instructor_full_name"));
                System.out.println("Email :" + data.getString("email"));
                System.out.println("Password :" + data.getString("password"));
                System.out.println("------------------");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Instructor viewInstructorDetails(int instructorId) {
        String query = "SELECT * FROM instructors WHERE id = " + instructorId;
        Instructor instructor = new Instructor();
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                instructor.setInstructorId(resultSet.getInt("id"));
                instructor.setInstructorFullName(resultSet.getString("instructor_full_name"));
                instructor.setEmail(resultSet.getString("email"));
                instructor.setPassword(resultSet.getString("password"));
            }
            System.out.println(instructor.getInstructorFullName());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instructor;
    }

    public List<Instructor> getAllInstructors() {
        ArrayList<Instructor> allListInstructor = new ArrayList<>();
        String query = "SELECT * FROM instructors";
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Instructor instructor = new Instructor();
                instructor.setInstructorId(resultSet.getInt("id"));
                instructor.setInstructorFullName(resultSet.getString("instructor_full_name"));
                instructor.setEmail(resultSet.getString("email"));
                instructor.setPassword(resultSet.getString("password"));
                allListInstructor.add(instructor);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return allListInstructor;
    }

    public static Instructor getInstructorById(int instructorId) {
        String query = "SELECT * FROM instructors WHERE id=?";
        Instructor instructor = new Instructor();
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                instructor = new Instructor();
                instructor.setInstructorId(resultSet.getInt("id"));
                instructor.setInstructorFullName(resultSet.getString("instructor_full_name"));
                instructor.setEmail(resultSet.getString("email"));
                instructor.setPassword(resultSet.getString("password"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instructor;
    }

    public List<Course> getMostRecommendedCoursesForInstructor(int instructorId, int topCount) {
        List<Course> recommendedCourses = new ArrayList<>();
        String query = "SELECT course.id AS course_id, course.course_name, course.course_number, " +
                "course.max_students, course.credit_number " +
                "FROM course " +
                "JOIN course_student_mapper ON course.id = course_student_mapper.course_id " +
                "WHERE course.instructor_id = ? AND course_student_mapper.grade > 50 " +
                "GROUP BY course.id, course.course_name, course.course_number, course.max_students, course.credit_number " +
                "ORDER BY COUNT(course_student_mapper.grade) DESC " +
                "LIMIT ?;";

        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, instructorId);
            preparedStatement.setInt(2, topCount);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("course_id")); // Use the alias defined in the query
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setCreditNumber(resultSet.getInt("credit_number"));
                course.setMaxStudents(resultSet.getInt("max_students"));
                recommendedCourses.add(course);
            }
        } catch (Exception e) {
            throw new RuntimeException( e.getMessage());
        }
        return recommendedCourses;
    }
}
