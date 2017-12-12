package ktrack.ui;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import ktrack.ui.panels.BookingPreview;

@MountPath("/book")
public class BookSlot extends BaseAuthenticatedPage {
    /** The id of the div that holds the booking calendar. */
    private static final String CALENDAR_DIV = "slot-booking-calendar";

	/**
	 * The serial version id.
	 */
	private static final long serialVersionUID = 3599765021503968315L;

	public BookSlot(final PageParameters pageParams) {
		super(pageParams);	
		add(new BookingPreview<>("booking-preview"));
		
	}
	
	   @Override
	    public void renderHead(IHeaderResponse response) {
	        super.renderHead(response);
	        response.render(new FilteredHeaderItem(CssHeaderItem.forReference(FontAwesomeCssReference.instance()),
	                "footer-container"));
	        response.render(new FilteredHeaderItem(
	                JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getPage().getClass(),
	                        "js/bookslot.js", getLocale(), getStyle(), getVariation())),
	                "footer-container"));
	        response.render(new FilteredHeaderItem(
	                JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getPage().getClass(),
	                        "js/zabuto/zabuto_calendar.js", getLocale(), getStyle(), getVariation())),
	                "footer-container"));
	       

	        response.render(new FilteredHeaderItem(
	                CssHeaderItem.forReference(
	                        new CssResourceReference(getPage().getClass(), "css/zabuto/zabuto_calendar.min.css")),
	                "footer-container"));
	    }


	
}
