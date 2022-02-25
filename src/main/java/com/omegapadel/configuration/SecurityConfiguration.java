package com.omegapadel.configuration;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Inject
	DataSource dataSource;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/javax.faces.resource/**").permitAll().and().authorizeRequests()
				.antMatchers("/login.xhtml").permitAll().antMatchers("/", "/index.xhtml", "/error.xhtml").permitAll()
				.antMatchers("/listaMarcas.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/listaTipoAccesorio.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/listaPalasCreadas.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/nuevaPala.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/nuevasPackPelotas.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/listaPelotasCreadas.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/nuevasPackPelotas.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/nuevoAnuncio.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/nuevoPaletero.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/listaPaleteros.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/nuevoZapatilla.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/listaPedidosParaTramitar.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/listaZapatillas.xhtml").hasAnyAuthority("empleado", "admin")
				.antMatchers("/listaEmpleados.xhtml").hasAuthority("admin")
				.antMatchers("/nuevoEmpleado.xhtml").hasAuthority("admin").antMatchers("/listaPuestoTrabajo.xhtml")
				.hasAuthority("admin").antMatchers("/listaEmpleados.xhtml").hasAuthority("admin")
				.antMatchers("/editarCliente.xhtml").hasAuthority("cliente").antMatchers("/editarEmpleado.xhtml")
				.hasAnyAuthority("admin", "empleado");

		http.formLogin().loginPage("/login.xhtml").permitAll().failureUrl("/login.xhtml?error=true");

//		http.logout(
//				logout -> logout.logoutUrl("/logout").addLogoutHandler(new SecurityContextLogoutHandler()));

		http.csrf().disable();

	}

	@Override
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(this.dataSource)
				.usersByUsernameQuery("select usuario,contrasenya,activo from usuario where usuario = ?")
				.authoritiesByUsernameQuery("select usuario,rol from roles where usuario = ?")
				.passwordEncoder(this.passwordEncoder());
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
