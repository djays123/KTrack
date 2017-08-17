package ktrack.security.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@EnableWebSecurity(debug = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService loginService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/wicket/resource/**").permitAll()
				.antMatchers("/**").hasRole("USER").and().formLogin().loginPage("/").permitAll()
				.defaultSuccessUrl("/dogs").and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
				.clearAuthentication(true).and().csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginService).passwordEncoder(new Md5PasswordEncoder());
	}
}