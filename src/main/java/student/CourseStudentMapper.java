package student;

import config.DataBaseConnectorConfig;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

public class CourseStudentMapper {
    public static void createCourseStudentMapper() {
        try (Statement statement = DataBaseConnectorConfig.getConnection().createStatement()) {
            String query = "DROP SEQUENCE IF EXISTS c_s_seq CASCADE;" +
                    "CREATE SEQUENCE c_s_seq INCREMENT BY 1 MINVALUE 0 MAXVALUE 214748367 START 1;" +
                    "CREATE TABLE IF NOT EXISTS course_student_mapper (" +
                    "id INTEGER DEFAULT nextval('c_s_seq') PRIMARY KEY ," +
                    "student_id INTEGER ," +
                    "course_id INTEGER ," +
                    "grade INTEGER," +
                    "course_start_date DATE, "+
                    "course_end_date DATE, "+
                    "CONSTRAINT fk_course_id FOREIGN KEY (course_id) REFERENCES course(id)," +
                    "CONSTRAINT fk_student_id FOREIGN KEY (student_id) REFERENCES students(id));";

            statement.execute(query);
            System.out.println("CourseStudent mapper created successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCourseStudentMapper (int studentID, int courseID, int grade, Timestamp start, Timestamp end){
        String query="INSERT INTO course_student_mapper(student_id , course_id,grade,course_start_date,course_end_date) VALUES(?,?,?,?,?)";
        try(PreparedStatement preparedStatement=DataBaseConnectorConfig.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1,studentID);
            preparedStatement.setInt(2,courseID);
            preparedStatement.setInt(3,grade);
            preparedStatement.setTimestamp(4,start);
            preparedStatement.setTimestamp(5,end);
            preparedStatement.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void deleteCourseStudentMapper(int studentID,int courseID){
        String query="DELETE FROM course_student_mapper  WHERE student_id=? AND course_id =? ";
        try(PreparedStatement preparedStatement=DataBaseConnectorConfig.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1,studentID);
            preparedStatement.setInt(2,courseID);
            preparedStatement.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
