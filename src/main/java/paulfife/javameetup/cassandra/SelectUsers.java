package paulfife.javameetup.cassandra;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Collectors;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;

/**
 * Demo selecting data.
 */
public class SelectUsers {
    public static void main(String[] args) {
        try {
            Session session = CassandraClient.connect();

            // Lookup a user by their userid
            Row row = session.execute("SELECT fullname, birthyear FROM meetupdemo.users where userid=?", "joeschmoe")
                            .one();
            System.out.println("Full name of joeschmoe is: " + row.getString("fullname") + ", born in: " + row.getInt(1));

            // Find users born on a certain day
            ResultSet rs = session.execute("SELECT fullname FROM meetupdemo.users_birthdays WHERE birthmonth=? and birthday=?", 1, 1);
            System.out.println("Users born on 1/1: " + rs.all().stream().map(r -> r.getString("fullname")).collect(Collectors.joining(",")));

            // Find 3 most recent messages from Joe Schmoe, leveraging the DESC sort order defined in the schema.
            // Use an iterator for large results - it will fetch results in batches rather than holding all in memory.
            // Convert the time-based UUID to a Java Date.
            // Notice when using Async insertion the order may vary!
            Iterator<Row> rsIt = session.execute("SELECT dateOf(ts), message FROM meetupdemo.users_messages WHERE userid=? LIMIT ?", "joeschmoe", 3).iterator();
            System.out.println("Joe Schmoe's recent messages:");
            while(rsIt.hasNext()) {
                row = rsIt.next();
                System.out.println(row.getDate(0).toString() + ": " + row.getString("message"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
