package paulfife.javameetup.cassandra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import com.datastax.driver.core.Session;
import paulfife.javameetup.cassandra.model.User;

/**
 * Create some data, but execute it all concurrently with async - much faster!
 */
public class CreateUsersAsync
{
    public static void main(String[] args) {
        try {
            Session session = CassandraClient.connect();
            long startTime = System.currentTimeMillis();
            List<Future> futures = new ArrayList<>();
            futures.addAll(createUser(session, new User("joeschmoe", "Joe Schmoe", 1980, 1, 1)));
            futures.addAll(createUser(session, new User("janeschmoe", "Jane Schmoe", 1980, 1, 1)));
            futures.addAll(createUser(session, new User("anotherperson", "Another Person", 1981, 2, 2)));
            for(Future f : futures) {
                f.get();
            }
            System.out.println("Created users in " + (System.currentTimeMillis() - startTime) + "ms.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static Collection<Future> createUser(Session session, User user) {
        List<Future> futures = new ArrayList<>();
        futures.add(session.executeAsync("INSERT INTO meetupdemo.users (userid, birthyear, birthmonth, birthday, fullname) VALUES (?,?,?,?,?)", user.getUserId(),
                user.getBirthYear(), user.getBirthMonth(), user.getBirthDay(), user.getFullName()));

        futures.add(session.executeAsync("INSERT INTO meetupdemo.users_birthdays (userid, birthyear, birthmonth, birthday, fullname) VALUES (?,?,?,?,?)", user.getUserId(),
                user.getBirthYear(), user.getBirthMonth(), user.getBirthDay(), user.getFullName()));

        futures.add(session.executeAsync("INSERT INTO meetupdemo.users_messages (userid, ts, fullname, message) VALUES (?,now(),?,?)", user.getUserId(), user.getFullName(),
                "Hi, my name is " + user.getFullName()));

        futures.add(session.executeAsync("INSERT INTO meetupdemo.users_messages (userid, ts, fullname, message) VALUES (?,now(),?,?)", user.getUserId(), user.getFullName(),
                "I was born on " + user.getBirthDay() + "/" + user.getBirthMonth()));

        futures.add(session.executeAsync("INSERT INTO meetupdemo.users_messages (userid, ts, fullname, message) VALUES (?,now(),?,?)", user.getUserId(), user.getFullName(),
                "I was born in " + user.getBirthYear()));

        futures.add(session.executeAsync("INSERT INTO meetupdemo.users_messages (userid, ts, fullname, message) VALUES (?,now(),?,?)", user.getUserId(), user.getFullName(),
                "Find me using my userid: " + user.getUserId()));
        return futures;
    }
}
