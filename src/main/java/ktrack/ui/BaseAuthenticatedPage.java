package ktrack.ui;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;

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
		NavbarButton<HomePage> homeButton = new NavbarButton<>(DogsList.class, Model.of(getString("dogs-list")));
		homeButton.setVisible(true);

		NavbarButton<HomePage> addDogButton = new NavbarButton<>(WelcomePage.class, Model.of(getString("add-dog")));
		addDogButton.setVisible(true);

		navbar.addComponents(new ImmutableNavbarComponent(homeButton, Navbar.ComponentPosition.LEFT),
				new ImmutableNavbarComponent(addDogButton, Navbar.ComponentPosition.LEFT),
				new ImmutableNavbarComponent(new LogoutForm(Navbar.componentId()), Navbar.ComponentPosition.RIGHT));

	};

}
