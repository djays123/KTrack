package ktrack.ui.panels;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;

public class CaregiverPanel extends Panel {
	public CaregiverPanel(String id) {
		super(id);
		add(new TextField<String>("caregiver"));
		add(new TextField<String>("caregiverMobile"));
		add(new TextField<String>("caregiverEmail"));
		
		add(new Icon("caregiver-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.user).build()));
		add(new Icon("caregiverMobile-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.mobile).build()));
		add(new Icon("caregiverEmail-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.envelope).build()));

	
	}


}
