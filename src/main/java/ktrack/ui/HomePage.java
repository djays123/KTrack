package ktrack.ui;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

@WicketHomePage
@WicketSignInPage
public class HomePage extends BasePage {

	/**
	 * The serial version id.
	 */
	private static final long serialVersionUID = 3599765021503968315L;

	public HomePage(final PageParameters pageParams) {
		super(pageParams);
		Label loginError = new Label("login-error", getString("login-failed"));
		loginError.setVisible(!pageParams.get("error").isNull());
		add(loginError);	
		
	}
	
	protected void addNavbarComponents(final Navbar navbar) {
		navbar.setBrandName(Model.of(getString("navbar-brand")));
	};

	
}