package ktrack.ui;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/welcome")
public class WelcomePage extends BaseAuthenticatedPage {
	/** The google maps API key. */
	private static String GOOGLE_MAPS_KEY = "AIzaSyCCBGibN4Tkk59VRZ2AtFnJdqTPK6PymNQ";

	/**
	 * 
	 * @param pageParams
	 */
	public WelcomePage(final PageParameters pageParams) {
		super(pageParams);

	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem
						.forReference(new JavaScriptResourceReference(getClass(), "js/bootstrap-imageupload.js")),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				CssHeaderItem.forReference(new CssResourceReference(getClass(),
						"css/bootstrap-imageupload.css")),
				"footer-container"));
		
		
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forUrl("http://maps.google.com/maps/api/js?sensor=false&libraries=places"),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				CssHeaderItem.forReference(new CssResourceReference(getClass(),
						"css/welcomepage.css")),
				"footer-container"));

		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem
						.forReference(new JavaScriptResourceReference(getClass(), "js/locationpicker.jquery.js")),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/welcomepage.js")),
				"footer-container"));

	}

}
