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
    protected ConstraintValidator validator = new ConstraintValidator(new ValidationFactory().validator());

    @BeforeAll
    static void setUpDatabase() {
        final Config config = ConfigFactory.parseResources("application.test.conf")
                .withFallback(ConfigFactory.parseResources("application.conf"))
                .withFallback(ConfigFactory.systemProperties())
                .resolve();

        final JdbcDataSource source = new JdbcDataSource();
        source.setURL(config.getString("db.url"));
        source.setUser(config.getString("db.user"));
        source.setPassword(config.getString("db.password"));

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
