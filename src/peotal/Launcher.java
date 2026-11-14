package peotal;

import auth.AuthModule;
import categories.CategoryController_;
import db.DBModule;
import io.jooby.Jooby;
import io.jooby.avaje.inject.AvajeInjectModule;
import io.jooby.flyway.FlywayModule;
import io.jooby.jdbi.TransactionalRequest;
import web.ExceptionHandler;
import web.JsonModule;
import transactions.TransactionController_;

public class Launcher extends Jooby {

    public static void main(String... args) {
        Jooby.runApp(args, Launcher::new);
    }

    {
        install(new DBModule());
        install(new FlywayModule());
        use(new TransactionalRequest());
        install(new JsonModule());
        install(new ExceptionHandler());
        install(AvajeInjectModule.of());
        install(new AuthModule());

        mvc(new CategoryController_());
        mvc(new TransactionController_());
    }
}
