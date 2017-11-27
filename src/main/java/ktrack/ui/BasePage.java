package ktrack.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.MetaDataHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.string.StringValue;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.IeEdgeMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.MobileViewportMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarText;
import de.agilecoders.wicket.core.markup.html.references.BootlintHeaderItem;
import de.agilecoders.wicket.core.markup.html.references.RespondJavaScriptReference;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ITheme;

public class BasePage<T> extends GenericWebPage<T> {

	/**
	 * Construct.
	 *
	 * @param parameters
	 *            current page parameters
	 */
	public BasePage(final PageParameters parameters) {
		super(parameters);

		add(new HtmlTag("html"));
		MobileViewportMetaTag mvt = new MobileViewportMetaTag("viewport");
		mvt.setWidth("device-width");
		mvt.setInitialScale("1");
		add(mvt);

		add(new IeEdgeMetaTag("ie-edge"));

		add(newNavbar("navbar"));
		add(new HeaderResponseContainer("footer-container", "footer-container"));

		Navbar footer = new Navbar("footer-navbar");
		footer.setPosition(Navbar.Position.BOTTOM);
		footer.addComponents(new NavbarText(footer.newExtraItemId(), getString("copyright"))
				.position(Navbar.ComponentPosition.LEFT));

		add(footer);

	}

	/**
	 * Returns true if the user is signed in.
	 * 
	 * @return true is the user is signed in.
	 */
	protected final boolean isSignedIn() {
		return AbstractAuthenticatedWebSession.get().isSignedIn();
	}

	/**
	 * creates a new {@link Navbar} instance
	 *
	 * @param markupId
	 *            The components markup id.
	 * @return a new {@link Navbar} instance
	 */
	protected Navbar newNavbar(String markupId) {
		Navbar navbar = new Navbar(markupId) {

			@Override
			protected Image newBrandImage(String markupId) {
				Image img = super.newBrandImage(markupId);
				img.add(AttributeModifier.replace("width", "80%"));
				img.add(AttributeModifier.replace("height", "80%"));

				return img;
			}

		};

		navbar.setPosition(Navbar.Position.TOP);
		navbar.setBrandName(Model.<String>of(getString("brandname")));
		navbar.setBrandImage(new PackageResourceReference(BasePage.class, "ccc.png"), Model.<String>of());

		addNavbarComponents(navbar);

		DropDownButton dropdown = new NavbarDropDownButton(Model.of(getString("themes"))) {
			@Override
			public boolean isActive(Component item) {
				return false;
			}

			@Override
			protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
				final List<AbstractLink> subMenu = new ArrayList<>();
				subMenu.add(new MenuHeader(Model.of(BasePage.this.getString("all-available-themes"))));
				subMenu.add(new MenuDivider());

				final IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
				final List<ITheme> themes = settings.getThemeProvider().available();

				for (final ITheme theme : themes) {
					PageParameters params = new PageParameters();
					params.set("theme", theme.name());

					subMenu.add(new MenuBookmarkablePageLink<Void>(getPageClass(), params, Model.of(theme.name())));
				}

				return subMenu;
			}
		}.setIconType(GlyphIconType.book);

		navbar.addComponents(new ImmutableNavbarComponent(dropdown, Navbar.ComponentPosition.RIGHT));

		return navbar;
	}

	/**
	 * To be overidden to augment the navbar.
	 * 
	 * @param navbar
	 */
	protected void addNavbarComponents(final Navbar navbar) {

	};

	/**
	 * sets the theme for the current user.
	 *
	 * @param pageParameters
	 *            current page parameters
	 */
	private void configureTheme(PageParameters pageParameters) {
		StringValue theme = pageParameters.get("theme");

		if (!theme.isEmpty()) {
			IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
			settings.getActiveThemeProvider().setActiveTheme(theme.toString(""));
		}
	}

	protected ITheme activeTheme() {
		IBootstrapSettings settings = Bootstrap.getSettings(getApplication());

		return settings.getActiveThemeProvider().getActiveTheme();
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();

		configureTheme(getPageParameters());
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(
				new FilteredHeaderItem(
						JavaScriptHeaderItem
								.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()),
						"footer-container"));
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(Bootstrap.getSettings().getJsResourceReference()),
				"footer-container"));
		response.render(RespondJavaScriptReference.headerItem());

		if ("google".equalsIgnoreCase(activeTheme().name())) {
			response.render(CssHeaderItem
					.forReference(new CssResourceReference(CssResourceReference.class, "google-docs.css")));
		}

		if (!getRequest().getRequestParameters().getParameterValue("bootlint").isNull()) {
			response.render(BootlintHeaderItem.INSTANCE);
		}

		ResourceReference faviconRef = new PackageResourceReference(BasePage.class, "favicon.png");
		response.render(MetaDataHeaderItem.forLinkTag("shortcut icon", urlFor(faviconRef, null).toString()));
	}

}
