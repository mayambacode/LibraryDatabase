package ca.mayambabokambmbu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	EmbeddedDatabase dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
				.build();
	}
	
	private LoggingAccessDeniedHandler accessHandler;
	
	@Autowired
	public void setAccessDeniedHandler(LoggingAccessDeniedHandler accessHandler) {
		this.accessHandler = accessHandler;
	}
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests().antMatchers("/login").permitAll()//For login.html page
				.antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/secured/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/admin/**").hasAnyRole("ADMIN")
				// for access to h2-console
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/", "/**").permitAll()
				.and()
				.formLogin().loginPage("/login")
				.defaultSuccessUrl("/secured") // changing secured
				.and()
				.logout().invalidateHttpSession(true)
				.clearAuthentication(true)
				.and()
				.exceptionHandling()
				.accessDeniedHandler(accessHandler);
		
		http.headers().frameOptions().sameOrigin();
		
		http.csrf().disable();
		return http.build();
		
		
	}
	
	@Autowired
	@Lazy
	JdbcUserDetailsManager jdbcUserDetailsManager;
	
	@Bean
	UserDetailsService users(EmbeddedDatabase dataSource) {
		
		
		UserDetails user = User.builder()
				.username("user")
				.password(passwordEncoder.encode("password")) // password = password
				.roles("USER")
				.build();
		UserDetails admin = User.builder()
				.username("admin")
				.password(passwordEncoder.encode("password")) // password = password
				.roles("USER", "ADMIN")
				.build();
		
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.createUser(user);
		users.createUser(admin);
		
		return users;
	}
	
	
}
