package auth;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jooby.Environment;
import io.avaje.inject.External;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class JwtService {

    private static final String ISSUER = "peotal";
    private static final String AUDIENCE = "peotal";

    private final SecretKey key;
    private final Duration ttl;

    @Inject
    public JwtService(@External Environment env) {
        this(env.getProperty("auth.jwt.secret"), env.getConfig().getDuration("auth.jwt.ttl"));
    }

    public JwtService(String secret, Duration ttl) {
        if (secret == null || secret.getBytes(UTF_8).length < 32) {
            throw new IllegalArgumentException("auth.jwt.secret must be at least 32 bytes");
        }

        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            throw new IllegalArgumentException("auth.jwt.ttl must be positive");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(UTF_8));
        this.ttl = ttl;
    }

    public String issue(long accountId) {
        final Instant now = Instant.now();

        return Jwts.builder()
                .subject(Long.toString(accountId))
                .issuer(ISSUER)
                .audience().add(AUDIENCE).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public long parse(String token) {
        final Claims claims = Jwts.parser()
                .verifyWith(key)
                .requireIssuer(ISSUER)
                .requireAudience(AUDIENCE)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(claims.getSubject());
    }
}
