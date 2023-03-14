package com.mindhub.homeBanking.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization extends WebSecurityConfigurerAdapter  {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()

                .antMatchers("/manager.html").hasAuthority("ADMIN")
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/rest/**").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/clients","/api/login").permitAll()
              .antMatchers(HttpMethod.POST,"/api/login").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/clients/current").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current").hasAuthority("CLIENT")
                .antMatchers("/api/accounts").hasAuthority("CLIENT")
                .antMatchers("/api/accounts/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/renew-card").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,  "/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,  "/api/transaction").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,  "/api/loans").hasAuthority("CLIENT")
                .antMatchers("/api/loans/**/DTO").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/loans/final-payments").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,  "/api/loans/**").hasAuthority("CLIENT")
                .antMatchers("/api/**").hasAuthority("ADMIN")

                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/web/assets/**").permitAll()
                .antMatchers("/web/accounts.html").hasAuthority("CLIENT")
                .antMatchers("/web/account.html").hasAuthority("CLIENT")
                .antMatchers("/web/cards.html").hasAuthority("CLIENT")
                .antMatchers("/web/create-cards.html").hasAuthority("CLIENT")
                .antMatchers("/web/transaction.html").hasAuthority("CLIENT");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        //is authed but doesn't have needed auth
        http.exceptionHandling().accessDeniedHandler((req, res, ex) ->res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
         .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);

//        http
//                .authorizeRequests()
//                .antMatchers("/manager/manager.html").hasAuthority("ADMIN")
//                .antMatchers("/h2-console").hasAuthority("ADMIN")
//                .antMatchers("/rest/**").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
//                .antMatchers(HttpMethod.POST,"/api/login").permitAll()
//                .antMatchers(HttpMethod.PUT, "/api/clients").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.PATCH, "/api/clients").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.POST, "/api/clients/current").hasAuthority("CLIENT")
//                .antMatchers(HttpMethod.GET, "/api/clients/current").hasAuthority("CLIENT")
//                .antMatchers(HttpMethod.POST, "/api/clients/renew-card").hasAuthority("CLIENT")
//                .antMatchers(HttpMethod.POST,  "clients/current/accounts").hasAuthority("CLIENT")
//                .antMatchers("/api/**").hasAuthority("ADMIN")
//                .antMatchers("/web/index.html").permitAll()
//                .antMatchers("/web/assets/**").permitAll()
//                .antMatchers("/web/accounts.html").hasAuthority("CLIENT")
//                .antMatchers("/web/account.html").hasAuthority("CLIENT")
//                .antMatchers("/web/cards.html").hasAuthority("CLIENT")
//                .antMatchers("/web/create-cards.html").hasAuthority("CLIENT")
//                .and() // chaining with and()
//                .formLogin()
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .loginPage("/api/login")
//                .and() // chaining with and()
//                .logout()
//                .logoutUrl("/api/logout")
//                .deleteCookies("JSESSIONID")
//                // turn off checking for CSRF tokens
//                .and()
//                .csrf()
//                .disable()
//                //disabling frameOptions so h2-console can be accessed
//                .headers().frameOptions().disable()
//                // if user is not authenticated, just send an authentication failure response
//                .and().exceptionHandling()
//                .authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                // if login is successful, just clear the flags asking for authentication
//                .and().formLogin()
//                .successHandler((req, res, auth) -> clearAuthenticationAttributes(req))
//                // if login fails, just send an authentication failure response
//                .failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                // if logout is successful, just send a success response
//                .and().logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

//        return http.build();

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }
    }

}
