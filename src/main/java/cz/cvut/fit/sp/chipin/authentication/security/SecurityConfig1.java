//package cz.cvut.fit.sp.chipin.authentication.security;
//
//import cz.cvut.fit.sp.chipin.authentication.userAccount.UserAccountService;
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@AllArgsConstructor
//@EnableWebSecurity
//public class SecurityConfig1 {
//
//    private final UserAccountService userAccountService;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//            .cors().disable()
//            .csrf().disable();
//
//        httpSecurity.userAccountDetailsService(userAccountService);
//
//        httpSecurity.authorizeRequests()
//                .antMatchers("/api/v*/registration/**", "/api/v*/login/**", "/resources/**")
//                    .permitAll()
//                .antMatchers("/**")
//                    .authenticated()
//                    .and()
//                    .oauth2ResourceServer().jwt();
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider =
//                new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(bCryptPasswordEncoder());
//        provider.setUserAccountDetailsService(userAccountService);
//        return provider;
//    }
//}
