package com.mindhub.homeBanking.configurations;

import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    ClientRepository clientRepo;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputEmail -> {

            Client foundClient = clientRepo.findByEmail(inputEmail);

            if (foundClient != null) {
                if (foundClient.getEmail().endsWith("@admin.mindhub")) {
                    return new User(foundClient.getEmail(), foundClient.getPassword(),
                            AuthorityUtils.createAuthorityList("ADMIN"));
                } else {
                    return new User(foundClient.getEmail(), foundClient.getPassword(),
                            AuthorityUtils.createAuthorityList("CLIENT"));
                }

            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputEmail);
            }
        });

        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        UserDetails inMemoryAdmin = User.withUsername("memoAdmin").password(passwordEncoder().encode("123")).authorities("ADMIN").build();
        userDetailsService.createUser(inMemoryAdmin);
        auth.userDetailsService(userDetailsService);
    }

}
