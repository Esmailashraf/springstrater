package org.studyeasy.springstrater.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.studyeasy.springstrater.util.constants.Privileges;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

        private static final String[] PUBLIC_URLS = {
                        "/",
                        "/api/v1/**",
                        "/register",
                        "/forgot-password",
                        "/reset-password",
                        "/change-password",
                        "/login",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fonts/**",
                        "/post/**"
        };

        private static final String DB_CONSOLE = "/db-console/**";

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(PUBLIC_URLS).permitAll()
                                                .requestMatchers(DB_CONSOLE).permitAll()
                                                .requestMatchers("/profile/**").authenticated()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/editor/**").hasAnyRole("ADMIN", "EDITOR")
                                                .requestMatchers("/test/**")
                                                .hasAuthority(Privileges.ACCESS_ADMIN_PANEL.getName())
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .rememberMe(rememberMe -> rememberMe.rememberMeParameter("remember-me"))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers(DB_CONSOLE))
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.disable()))
                                .httpBasic(httpBasic -> {
                                });

                return http.build();
        }

        @Bean
        public static PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}