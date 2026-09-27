package support;

import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import modules.Db;

public abstract class RepositoryTest {

    private static Jdbi jdbi;

    protected Handle handle;

    @BeforeAll
    static void setUpDatabase() {
        final JdbcDataSource source = new JdbcDataSource();
        source.setURL("jdbc:h2:mem:test-" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");

        Flyway.configure()
                .dataSource(source)
                .locations("classpath:db")
                .load()
                .migrate();

        jdbi = Db.create(source);
    }

    @BeforeEach
    void beginTransaction() {
        handle = jdbi.open();
        handle.begin();

        handle.execute("DELETE FROM transactions");
        handle.execute("DELETE FROM categories");
    }

    @AfterEach
    void rollbackTransaction() {
        handle.rollback();
        handle.close();
    }
}
