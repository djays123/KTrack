package ktrack.ui;

import org.apache.wicket.AttributeModifier;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarForm;

public class LogoutForm extends NavbarForm<Object> {

	public LogoutForm(String componentId) {
		super(componentId);		
		add(new AttributeModifier("action", "/logout"));
	}

}
