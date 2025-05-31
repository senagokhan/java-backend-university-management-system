package course;

import config.DataBaseConnectorConfig;
import instructor.Instructor;
import student.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CourseRepository {
    public static void createCourseTable() {
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            String query = "CREATE SEQUENCE IF NOT EXISTS course_id_seq INCREMENT BY 1 MINVALUE 0 MAXVALUE 214748367 START 1; " +
                    "CREATE TABLE IF NOT EXISTS course (" +
                    "id INTEGER DEFAULT nextval('course_id_seq') PRIMARY KEY, " +
                    "course_name VARCHAR(255), " +
                    "course_number BIGINT, " +
                    "max_students INTEGER," +
                    "instructor_id INTEGER, " +
                    "credit_number INTEGER," +
                    "attendance_limit INTEGER, " +
                    "FOREIGN KEY (instructor_id) REFERENCES instructors(id) ON DELETE SET NULL ON UPDATE CASCADE);";

            statement.execute(query);
            System.out.println("Course table created successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Course saveCourse(Course course) {
        String query = "INSERT INTO course(course_name, course_number,max_students, instructor_id, credit_number,attendance_limit) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, course.getCourseName());
            preparedStatement.setLong(2, course.getCourseNumber());
            preparedStatement.setInt(3, course.getMaxStudents());
            preparedStatement.setInt(4, course.getInstructorId());
            preparedStatement.setInt(5, course.getCreditNumber());
            preparedStatement.setInt(6, course.getAttendanceLimit());
            preparedStatement.execute();
            System.out.println("Course saved successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return course;
    }


    public static void updateCourse(String courseName, Long courseNumber, int creditNumber, int instructorId, int maxStudents, int attendance_limit, int id) {
        String query = "UPDATE course SET course_name=?, course_number=?,credit_number=?, instructor_Id=? , max_students=? , attendance_limit=?  WHERE id=?";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.setLong(2, courseNumber);
            preparedStatement.setInt(3, creditNumber);
            preparedStatement.setInt(4, instructorId);
            preparedStatement.setInt(5, maxStudents);
            preparedStatement.setInt(6, attendance_limit);
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();
            System.out.println("Course updated successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteCourse(int id) {
        String query = "DELETE FROM course WHERE id=?";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            System.out.println("Course deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void getCourse() {
        String query = "SELECT * FROM course";
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            ResultSet data = statement.executeQuery(query);
            while (data.next()) {
                System.out.println("ID :" + data.getInt("id"));
                System.out.println("Course Name :" + data.getString("course_name"));
                System.out.println("Course Number :" + data.getString("course_number"));
                System.out.println("Credit Number :" + data.getInt("credit_number"));
                System.out.println("Instructor ID :" + data.getInt("instructor_id"));
                System.out.println("------------------");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Course getCourseByNumber(long courseNumber) {
        String query = "SELECT * FROM course WHERE course_number=" + courseNumber;
        Course course = new Course();
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {

            preparedStatement.setLong(1, courseNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                course = new Course();
                course.setCourseId(resultSet.getInt("id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setCreditNumber(resultSet.getInt("credit_number"));
                System.out.println("ID :" + course.getCourseId());
                System.out.println("Course Name :" + course.getCourseName());
                System.out.println("Course Number :" + course.getCourseNumber());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return course;
    }

       /* Keeps courses in a list called allListCourse.
        Creates a result set and adds all courses to the list of allListCourse and returns.*/

    public List<Course> getAllCourses() {
        ArrayList<Course> allListCourse = new ArrayList<>();
        String query = "SELECT * FROM course";
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setCourseId(resultSet.getInt("credit_number"));
                allListCourse.add(course);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return allListCourse;
    }

    public int calculateTotalCreditHours(int studentId) {
        int totalCreditHours = 0;
        String query = "SELECT SUM(course.credit_number) AS total_credits " +
                "FROM course_student_mapper " +
                "JOIN course ON course_student_mapper.course_id = course.id " +
                "WHERE course_student_mapper.student_id =" + studentId;

        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                totalCreditHours = resultSet.getInt("total_credits");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return totalCreditHours;
    }
      /*
    Keeps courses in a list called courses.
    Create a result set and return the courses that the student is enrolled in by adding them to the courses list.
     */

    public List<Course> getStudentEnrolledCourses(int studentId) {
        ArrayList<Course> courses = new ArrayList<>();
        String query = "SELECT course_id, course_student_mapper.student_id, course_name, course_number, instructor_id " +
                "FROM course_student_mapper " +
                "LEFT JOIN course ON course_student_mapper.course_id = course.id " +
                "WHERE course_student_mapper.student_id = " + studentId;

        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            Course course;
            while (resultSet.next()) {
                Instructor instructor = new Instructor();
                course = new Course();
                course.setCourseId(resultSet.getInt("course_id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setInstructor(instructor);
                courses.add(course);
                System.out.println(course.getCourseName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return courses;
    }

    public boolean isCourseFull(int courseId) {
        int studentCount = 0;
        int maxStudents = 0;
        String query = "SELECT course.id, course_name, max_students, COUNT(course_student_mapper.course_id) AS number_of_students " +
                "FROM course " +
                "LEFT JOIN course_student_mapper ON course.id = course_student_mapper.course_id " +
                "WHERE course.id = ? " +
                "GROUP BY course.id ;";

        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                studentCount = resultSet.getInt("number_of_students");
                maxStudents = resultSet.getInt("max_students");
                System.out.println("Student count " + studentCount + " --- Max Students " + maxStudents);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (studentCount == maxStudents) {
            return true;
        } else {
            return false;
        }
    }


    public List<Course> advancedSearchAndFilters(String searchCriteria, Map<Object, Object> filters) {
        List<Course> courses = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM course WHERE course_name ILIKE ?");
        if (filters != null) {
            for (Object key : filters.keySet()) {
                query.append(" AND ").append(key).append(" = ?");
            }
        }
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query.toString())) {
            preparedStatement.setString(1, "%" + searchCriteria + "%");
            int index = 2;
            if (filters != null) {
                for (Object key : filters.keySet()) {
                    preparedStatement.setObject(index, filters.get(key));
                    index++;
                }
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                courses.add(course);
            }
            for (Course course : courses) {
                System.out.println("Course ID: " + course.getCourseId());
                System.out.println("Course Name: " + course.getCourseName());
                System.out.println("Course Number: " + course.getCourseNumber());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return courses;
    }

    public CourseStatistics calculateCourseStatistics(int courseId) {
        CourseStatistics courseStatistics = new CourseStatistics();
        String query = "SELECT course.id,MIN(grade) AS lowest_grade ,MAX(grade) AS highest_grade,AVG(grade) AS average_grade FROM course " +
                "LEFT JOIN course_student_mapper ON course.id=course_student_mapper.course_id " +
                "WHERE course.id = ?  GROUP BY course.id ";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                courseStatistics.setCourseId(courseId);
                courseStatistics.setLowestGrade(resultSet.getDouble("lowest_grade"));
                courseStatistics.setHighestGrade(resultSet.getDouble("highest_grade"));
                courseStatistics.setAvarageGrade(resultSet.getDouble("average_grade"), 1);
                System.out.println(courseStatistics.getAvarageGrade());
                System.out.println(courseStatistics.getHighestGrade());
                System.out.println(courseStatistics.getLowestGrade());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return courseStatistics;
    }

    public List<Course> listPopularCourses(int topCount) {
        ArrayList<Course> popularCourses = new ArrayList<>();
        String query = "SELECT course_id,course_name ,course_number , max_students,COUNT(course_student_mapper.student_id) AS enrolled_students " +
                "FROM course " +
                "LEFT JOIN course_student_mapper ON course.id = course_student_mapper.course_id " +
                "GROUP BY course_id , course_name , course_number , max_students " +
                "ORDER BY enrolled_students DESC " +
                "LIMIT ? ";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, topCount);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("course_id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setMaxStudents(resultSet.getInt("max_students"));
                popularCourses.add(course);
                System.out.println(course.getCourseName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return popularCourses;
    }

        /*
        The total number of students was assigned to 0.
        The number of different students of the instructor was added to the total students and the value was returned.
        */

    public int calculateTotalStudentsForInstructor(int instructorId) {
        int totalStudents = 0;
        String query = "SELECT COUNT(DISTINCT course_student_mapper.student_id) AS total_students FROM course " +
                "JOIN course_student_mapper ON course.id=course_student_mapper.course_id " +
                "WHERE course.instructor_id = ? ";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalStudents = resultSet.getInt("total_students");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(totalStudents);
        return totalStudents;
    }

    public Course findCourseWithMostParticipants() {
        Course course = new Course();
        String query = "SELECT course_id , course_name , course_number , max_students , instructor_id , COUNT(course_student_mapper.student_id) AS enrolled_students FROM course " +
                "JOIN course_student_mapper ON course.id=course_student_mapper.course_id " +
                "GROUP BY course.id, course.course_name, course.course_number, course.max_students, course.instructor_id,course_student_mapper.course_id " +
                "ORDER BY enrolled_students DESC " +
                "LIMIT 1 ";
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Instructor instructor = new Instructor();
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setMaxStudents(resultSet.getInt("max_students"));
                instructor.setInstructorId(resultSet.getInt("instructor_id"));
                course.setInstructor(instructor);
                System.out.println(course.getCourseName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return course;
    }


    public List<Course> viewInstructorTaughtCourses(int instructorId) {
        ArrayList<Course> taughtCourses = new ArrayList<>();
        String query = "SELECT course.id, course_name,course_number , max_students FROM course " +
                "LEFT JOIN instructors ON instructors.id=course.instructor_id " +
                "WHERE instructor_id= " + instructorId;
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setMaxStudents(resultSet.getInt("max_students"));
                course.setCourseId(resultSet.getInt("id"));
                taughtCourses.add(course);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return taughtCourses;
    }

    public List<Course> reportStudentEnrolledCourses(int studentId) {
        ArrayList<Course> enrolledCourses = new ArrayList<>();
        String query = "SELECT DISTINCT( student_id) course_id, course_name, course_number,max_students, instructor_id FROM course " +
                "INNER JOIN course_student_mapper ON course.id=course_student_mapper.course_id " +
                "WHERE course_student_mapper.student_id=? ";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setMaxStudents(resultSet.getInt("max_students"));
                Instructor instructor = new Instructor();
                instructor.setInstructorId(resultSet.getInt("instructor_id"));
                course.setInstructor(instructor);
                enrolledCourses.add(course);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return enrolledCourses;
    }

    public Student findTopStudentInInstructorCourses(int instructorId) {
        Student student = new Student();
        String query = "SELECT instructors.id AS instructor_id, course_student_mapper.student_id, students.student_name, students.email, " +
                "MAX(course_student_mapper.grade) AS Max_grade " +
                "FROM course " +
                "JOIN instructors ON course.instructor_id = instructors.id " +
                "JOIN course_student_mapper ON course.id = course_student_mapper.course_id " +
                "JOIN students ON course_student_mapper.student_id = students.id " +
                "WHERE instructors.id = ? " +
                "GROUP BY instructors.id, course_student_mapper.student_id, students.student_name, students.email " +
                "ORDER BY Max_grade DESC " +
                "LIMIT 1";

        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, instructorId);  // instructorId'yi bağla
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                student.setStudentID(resultSet.getInt("student_id"));
                student.setStudentName(resultSet.getString("student_name"));
                student.setEmail(resultSet.getString("email"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return student;
    }

    public double calculateAverageGradeForCourse(int courseId) {

        String query = "SELECT SUM(grade) as total_grade, COUNT(grade) as number  FROM course_student_mapper " +
                "WHERE course_id = ? ";

        try (PreparedStatement statement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {

            statement.setInt(1, courseId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double totalGrade = resultSet.getDouble("total_grade");
                int numberOfStudents = resultSet.getInt("number");

                if (numberOfStudents > 0) {
                    return totalGrade / numberOfStudents;
                } else {
                    return 0;
                }
            } else {
                throw new SQLException("No results found.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }

    }

    public double calculateLetterGradeForStudent(int studentId, int courseId) {
        double grade = -1;

        String query = "SELECT grade FROM course_student_mapper WHERE course_id = ? AND student_id = ?";

        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    grade = resultSet.getDouble("grade");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return grade;
    }

    public double calculateAverageSuccessGradeForInstructorCourses(int instructorId) {

        String query = "SELECT SUM(grade) AS total_grade, COUNT(grade) AS number_grade " +
                "FROM course " +
                "LEFT JOIN course_student_mapper ON course_student_mapper.course_id = course.id " +
                "WHERE instructor_id = ? " +
                "GROUP BY instructor_id";

        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, instructorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double totalGrade = resultSet.getDouble("total_grade");
                int numberOfStudents = resultSet.getInt("number_grade");


                if (numberOfStudents > 0) {
                    return totalGrade / numberOfStudents;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }


    public List<Course> listCoursesOrderedByStudentAverageGrade() {
        ArrayList<Course> courseArrayList = new ArrayList<>();
        String query = "SELECT AVG(grade) AS average  , course_name , course_number , max_students ,instructor_id, credit_number ,attendance_limit FROM course " +
                "LEFT JOIN course_student_mapper ON course.id=course_student_mapper.course_id " +
                "GROUP BY course_name , course_number ,max_students , instructor_id , credit_number , attendance_limit " +
                "ORDER BY average DESC ; ";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                Instructor instructor = new Instructor();
                course.setInstructor(instructor);
                instructor.setInstructorId(resultSet.getInt("instructor_id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setMaxStudents(resultSet.getInt("max_students"));
                course.setCreditNumber(resultSet.getInt("credit_number"));
                course.setAttendanceLimit(resultSet.getInt("attendance_limit"));
                courseArrayList.add(course);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return courseArrayList;
    }

    public List<Course> createCourseSchedule(int studentId) {
        ArrayList<Course> courses = new ArrayList<>();

        String query = "SELECT student_id , course_id , course_name,course_number,max_students,credit_number ,attendance_limit,instructor_id " +
                "FROM course_student_mapper " +
                "LEFT JOIN course ON course_student_mapper.course_id=course.id " +
                "WHERE student_id= ? ";
        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                Instructor instructor = new Instructor();
                instructor.setInstructorId(resultSet.getInt("instructor_id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setCourseId(resultSet.getInt("course_id"));
                course.setCourseNumber(resultSet.getLong("course_number"));
                course.setInstructor(instructor);
                course.setCreditNumber(resultSet.getInt("credit_number"));
                course.setAttendanceLimit(resultSet.getInt("attendance_limit"));
                course.setMaxStudents(resultSet.getInt("max_students"));
                courses.add(course);
                System.out.println(course.getCourseName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courses;

    }

    public  Map<String,Object>  generateCourseReport(int courseId) {
        Map<String,Object> courseReport=new HashMap<>();
        String query = "SELECT course_name, course_number, max_students, credit_number , attendance_limit ,  SUM(grade) AS total_score, COUNT(course_id) AS student_count " +
                "FROM course LEFT JOIN course_student_mapper ON course.id = course_student_mapper.course_id " +
                "WHERE course.id = ? " +
                "GROUP BY course.id, course_name, course_number, max_students,credit_number , attendance_limit ";

        try (PreparedStatement preparedStatement = DataBaseConnectorConfig.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                courseReport.put("CourseName",resultSet.getString("course_name"));
                courseReport.put("CourseNumber",resultSet.getString("course_number"));
                courseReport.put("creditNumber",resultSet.getInt("credit_number"));
                courseReport.put("student_count",resultSet.getInt("student_count"));
                courseReport.put("max_students",resultSet.getInt("max_students"));
                courseReport.put("AttendanceLimit" , resultSet.getInt("attendance_limit"));
                courseReport.put("total_student_score",resultSet.getDouble("total_score"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseReport;
    }

}
