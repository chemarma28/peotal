package support;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import db.DBModule;

public abstract class RepositoryTest {

    private static Jdbi jdbi;

    protected Handle handle;

    @BeforeAll
    static void setUpDatabase() {
        final Config db = ConfigFactory.parseResources("application.test.conf")
                .withFallback(ConfigFactory.systemProperties())
                .resolve()
                .getConfig("db");

        final JdbcDataSource source = new JdbcDataSource();
        source.setURL(db.getString("url"));
        source.setUser(db.getString("user"));
        source.setPassword(db.getString("password"));

        Flyway.configure()
                .dataSource(source)
                .locations("classpath:db")
                .load()
                .migrate();

        jdbi = Jdbi.create(source);
        DBModule.registerBeanMappers(jdbi);
    }

    @BeforeEach
    void beginTransaction() {
        handle = jdbi.open();
        handle.begin();
    }

    @AfterEach
    void rollbackTransaction() {
        handle.rollback();
        handle.close();
    }
}
