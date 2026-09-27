import categories.CategoryController_;
import io.jooby.Jooby;
import io.jooby.avaje.inject.AvajeInjectModule;
import io.jooby.jdbi.TransactionalRequest;
import modules.ConverterModule;
import modules.DBModule;
import modules.ExceptionHandler;
import transactions.TransactionController_;

public class Launcher extends Jooby {

    public static void main(String... args) {
        Jooby.runApp(args, Launcher::new);
    }

    {
        install(new DBModule());
        install(new ConverterModule());
        install(new ExceptionHandler());
        install(AvajeInjectModule.of());

        use(new TransactionalRequest());
        mvc(new CategoryController_());
        mvc(new TransactionController_());
    }
}
