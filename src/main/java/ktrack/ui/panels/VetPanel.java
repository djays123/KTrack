package ktrack.ui.panels;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;

/**
 * Adds the inputs for vets info to a form.
 * @author dsharma
 *
 */
public class VetPanel extends Panel {

	public VetPanel(String id) {
		super(id);
		add(new Icon("doctor-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.stethoscope).build()));
		add(new TextField<String>("vetrinarian"));
	}

}
