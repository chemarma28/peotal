
import java.time.LocalDate;

import org.jdbi.v3.core.Handle;

import categories.CategoryRepository;
import io.jooby.Jooby;
import io.jooby.avaje.inject.AvajeInjectModule;
import modules.ConverterModule;
import modules.DBModule;
import transactions.Transaction;
import transactions.TransactionRepository;

public class Launcher extends Jooby {

    public static void main(String... args) {
        Jooby.runApp(args, Launcher::new);
    }

    {
        install(new DBModule());
        install(new ConverterModule());
        install(AvajeInjectModule.of());

        path("/api", () -> {
            get("/categories", ctx -> {
                final Handle handle = require(Handle.class);
                final CategoryRepository repo = require(CategoryRepository.class);

                return repo.list(handle);
            });

            get("/transactions", ctx -> {
                final Handle handle = require(Handle.class);
                final TransactionRepository repo = require(TransactionRepository.class);

                final LocalDate begin = ctx.query("begin").to(LocalDate.class);
                final LocalDate end = ctx.query("end").to(LocalDate.class);

                return repo.list(handle, begin, end);
            });

            post("/transaction", ctx -> {
                final Handle handle = require(Handle.class);
                final TransactionRepository repo = require(TransactionRepository.class);

                final Transaction t = ctx.body(Transaction.class);

                return repo.save(handle, t);
            });
        });
    }
}
