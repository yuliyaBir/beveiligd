package be.vdab.beveiligd;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private static final String MANAGER = "manager";
    private static final String HELPDESKMEDEWERKER = "helpdeskmedewerker";
    private static final String MAGAZIJNIER = "magazijnier";
    @Bean
    InMemoryUserDetailsManager maakPrincipals(){
        var joe = User.withUsername("joe")
                .password("{noop}theboss")
                .authorities(MANAGER)
                .build();
        var averell = User.withUsername("averell")
                .password("{noop}hungry")
                .authorities(HELPDESKMEDEWERKER, MAGAZIJNIER)
                .build();
        return new InMemoryUserDetailsManager(joe, averell);
    }
    @Bean
    SecurityFilterChain geefRechten(HttpSecurity http) throws Exception{
        http.formLogin(Customizer.withDefaults());
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/images/**", "/css/**", "/", "index.html", "/accessDenied.html").permitAll()
                .requestMatchers("/bestellingen.html", "/bestellingen/**")
                .hasAuthority(MANAGER)
                .requestMatchers("/werknemers.html", "/werknemers/**")
                .hasAnyAuthority(MAGAZIJNIER, HELPDESKMEDEWERKER));
        http.exceptionHandling(handling -> handling.accessDeniedPage("/accessDenied.html"));
        return http.build();
    }
}
