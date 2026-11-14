package e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;

import auth.Credentials;
import io.jooby.test.JoobyTest;
import peotal.Launcher;
import support.E2ETest;

@JoobyTest(value = Launcher.class, environment = "e2e", port = 0)
class AuthE2ETest extends E2ETest {

    private String serverPath;

    @Test
    void loginReturnsAccountAndCookie() throws Exception {
        HttpResponse<String> resp = client.send(
                post(serverPath, "api/auth/login", new Credentials("e2e", "secret")),
                HttpResponse.BodyHandlers.ofString());

        assertThat(resp.statusCode()).isEqualTo(200);
        assertThat(resp.headers().firstValue("set-cookie")).hasValueSatisfying(cookie -> {
            assertThat(cookie).startsWith("peotal_token=");
            assertThat(cookie).contains("HttpOnly");
            assertThat(cookie).contains("SameSite=Lax");
        });
    }

    @Test
    void loginRejectsWrongPassword() throws Exception {
        HttpResponse<String> resp = client.send(
                post(serverPath, "api/auth/login", new Credentials("e2e", "wrong")),
                HttpResponse.BodyHandlers.ofString());

        assertThat(resp.statusCode()).isEqualTo(401);
    }
}
