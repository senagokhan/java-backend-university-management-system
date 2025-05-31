import config.DataBaseConnectorConfig;
import instructor.Instructor;
import instructor.InstructorRepository;
import person.Person;
import person.PersonRepository;

public class Main {
    public static void main(String[] args) {
        DataBaseConnectorConfig.setConnection();


        InstructorRepository instructorRepository = new InstructorRepository();
        Instructor instructor = new Instructor();
        instructor.setInstructorFullName("Sena");
        instructor.setEmail("deneme@gmail.com");
        instructor.setPassword("123456");
        instructorRepository.saveInstructor(instructor);

        PersonRepository.createPersonTable();

        PersonRepository personRepository = new PersonRepository();
        Person person = new Person();
        person.setFullName("Furkan");
        person.setEmail("deneme@gmail.com");
        person.setPassword("12345");
        personRepository.savePerson(person);

    }
}
