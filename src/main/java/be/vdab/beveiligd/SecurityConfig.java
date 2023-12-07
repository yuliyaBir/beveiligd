package be.vdab.beveiligd;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    private static final String MANAGER = "manager";
    private static final String HELPDESKMEDEWERKER = "helpdeskmedewerker";
    private static final String MAGAZIJNIER = "magazijnier";
    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    JdbcUserDetailsManager maakPrincipals() {
        var manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
                """
                        select naam as username, paswoord as password, actief as enabled
                        from gebruikers
                        where naam = ?
                        """);
        manager.setAuthoritiesByUsernameQuery(
                """
                        select gebruikers.naam as username, rollen.naam as authorities
                        from gebruikers inner join gebruikersrollen
                        on gebruikers.id = gebruikersrollen.gebruikerId
                        inner join rollen on rollen.id = gebruikersrollen.rolId
                        where gebruikers.naam = ?
                        """);
        return manager;
    }
    @Bean
    SecurityFilterChain geefRechten(HttpSecurity http) throws Exception{
        http.formLogin(Customizer.withDefaults());
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/images/**", "/css/**", "/", "index.html", "/js/**", "/accessDenied.html", "/principal").permitAll()
                .requestMatchers("/bestellingen.html", "/bestellingen/**")
                .hasAuthority(MANAGER)
                .requestMatchers("/werknemers.html", "/werknemers/**")
                .hasAnyAuthority(MAGAZIJNIER, HELPDESKMEDEWERKER));
        http.exceptionHandling(handling -> handling.accessDeniedPage("/accessDenied.html"));
        return http.build();
    }
}
