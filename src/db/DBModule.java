package db;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

import categories.Category;
import categories.CategorySummary;
import io.jooby.Environment;
import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.jdbi.JdbiModule;
import transactions.Transaction;

public class DBModule implements Extension {

    @Override
    public void install(Jooby it) throws Exception {
        final Environment env = it.getEnvironment();

        it.getServices().put(DataSource.class, createDataSource(env));
        it.install(new JdbiModule());

        registerBeanMappers(it.require(Jdbi.class));
    }

    public static void registerBeanMappers(Jdbi jdbi) {
        jdbi.registerRowMapper(BeanMapper.factory(Transaction.class));
        jdbi.registerRowMapper(BeanMapper.factory(Category.class));
        jdbi.registerRowMapper(BeanMapper.factory(CategorySummary.class));
    }

    private static DataSource createDataSource(Environment env) {
        final JdbcDataSource s = new JdbcDataSource();

        s.setURL(env.getProperty("db.url"));
        s.setUser(env.getProperty("db.user"));
        s.setPassword(env.getProperty("db.password"));

        return s;
    }
}
