package auth;

import java.time.Duration;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import io.jooby.Cookie;
import io.jooby.Environment;
import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.SameSite;

public class AuthModule implements Extension {

    @Override
    public boolean lateinit() {
        return true;
    }

    @Override
    public void install(Jooby it) {
        final Environment env = it.getEnvironment();
        final Duration ttl = env.getConfig().getDuration("auth.jwt.ttl");
        final boolean secure = env.getConfig().getBoolean("auth.cookie.secure");

        final AuthService auth = it.require(AuthService.class);

        it.require(Jdbi.class).useHandle(handle -> auth.bootstrap(
                handle,
                env.getProperty("auth.bootstrap.username"),
                env.getProperty("auth.bootstrap.password")));

        it.post("/api/auth/login", ctx -> {
            final Credentials creds = ctx.body(Credentials.class);

            final Handle handle = ctx.require(Handle.class);
            final AuthService.LoginResult result = auth.login(handle, creds);

            final Cookie cookie = new Cookie("peotal_token", result.token())
                    .setHttpOnly(true)
                    .setSameSite(SameSite.LAX)
                    .setPath("/")
                    .setMaxAge(ttl)
                    .setSecure(secure);

            ctx.setResponseCookie(cookie);

            return result.account();
        });
    }
}
