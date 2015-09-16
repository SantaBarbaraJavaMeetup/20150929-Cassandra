package paulfife.javameetup.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import paulfife.javameetup.cassandra.model.User;

/**
 * Create some data.
 */
public class CreateUsers {
    public static void main(String[] args) {
        try {
            Session session = CassandraClient.connect();
            long startTime = System.currentTimeMillis();
            createUser(session, new User("joeschmoe", "Joe Schmoe", 1980, 1, 1));
            createUser(session, new User("janeschmoe", "Jane Schmoe", 1980, 1, 1));
            createUser(session, new User("anotherperson", "Another Person", 1981, 2, 2));
            System.out.println("Created users in " + (System.currentTimeMillis()-startTime) + "ms.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void createUser(Session session, User user) {
        session.execute("INSERT INTO meetupdemo.users (userid, birthyear, birthmonth, birthday, fullname) VALUES (?,?,?,?,?)",
                user.getUserId(), user.getBirthYear(), user.getBirthMonth(), user.getBirthDay(), user.getFullName());

        session.execute("INSERT INTO meetupdemo.users_birthdays (userid, birthyear, birthmonth, birthday, fullname) VALUES (?,?,?,?,?)",
                user.getUserId(), user.getBirthYear(), user.getBirthMonth(), user.getBirthDay(), user.getFullName());

        session.execute("INSERT INTO meetupdemo.users_messages (userid, ts, fullname, message) VALUES (?,now(),?,?)",
                user.getUserId(), user.getFullName(), "Hi, my name is " + user.getFullName());

        session.execute("INSERT INTO meetupdemo.users_messages (userid, ts, fullname, message) VALUES (?,now(),?,?)",
                user.getUserId(), user.getFullName(), "I was born on " + user.getBirthDay() + "/" + user.getBirthMonth());

        session.execute("INSERT INTO meetupdemo.users_messages (userid, ts, fullname, message) VALUES (?,now(),?,?)",
                user.getUserId(), user.getFullName(), "I was born in " + user.getBirthYear());

        session.execute("INSERT INTO meetupdemo.users_messages (userid, ts, fullname, message) VALUES (?,now(),?,?)",
                user.getUserId(), user.getFullName(), "Find me using my userid: " + user.getUserId());
    }
}
