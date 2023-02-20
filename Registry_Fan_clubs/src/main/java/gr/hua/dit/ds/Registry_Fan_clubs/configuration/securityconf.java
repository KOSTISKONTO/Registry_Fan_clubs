package gr.hua.dit.ds.Registry_Fan_clubs.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class securityconf {


    @Autowired
    DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {

        return new JdbcUserDetailsManager(dataSource);}




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

        .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/newaitisigga/**").permitAll()
                .antMatchers("/addfancommand/**").permitAll()
                .antMatchers("/formaitisiellas/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout().permitAll();

        http.headers().frameOptions().sameOrigin();

        return http.build();

    }





    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web.ignoring().antMatchers(
                        "/css/**", "/js/**", "/images/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        System.out.println("encode");
        return new BCryptPasswordEncoder();
    }

}
