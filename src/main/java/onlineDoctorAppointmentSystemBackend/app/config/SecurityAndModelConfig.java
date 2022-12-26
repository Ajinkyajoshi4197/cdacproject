package onlineDoctorAppointmentSystemBackend.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityAndModelConfig {

	//Declaring bean for diasable Cross-Site Request Forgery (CSRF)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.csrf().disable();
        return http.build();
    }
    
    //Declaring BCryptPasswordEncoder bean for hashing password
    @Bean
	PasswordEncoder initPasswordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
    
  //Declaring ModelMapper bean for mapping dto with class object
    @Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
		
	}

}