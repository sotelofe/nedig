package mx.org.inai.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import mx.org.inai.serviceImpl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	private static final String[] AUTH_WHITELIST = {            
            "/documentos/**",
            "/fichero/**",
            "/usuario/alta",
            "/notificaciones/descargaNotificaciones",
            "/api/excencion/cuestionario/descargar",
            "/usuario/recuperarUsuario",
            "/usuario/validaUsuarioToken",
            "/usuario/actualizarPass",
    };
	
	@Override
	public void configure(WebSecurity web) throws Exception {	      
	        web.ignoring().antMatchers(AUTH_WHITELIST);
	}
		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().disable().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) 	
		.and().authorizeRequests().antMatchers("/oauth/token")
		.permitAll().anyRequest().authenticated()
		.and()
		.authorizeRequests().antMatchers(HttpMethod.GET,"/user").hasRole("ADMIN")
		;
	}
	
	

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder( bCryptPasswordEncoder() );
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}	
}