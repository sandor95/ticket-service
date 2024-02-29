//package hu.otp.partner.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http.csrf(AbstractHttpConfigurer::disable)
//                    .httpBasic(Customizer.withDefaults())
//                    .addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                    .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                    .authorizeHttpRequests(buildRequestMatchers())
//                    .build();
//    }
//
//    private static Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>
//        buildRequestMatchers() {
//        return requests ->
//                requests.requestMatchers("/swagger/**", "/actuator**").permitAll()
//                        .anyRequest().permitAll();
//    }
//}