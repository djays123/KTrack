package ktrack.security.wicket;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.giffing.wicket.spring.boot.context.security.AuthenticatedWebSessionConfig;

@Component
public class AuthenticatedSession implements AuthenticatedWebSessionConfig {

	@Override
	public Class<? extends AbstractAuthenticatedWebSession> getAuthenticatedWebSessionClass() {
		return AuthenticatedWebSession.class;
	}

	public static class AuthenticatedWebSession extends AbstractAuthenticatedWebSession {

		/**
		 * The default serial versions ID
		 */
		private static final long serialVersionUID = 1L;

		public AuthenticatedWebSession(Request request) {
			super(request);
		}

		@Override
		public Roles getRoles() {
			Roles roles = new Roles();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				roles.add(authority.getAuthority());
			}
			return roles;
		}

		@Override
		public boolean isSignedIn() {
			return SecurityContextHolder.getContext().getAuthentication() != null;
		}

	}

}
