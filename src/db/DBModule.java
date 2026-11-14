package db;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

import auth.Account;
import auth.AccountCredentials;
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
        jdbi.registerRowMapper(BeanMapper.factory(Account.class));
        jdbi.registerRowMapper(BeanMapper.factory(AccountCredentials.class));
    }

    private static DataSource createDataSource(Environment env) {
        final String url = env.getProperty("db.url");

        if (url == null || url.isBlank()) {
            throw new IllegalStateException(
                    "db.url is not configured: set PEOTAL_DB_URL or use application.dev.conf");
        }

        final JdbcDataSource s = new JdbcDataSource();

        s.setURL(url);
        s.setUser(env.getProperty("db.user"));
        s.setPassword(env.getProperty("db.password"));

        return s;
    }
}
