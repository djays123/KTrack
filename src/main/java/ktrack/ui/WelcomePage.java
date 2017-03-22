package ktrack.ui;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.lang.Classes;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BooleanRadioChoiceRenderer;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BooleanRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.radio.BootstrapRadioGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;

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
		add(new Icon("paw-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.paw).build()));
		add(new Icon("location-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.location_arrow).build()));
		BooleanRadioGroup sex = new BooleanRadioGroup("sex", new Model<Boolean>(Boolean.FALSE));
		sex.setChoiceRenderer(new BooleanRadioChoiceRenderer(Type.Primary, this) {
			protected String resourceKey(Boolean choice) {
				return choice ?  "male" : "female";
			}
		});
		add(sex);
		
		BooleanRadioGroup sterlized = new BooleanRadioGroup("sterilized", new Model<Boolean>(Boolean.FALSE));
		sterlized.setChoiceRenderer(new BooleanRadioChoiceRenderer(Type.Primary, this) {
			protected String resourceKey(Boolean choice) {
				return choice ?  "sterilized" : "not-sterilized";
			}
		});
		add(sterlized);
		
		BooleanRadioGroup behavior = new BooleanRadioGroup("behavior", new Model<Boolean>(Boolean.FALSE));
		behavior.setChoiceRenderer(new BooleanRadioChoiceRenderer(Type.Primary, this) {
			protected String resourceKey(Boolean choice) {
				return choice ?  "friendly" : "not-friendly";
			}
		});
		add(behavior);

	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(
				new JavaScriptResourceReference(getClass(), "js/dropzone.js", getLocale(), getStyle(), getVariation())),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				CssHeaderItem.forReference(new CssResourceReference(getClass(), "css/dropzone.css")),
				"footer-container"));
		response.render(new FilteredHeaderItem(CssHeaderItem.forReference(FontAwesomeCssReference.instance()),
				"footer-container"));

		response.render(new FilteredHeaderItem(
				JavaScriptHeaderItem.forUrl("http://maps.google.com/maps/api/js?sensor=false&libraries=places"),
				"footer-container"));
		response.render(new FilteredHeaderItem(
				CssHeaderItem.forReference(new CssResourceReference(getClass(), "css/welcomepage.css")),
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
