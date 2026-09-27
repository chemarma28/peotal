package modules;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.core.Jdbi;

import io.jooby.Environment;
import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.flyway.FlywayModule;
import io.jooby.jdbi.JdbiModule;

public class DBModule implements Extension {

    @Override
    public void install(Jooby it) throws Exception {
        final Environment env = it.getEnvironment();

        it.getServices().put(DataSource.class, dataSource(env));
        it.install(new FlywayModule());
        it.install(new JdbiModule());

        Db.registerMappers(it.require(Jdbi.class));
    }

    private DataSource dataSource(Environment env) {
        final JdbcDataSource s = new JdbcDataSource();

        s.setURL(env.getProperty("db.url"));
        s.setUser(env.getProperty("db.user"));
        s.setPassword(env.getProperty("db.password"));

        return s;
    }
}
