package course;

import student.Student;
import instructor.Instructor;
import instructor.InstructorRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.*;

public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(Course course) {
        System.out.println("Course" + course.getCourseName() + "has been created");
        return courseRepository.saveCourse(course);

    }

    public void deleteCourse(long courseNumber) {
        Course course = CourseRepository.getCourseByNumber(courseNumber);
        int courseId = course.getCourseId();
        if (course != null) {
            System.out.println("Course has been deleted.");
            CourseRepository.deleteCourse(courseId);
        } else {
            System.out.println("Course not found with course number: " + courseNumber);
        }
    }

    public void updateCourse(String courseName, long courseNumber,int credit, int instructorId,int maxStudents ,int attendanceLimit) {
        Course course = CourseRepository.getCourseByNumber(courseNumber);
        int courseId = course.getCourseId();
        if (course != null) {
            if (course.getCourseId() == courseId) {
                System.out.println("Course has been updated.");
                CourseRepository.updateCourse(courseName, courseNumber,credit, instructorId,courseId,maxStudents,attendanceLimit);
            } else {
                System.out.println("Course ID does not match for the given course number.");
            }
        } else {
            System.out.println("Course not found with course number: " + courseNumber);
        }
    }

    public void findCourseWithMostParticipants() {
        Course result = courseRepository.findCourseWithMostParticipants();
        System.out.printf("%-20s %-20s %-20s %-20s%n", "Course Name", "Course Number", "Max Students", "Instructor ID");
        System.out.printf("%-20s %-20s %-20s %-20s%n", "------------", "------------", "------------", "------------");
        System.out.printf("%-20s %-20d %-20d %-20d%n",
                result.getCourseName(),
                result.getCourseNumber(),
                result.getMaxStudents(),
                result.getInstructorId());
    }

    public List<Course> viewInstructorTaughtCourses(int instructorId) {
        Instructor instructor = InstructorRepository.getInstructorById(instructorId);
        if (instructor != null) {
            List<Course> courses = courseRepository.viewInstructorTaughtCourses(instructorId);
            if (!courses.isEmpty()) {
                System.out.printf("%-30s %-30s %-15s%n", "Course Name", "Course Number", "Max Students");
                System.out.printf("%-30s %-30s %-15s%n", "------------", "------------", "------------");
                for (Course course : courses) {
                    System.out.printf("%-30s %-30d %-15d%n",
                            course.getCourseName(),
                            course.getCourseNumber(),
                            course.getMaxStudents());
                }
            } else {
                System.out.println("This instructor does not teach any courses.");
            }
            return courses;
        } else {
            System.out.println("Please enter a valid instructor id.");
            return Collections.emptyList();
        }
    }

    public List<Course> reportStudentEnrolledCourses(int studentId) {
        List<Course> courses = courseRepository.reportStudentEnrolledCourses(studentId);
        if (courses != null) {
            System.out.printf("%-30s %-30s %-15s%n", "Course Name", "Course Number", "Max Students");
            System.out.printf("%-30s %-30s %-15s%n", "------------", "------------", "------------");
            for (Course course : courses) {
                System.out.printf("%-30s %-30d %-15d%n",
                        course.getCourseName(),
                        course.getCourseNumber(),
                        course.getMaxStudents());
            }
        } else {
            System.out.println("The student has no registered courses");
        }
        return courses;
    }

    public void findTopStudentInInstructorCourses(int instructorId) {
        Student student = courseRepository.findTopStudentInInstructorCourses(instructorId);
        if (student == null) {
            System.out.println("No student found for the given instructor ID: " + instructorId);
        } else {
            System.out.printf("%-15s %-25s %-15s%n", "Student ID", "Student Name", "Student Email");
            System.out.println("----------------------------------------------------------");
            System.out.printf("%-15d %-25s %-15s%n",
                    student.getStudentID(),
                    student.getStudentName(),
                    student.getEmail());
        }
    }

    public void calculateAverageGradeForCourse(int courseId) {

        double averageGradeForCourse = courseRepository.calculateAverageGradeForCourse(courseId);

        if (averageGradeForCourse > 0) {
            System.out.println("Average success grade of the course with " + courseId + " = " + averageGradeForCourse);
        } else {
            System.out.println("The average could not be calculated because there were not enough students!");
        }

    }

    public String calculateLetterGradeForStudent(int studentId, int courseId) {
        double grade = courseRepository.calculateLetterGradeForStudent(studentId, courseId);
        String letterGrade;
        if (grade >= 0 && grade <= 59) {
            letterGrade = "F";
        } else if (grade >= 60 && grade <= 69) {
            letterGrade = "D";
        } else if (grade >= 70 && grade <= 79) {
            letterGrade = "C";
        } else if (grade >= 80 && grade <= 89) {
            letterGrade = "B";
        } else if (grade >= 90 && grade <= 100) {
            letterGrade = "A";
        } else {
            letterGrade = "NaN";  // Not a valid grade
        }

        System.out.printf("Student final grade is: %s (%.2f)%n", letterGrade, grade);

        return letterGrade;
    }

    public void calculateAverageSuccessGradeForInstructorCourses(int instructorId) {
        double averageGradeForInstructorCourses = courseRepository.calculateAverageSuccessGradeForInstructorCourses(instructorId);

        if (averageGradeForInstructorCourses > 0) {
            System.out.printf("Instructor with ID %d has an average grade of %.2f across their courses.%n",
                    instructorId, averageGradeForInstructorCourses);
        } else {
            System.out.printf("Could not calculate the average grade for instructor with ID %d. No data available or insufficient grades.%n",
                    instructorId);
        }
    }

    public void listCoursesOrderedByStudentAverageGrade() {
        List<Course> courses = courseRepository.listCoursesOrderedByStudentAverageGrade();
        for (Course course : courses) {
            System.out.printf("Course Name: %s%n", course.getCourseName());
            System.out.printf("Course Number: %d%n", course.getCourseNumber());
            System.out.printf("Instructor ID: %d%n", course.getInstructor().getInstructorId());
            System.out.printf("Max Students: %d%n", course.getMaxStudents());
            System.out.printf("Credit Number: %d%n", course.getCreditNumber());
            System.out.printf("Attendance Limit: %d%n", course.getAttendanceLimit());
            System.out.println("-------------------------");
        }
    }

    public void createCourseSchedule(int studentId) {
        List<Course> courses = courseRepository.createCourseSchedule(studentId);
        String[] weekDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String tuesdayStartTime = "08:00 AM";
        String tuesdayEndTime = "10:00 AM";
        String tuesdayDay = "Tuesday";

        Map<String, List<String>> scheduledCourses = new HashMap<>();

        for (int i = 0; i < 6; i++) {
            String currentDay = weekDays[i];
            System.out.println("Courses on " + currentDay + "\n");

            scheduledCourses.put(currentDay, new ArrayList<>());

            for (int j = 0; j < courses.size(); j++) {
                Course course = courses.get(j);
                if (currentDay.equals(tuesdayDay)) {
                    if (!scheduledCourses.get(tuesdayDay).isEmpty()) {
                        System.out.println("Conflict detected for course: " + course.getCourseName() +
                                " on " + tuesdayDay + " from " + tuesdayStartTime + " to " + tuesdayEndTime + ".");
                    } else {
                        System.out.println("   Lesson Time: " + tuesdayStartTime + " - " + tuesdayEndTime + "\n");
                        System.out.println("   Course name: " + course.getCourseName() + "\n" +
                                "   Course number: " + course.getCourseNumber() + "\n");
                        scheduledCourses.get(tuesdayDay).add(course.getCourseName());
                    }
                } else {
                    if (j == 0) {
                        System.out.println("   Lesson Time: 10:00 AM \n");
                    } else {
                        System.out.println("   ____________________\n   Lesson Time: 1:30 PM \n");
                    }
                    System.out.println("   Course name: " + course.getCourseName() + "\n" +
                            "   Course number: " + course.getCourseNumber() + "\n");
                }
            }
        }
    }

    public void generateCourseReport(int courseId) {
        Map<String, Object> courseReport = courseRepository.generateCourseReport(courseId);

        if (courseReport.isEmpty()) {
            System.out.println("No data found for the course with ID: " + courseId);
            return;
        }

        System.out.println("Course report generated successfully! \n" +
                "   Course name: " + courseReport.get("CourseName") +
                "\n   Course number: " + courseReport.get("CourseNumber") +
                "\n   Course credits: " + courseReport.get("credits") +
                "\n   Course max student count: " + courseReport.get("max_students") +
                "\n   Course attendance limit: " + courseReport.get("AttendanceLimit") +
                "\n   Course total score: " + courseReport.get("total_student_score") +
                "\n   Course student count: " + courseReport.get("student_count") +
                "\n   Course Average: " + ((int)courseReport.get("student_count") > 0 ?
                ((double) courseReport.get("total_student_score") / (int)courseReport.get("student_count")) : 0));
    }
}
