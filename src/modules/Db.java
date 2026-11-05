package modules;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

import categories.Category;
import categories.CategorySummary;
import transactions.Currency;
import transactions.Transaction;
import transactions.TransactionType;

public final class Db {

    private Db() {
    }

    public static Jdbi create(DataSource dataSource) {
        final Jdbi jdbi = Jdbi.create(dataSource);
        registerMappers(jdbi);
        return jdbi;
    }

    public static void registerMappers(Jdbi jdbi) {
        jdbi.registerColumnMapper(TransactionType.class, (rs, col, ctx) -> TransactionType.valueOf(rs.getString(col)));
        jdbi.registerColumnMapper(Currency.class, (rs, col, ctx) -> Currency.valueOf(rs.getString(col)));
        jdbi.registerRowMapper(BeanMapper.factory(Transaction.class));
        jdbi.registerRowMapper(BeanMapper.factory(Category.class));
        jdbi.registerRowMapper(BeanMapper.factory(CategorySummary.class));
    }
}
