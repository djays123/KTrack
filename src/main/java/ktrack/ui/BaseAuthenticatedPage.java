package ktrack.ui;


import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarForm;

@AuthorizeInstantiation("ROLE_USER")
public abstract class BaseAuthenticatedPage extends BasePage {

	public BaseAuthenticatedPage(final PageParameters pageParams) {
		super(pageParams);

	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if (getPageParameters().get("logout").toBoolean(Boolean.FALSE)) {
			getSession().invalidate();
			((AuthenticatedWebApplication) AuthenticatedWebApplication.get()).onUnauthorizedInstantiation(this);
		}
	}

	/**
	 * To be overidden to augment the navbar.
	 * 
	 * @param navbar
	 *            The navbar.
	 */
	@Override
	protected void addNavbarComponents(final Navbar navbar) {
		if (AbstractAuthenticatedWebSession.get().isSignedIn()) {
			NavbarForm<?> logoutForm = new LogoutForm(Navbar.componentId());
			logoutForm.add(new AttributeModifier("action", "/logout"));
		    Button button1 = new Button(Navbar.componentId(), Model.of(getString("logout"))) {
		        public void onSubmit() {
		            info("button1.onSubmit executed");
		        }
		    };
		    button1.add(new AttributeModifier("value", getString("logout")));
		    button1.add(new AttributeModifier("type", "submit"));
			//logoutForm.add(button1);
			PageParameters pageParameters = new PageParameters();
			pageParameters.add("logout", Boolean.TRUE);
			
			NavbarButton<HomePage> logoutButton = new NavbarButton(getClass(), pageParameters,
					Model.of(getString("logout"))) {
				protected String getOnClickScript() {
					return "";
				}
			};
			
			NavbarAjaxButton navbarAjaxButton = new NavbarAjaxButton(Model.of(getString("logout")), Type.Link) {
				
			};
			
			
			NavbarAjaxLink<?> navbarAjaxlnk = new NavbarAjaxLink(Model.of(getString("logout"))) {

				@Override
				public void onClick(AjaxRequestTarget target) {
					//target.

				}

			};
			logoutForm.add(navbarAjaxButton);
			navbarAjaxlnk.setIconType(GlyphIconType.logout);
			LogoutForm f = new LogoutForm(Navbar.componentId());
		
			navbar.addComponents(new ImmutableNavbarComponent(new LogoutForm(Navbar.componentId()), Navbar.ComponentPosition.RIGHT));
			//navbar.addComponents(new ImmutableNavbarComponent(navbarAjaxButton, Navbar.ComponentPosition.RIGHT));

		}
	};

}
