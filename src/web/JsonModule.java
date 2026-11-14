package web;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.MediaType;

public class JsonModule implements Extension {

    private final Gson gson;

    public JsonModule() {
        this.gson = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
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
    }
}
