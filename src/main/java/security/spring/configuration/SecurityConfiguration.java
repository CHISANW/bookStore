package security.spring.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import security.spring.handler.CustomAuthenticationFailureHandler;
import security.spring.handler.OAuth2SuccessHandler;
import security.spring.service.member.CustomUserDetailsService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static String url[]={"/","/login","/join","/logout","/verify/email","/checkDuplicateId","/checkDuplicatePwd","/login-error","/login-disabled","/login-emailVerified",
    "/item/itemAll","/item/**/info","/updateQuantity","/changePhone"};

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().
                requiresChannel().anyRequest().requiresSecure().and()
                .authorizeRequests()
                .antMatchers(url).permitAll()
                .antMatchers(HttpMethod.POST,"/basket").permitAll()
                .antMatchers(HttpMethod.GET,"/baskets").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").failureHandler(customAuthenticationFailureHandler).defaultSuccessUrl("/",true)
                .and()
                .rememberMe().key("remember-me-key").rememberMeCookieName("bookStore-remember-me")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(new OAuth2SuccessHandler())
                .and()
                .logout().logoutSuccessUrl("/");

        http.cors()
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/images/**","/css/**","/js/**", "/uploads/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }

    @Bean
    public HttpFirewall allowUrlEncode(){
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://service.iamport.kr");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
