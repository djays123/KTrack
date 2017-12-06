package ktrack.ui.panels;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.HTML5Attributes;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;

public class CaregiverPanel extends Panel {
	/** The pattern for a phone number. */
	private static final String MOBILE_PATTERN = "(7|8|9)\\d{9}";
	
	public CaregiverPanel(String id, boolean required) {
		super(id);
		add(new TextField<String>("caregiver").setRequired(required).add(new HTML5Attributes()));
		TextField<String> mobile = new TextField<String>("caregiverMobile") {
			
			@Override
			protected String[] getInputTypes() {
				return new String[] {"tel"};
			}
		};
		mobile.setRequired(required).add(new HTML5Attributes());
		mobile.add(new PatternValidator(MOBILE_PATTERN));
		add(mobile);
		EmailTextField email = (EmailTextField)new EmailTextField("caregiverEmail").setRequired(required).add(new HTML5Attributes());
		email.add(EmailAddressValidator.getInstance());
		add(email);

		add(new Icon("caregiver-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.user).build()));
		add(new Icon("caregiverMobile-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.mobile).build()));
		add(new Icon("caregiverEmail-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.envelope).build()));

	}

}
