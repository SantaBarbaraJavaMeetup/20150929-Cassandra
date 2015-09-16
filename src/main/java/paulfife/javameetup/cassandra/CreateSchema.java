package paulfife.javameetup.cassandra;

import com.datastax.driver.core.Session;

/**
 * Creates the schema for the Cassandra demo.
 */
public class CreateSchema {
    public static void main(String[] args) {
        try {
            Session session = CassandraClient.connect();

            // Create a KEYSPACE to hold our tables - Think of a keyspace like a SQL schema.
            session.execute("CREATE KEYSPACE IF NOT EXISTS meetupdemo WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }");

            // Create a TABLE for users if one does not already exist.
            // Users can be queried by userid
            session.execute("CREATE TABLE  IF NOT EXISTS meetupdemo.users (userid text PRIMARY KEY, fullname text, birthday int, birthmonth int, birthyear int)");

            // Create a TABLE for user's birthdays
            // We denormalize the entire user table to eliminate multiple reads, leveraging Cassandra's efficient writes.
            // The birthday (month+year) is the partition key and the userid is a clustering key.
            // All userids for a particular birthday are stored beside each other on disk.
            session.execute("CREATE TABLE IF NOT EXISTS meetupdemo.users_birthdays (userid text, fullname text, birthday int, birthmonth int, birthyear int, "
                    + "PRIMARY KEY ((birthmonth, birthday), userid))");

            // Create a TABLE for user's messages.
            // We denormalize values we might need while displaying a message, like the user's full name.
            // This table uses the userid as a partition key and ts as a clustering key.
            // All rows for a user are stored beside each other on disk, sorted with the newest messages first.
            session.execute("CREATE TABLE IF NOT EXISTS meetupdemo.users_messages (userid text, fullname text, ts timeuuid, message text, " +
                    "PRIMARY KEY (userid, ts))" +
                    "WITH CLUSTERING ORDER BY (ts DESC)");

            System.out.println("Create schema finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
