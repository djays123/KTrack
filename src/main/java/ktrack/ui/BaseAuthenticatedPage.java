package ktrack.ui;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

@AuthorizeInstantiation("ROLE_USER")
public abstract class BaseAuthenticatedPage extends BasePage {

	public BaseAuthenticatedPage(final PageParameters pageParams) {
		super(pageParams);

	}

	/**
	 * To be overridden to augment the navbar.
	 * 
	 * @param navbar
	 *            The navbar.
	 */
	@Override
	protected void addNavbarComponents(final Navbar navbar) {
		navbar.addComponents(
				new ImmutableNavbarComponent(new LogoutForm(Navbar.componentId()), Navbar.ComponentPosition.RIGHT));
	};

}
