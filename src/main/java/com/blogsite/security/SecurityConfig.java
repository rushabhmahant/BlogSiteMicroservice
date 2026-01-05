package com.blogsite.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {
	
	@Autowired
    private JwtFilter jwtFilter;

	/*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/blogsite/user/register", "/api/v1/blogsite/user/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    */
	
	/* Working method below - but with CORS policy issue
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable() 
				.authorizeHttpRequests().anyRequest().permitAll(); 

		return http.build();
	}
	*/
	
	/* Working method for all APIs
	 * @Bean public SecurityFilterChain configure(HttpSecurity http) throws
	 * Exception {
	 * 
	 * CorsConfiguration corsConfiguration = new CorsConfiguration();
	 * corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control",
	 * "Content-Type", "Access-Control-Allow-Origin", "status"));
	 * corsConfiguration.setAllowedOriginPatterns(List.of("*"));
	 * corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE",
	 * "PUT","OPTIONS","PATCH", "DELETE"));
	 * corsConfiguration.setAllowCredentials(true);
	 * corsConfiguration.setExposedHeaders(List.of("Authorization"));
	 * 
	 * 
	 * http.csrf().disable();
	 * http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.
	 * STATELESS);
	 * 
	 * http.authorizeHttpRequests() .anyRequest().permitAll()
	 * .and().csrf().disable().cors().configurationSource(request ->
	 * corsConfiguration);
	 * 
	 * return http.build(); }
	 */
	
	@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    	
    	CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin", "status"));
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        
    	
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api/v1/blogsite/user/login", "/api/v1/blogsite/user/register").permitAll()
                .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(request -> corsConfiguration));
         
        return http.build();
    }
	
	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
	    UserDetails user = User.builder()
	            .username("user")
	            .password(passwordEncoder.encode("14b96ac3-0684-4d11-bd6c-9cc87f3ffa97"))
	            .roles("ADMIN")
	            .build();

	    return new InMemoryUserDetailsManager(user);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

}
