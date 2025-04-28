package com.example.SpringSecurityEx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  //Saying that use this class for security not default one
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;


    //The below method is used to create the security filter chain and use that for authentication
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer -> customizer.disable())  //It will disable the csrf token
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/register","/login").permitAll() //It will allow the register and login requests without authentication
                        .anyRequest().authenticated()) //It will authenticate all the requests

                .httpBasic(Customizer.withDefaults()) //To enable the default basic authentication through Rest API like Postman
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  //To make the session stateless
        //        http.formLogin(Customizer.withDefaults()); //To enable the default login page


//        Customizer<CsrfConfigurer<HttpSecurity>> custCsrf = new Customizer<CsrfConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(CsrfConfigurer<HttpSecurity> customizer) {
//                customizer.disable();
//            }
//        };
//        http.csrf(custCsrf.disable());
        return http.build();
    }
    //The below method is used to create the in memory user details manager and use that for authentication
//    @Bean
//    public UserDetailsService userDetailsService()
//    {
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("mine1")
//                .password("1234")
//                .roles("USER")
//                .build();
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("mine2")
//                .password("12345")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1,user2);
//    }


    //The below is used to change the Authentication provider and use the below one not default one
    @Bean
    public AuthenticationProvider authenticationProvider() {
        //DaoAuthenticationProvider is used to authenticate the user with the database here AuthenticationProvider is an interface
        //so we cannot instantiate it directly hence we are using the DaoAuthenticationProvider which indirectly implements the AuthenticationProvider
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); //NoOpPasswordEncoder is used to not encode the password
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }
}
