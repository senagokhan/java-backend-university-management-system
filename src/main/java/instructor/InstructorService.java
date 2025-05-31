package instructor;

import course.Course;
import java.util.List;

public class InstructorService {

    private final InstructorRepository instructorRepository;


    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public Instructor createInstructor(Instructor instructor) {
        System.out.println("Instructor" + instructor.getInstructorFullName() + " has been created ");
        return instructorRepository.saveInstructor(instructor);
    }

    public void deleteInstructor(int id) {
        Instructor instructor = InstructorRepository.getInstructorById(id);
        if (instructor != null) {
            System.out.println("Instructor has been deleted successfully");
            InstructorRepository.deleteInstructor(id);
        } else {
            System.out.println("Instructor not found with ID : " + id);
        }
    }

    public void update(String instructor_name, String email, String password, int id) {
        Instructor instructor = InstructorRepository.getInstructorById(id);
        if (instructor != null) {
            if (instructor.getInstructorId() == id) {
                System.out.println("Instructor has been updated");
                InstructorRepository.updateInstructor(instructor_name, email, password, id);
            } else {
                System.out.println("Instructor ID does not match for the given instructor id.");
            }
        } else {
            System.out.println("Instructor not found with instructor id: " + id);
        }
    }

    public void listMostRecommendedCoursesForInstructor(int instructorId, int topCount) {
        List<Course> recommendedCourses = instructorRepository.getMostRecommendedCoursesForInstructor(instructorId, topCount);
        for (Course course : recommendedCourses) {
            System.out.println("Course Name: " + course.getCourseName());
            System.out.println("Course Number: " + course.getCourseNumber());
            System.out.println("Max Students: " + course.getMaxStudents());
            System.out.println("Credit Number: " + course.getCreditNumber());
            System.out.println("-------------------------");
        }
    }
}
