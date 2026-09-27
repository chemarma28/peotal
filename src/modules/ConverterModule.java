package modules;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.time.Instant;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import converters.CurrencyConverter;
import converters.LocalDateConverter;
import converters.TransactionTypeConverter;
import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.MediaType;
import io.jooby.value.ValueFactory;
import transactions.Currency;
import transactions.TransactionType;

public class ConverterModule implements Extension {

    private final Gson gson;
    private final LocalDateConverter instantConverter;
    private final TransactionTypeConverter transactionTypeConverter;
    private final CurrencyConverter currencyConverter;

    public ConverterModule() {
        this.instantConverter = new LocalDateConverter();
        this.transactionTypeConverter = new TransactionTypeConverter();
        this.currencyConverter = new CurrencyConverter();

        this.gson = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(LocalDate.class, instantConverter)
                .registerTypeAdapter(TransactionType.class, transactionTypeConverter)
                .registerTypeAdapter(Currency.class, currencyConverter)
                .create();
    }

    @Override
    public void install(Jooby it) throws Exception {
        it.getServices().put(Gson.class, gson);

        it.encoder(MediaType.json, (ctx, result) -> {
            String json = gson.toJson(result);
            ctx.setDefaultResponseType(MediaType.json);

            return ctx.getOutputFactory().wrap(json);
        });

        it.decoder(MediaType.json, (ctx, type) -> {
            final byte[] body = ctx.body().bytes();

            return gson.fromJson(new JsonReader(new InputStreamReader(new ByteArrayInputStream(body), UTF_8)), type);
        });

        final ValueFactory valueFactory = it.getValueFactory();

        valueFactory.put(Instant.class, instantConverter);
        valueFactory.put(TransactionType.class, new TransactionTypeConverter());
        valueFactory.put(Currency.class, new CurrencyConverter());
    }
}
