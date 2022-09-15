package sonia.meetapp.users;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().authorizeRequests().antMatchers("/hello").permitAll().antMatchers("/events").permitAll().antMatchers(HttpMethod.GET, "/participants").permitAll().antMatchers("/participants").authenticated().and().httpBasic().and().build();
    }

    @Bean
    public UserDetailsManager userDetailsService() {
        return new UserDetailsManager() {
            @Override
            public void createUser(UserDetails user) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void updateUser(UserDetails user) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void deleteUser(String username) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean userExists(String username) {
                throw new UnsupportedOperationException();
            }

            @Override
            public User loadUserByUsername(String email) {
                return userService.loadUserByUsername(email);
            }
        };
    }
}
