package br.com.logap.logitrackpro.config;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * Render/Railway expõem DATABASE_URL no formato "postgres://user:pass@host:port/db",
 * mas o driver JDBC exige "jdbc:postgresql://host:port/db" com usuário/senha à parte.
 * Converte automaticamente para que DATABASE_URL possa ser usada como veio da plataforma,
 * sem edição manual no dashboard de deploy.
 */
public class RenderDatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> converted = new HashMap<>();

        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl != null && !databaseUrl.startsWith("jdbc:")) {
            ParsedDatabaseUrl parsed = parseDatabaseUrl(databaseUrl);
            converted.put("spring.datasource.url", parsed.jdbcUrl());
            if (parsed.username() != null) {
                converted.put("spring.datasource.username", parsed.username());
            }
            if (parsed.password() != null) {
                converted.put("spring.datasource.password", parsed.password());
            }
        }

        String directDatabaseUrl = environment.getProperty("DATABASE_URL_DIRECT");
        if (directDatabaseUrl != null && !directDatabaseUrl.startsWith("jdbc:")) {
            ParsedDatabaseUrl parsed = parseDatabaseUrl(directDatabaseUrl);
            converted.put("spring.flyway.url", parsed.jdbcUrl());
            if (parsed.username() != null) {
                converted.put("spring.flyway.user", parsed.username());
            }
            if (parsed.password() != null) {
                converted.put("spring.flyway.password", parsed.password());
            }
        }

        if (converted.isEmpty()) {
            return;
        }

        environment.getPropertySources()
                .addFirst(new MapPropertySource("renderDatabaseUrl", converted));
    }

    private ParsedDatabaseUrl parseDatabaseUrl(String rawUrl) {
        URI uri = URI.create(rawUrl);
        String[] userInfo = uri.getUserInfo() != null ? uri.getUserInfo().split(":", 2) : new String[0];
        String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
        if (uri.getQuery() != null) {
            jdbcUrl += "?" + uri.getQuery();
        }

        String username = userInfo.length > 0 ? userInfo[0] : null;
        String password = userInfo.length > 1 ? userInfo[1] : null;

        return new ParsedDatabaseUrl(jdbcUrl, username, password);
    }

    private record ParsedDatabaseUrl(String jdbcUrl, String username, String password) {
    }
}
