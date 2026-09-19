package modules;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.MediaType;
import static io.jooby.StatusCode.BAD_REQUEST;
import static io.jooby.StatusCode.NOT_FOUND;
import static io.jooby.StatusCode.SERVER_ERROR;

public class ExceptionHandler implements Extension {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public void install(Jooby app) throws Exception {
        app.errorCode(IllegalArgumentException.class, BAD_REQUEST);
        app.errorCode(NoSuchElementException.class, NOT_FOUND);

        app.error(SERVER_ERROR, (ctx, cause, code) -> {
            if (!ctx.accept(MediaType.json)) {
                return;
            }

            log.error("Unhandled error", cause);

            final Gson gson = app.require(Gson.class);
            ctx.setResponseType(MediaType.json);
            ctx.send(gson.toJson(Map.of("statusCode", code.value(), "message", "Internal error")), UTF_8);
        });
    }
}
