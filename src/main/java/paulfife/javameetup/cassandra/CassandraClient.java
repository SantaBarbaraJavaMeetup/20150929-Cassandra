package paulfife.javameetup.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;

/**
 * Base class for Cassandra Connections.
 */
public class CassandraClient {
    public static Session connect() {
        return Cluster.builder()
                .addContactPoint("localhost")
                // Token aware policy tells the driver to use the partition key (hash) to find the appropriate node to talk to
                // RoundRobinPolicy will fallback on round robin among nodes - use DCAware for multi-DC setups!
                .withLoadBalancingPolicy(new TokenAwarePolicy(new RoundRobinPolicy()))
                .build()
                .connect();
    }
}
