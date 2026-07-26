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
        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.startsWith("jdbc:")) {
            return;
        }

        URI uri = URI.create(databaseUrl);
        String[] userInfo = uri.getUserInfo() != null ? uri.getUserInfo().split(":", 2) : new String[0];
        String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();

        Map<String, Object> converted = new HashMap<>();
        converted.put("spring.datasource.url", jdbcUrl);
        if (userInfo.length > 0) {
            converted.put("spring.datasource.username", userInfo[0]);
        }
        if (userInfo.length > 1) {
            converted.put("spring.datasource.password", userInfo[1]);
        }

        environment.getPropertySources()
                .addFirst(new MapPropertySource("renderDatabaseUrl", converted));
    }
}
