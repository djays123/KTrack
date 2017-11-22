package ktrack.ui.panels;

import org.apache.wicket.markup.html.panel.Panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;

/**
 * A panel that renders the date attributes of a dog to a form.
 * 
 * @author dsharma
 *
 */
public class DatePanel extends Panel {
	public DatePanel(String id) {
		super(id);

		String dateFormat = getString("dateFormat");
		add(
				new Icon("arrival-calendar-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.calendar).build()));
		add(
				new Icon("surgery-calendar-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.calendar).build()));
		add(
				new Icon("release-calendar-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.calendar).build()));
		add(
				new DateTextField("arrivalDate", new DateTextFieldConfig().autoClose(true).withFormat(dateFormat)));
		add(
				new DateTextField("surgeryDate", new DateTextFieldConfig().autoClose(true).withFormat(dateFormat)));
		add(
				new DateTextField("releaseDate", new DateTextFieldConfig().autoClose(true).withFormat(dateFormat)));

	}
}
