package person;

import config.DataBaseConnectorConfig;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class PersonRepository {
    public static void createPersonTable(){
        try(Statement statement = DataBaseConnectorConfig.getConnection().createStatement()){
            String query = "DROP SEQUENCE IF EXISTS person_id_seq;" +
                    "CREATE SEQUENCE person_id_seq INCREMENT BY 1 MINVALUE 0 MAXVALUE 214748367 START 1;" +
                    "CREATE TABLE IF NOT EXISTS persons(" +
                    "id INTEGER DEFAULT nextval('person_id_seq') PRIMARY KEY," +
                    "full_name VARCHAR(255)," +
                    "email VARCHAR(255)," +
                    "password VARCHAR(255))";

            statement.execute(query);
            System.out.println("Table created successfully");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Person savePerson(Person person){
        String query = "INSERT INTO persons(full_name, email, password) VALUES (?,?,?)";
        try (PreparedStatement statement = DataBaseConnectorConfig.getConnection().prepareStatement(query)){
            statement.setString(1, person.getFullName());
            statement.setString(2, person.getEmail());
            statement.setString(3, person.getPassword());
            statement.execute();
            System.out.println("Person saved successfully");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return person;
    }
    // update , delete , getPerson

}