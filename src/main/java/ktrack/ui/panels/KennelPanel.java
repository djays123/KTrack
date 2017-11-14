package ktrack.ui.panels;

import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.panel.Panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;

/**
 * Adds kennel information to a form.
 * @author dsharma
 *
 */
public class KennelPanel extends Panel {
	public KennelPanel(String id) {
		super(id);
		add(new Icon("home-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.home).build()));

		add(new NumberTextField<Integer>("kennel", Integer.class).setMinimum(1).setMaximum(999).setStep(1));

	}

}
