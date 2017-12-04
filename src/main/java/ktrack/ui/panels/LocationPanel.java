package ktrack.ui.panels;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;
import ktrack.entity.Dog;

public class LocationPanel extends Panel {
	/** The initial latitude. */
	private static final Double LATITUDE = 18.52895184;

	/** The initial longitude. */
	private static final Double LONGITUDE = 73.87434160;
	
	/** The google maps API key. */
	private static String GOOGLE_MAPS_KEY = "AIzaSyCCBGibN4Tkk59VRZ2AtFnJdqTPK6PymNQ";

	
	
	public LocationPanel(String id, Dog dog) {
		super(id);
		
		if(dog != null) {
			dog.setLatitude(LATITUDE);
			dog.setLongitude(LONGITUDE);
		}
		add(new RequiredTextField<String>("location"));
		add(new HiddenField<Double>("latitude", Double.class));
		add(new HiddenField<Double>("longitude", Double.class));
		add(new Icon("location-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.location_arrow).build()));


	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
	
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem
						.forUrl("http://maps.google.com/maps/api/js?key=" + GOOGLE_MAPS_KEY + "&libraries=places"),
				"footer-container"));
	
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem
						.forReference(new JavaScriptResourceReference(getClass(), "js/locationpicker.jquery.js")),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/locationpanel.js")),
				"footer-container"));

	}

	
	

}
