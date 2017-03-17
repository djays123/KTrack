package ktrack.security.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ktrack.entity.User;
import ktrack.repository.UserRepository;

@Component
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		 * Here add user data layer fetching from the MongoDB. I have used
		 * userRepository
		 */
		User user = userRepository.findByName(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		} else {			
			String[] roles = user.getRole().split(",");
			Collection<GrantedAuthority> authorities = new ArrayList<>(roles.length);
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			UserDetails details = new org.springframework.security.core.userdetails.User(user.getName(),
					user.getPassword(), authorities);
			return details;
		}
	}
}
