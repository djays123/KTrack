package ktrack.ui;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

public class BookSlot extends BasePage<Void> {

	/**
	 * The serial version id.
	 */
	private static final long serialVersionUID = 3599765021503968315L;

	public BookSlot(final PageParameters pageParams) {
		super(pageParams);	
		
	}
	
	protected void addNavbarComponents(final Navbar navbar) {
		navbar.setBrandName(Model.of(getString("navbar-brand")));
	};

	
}
