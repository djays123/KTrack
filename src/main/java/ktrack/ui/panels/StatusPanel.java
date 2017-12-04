package ktrack.ui.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class StatusPanel extends FeedbackPanel {

	public StatusPanel(String id) {
		super(id);
		setOutputMarkupId(true);
	}
	
	@Override
	protected String getCSSClass(FeedbackMessage message) {
		switch (message.getLevel()) {
		case FeedbackMessage.SUCCESS:
			return "active list-unstyled list-group-item  small";
		}

		return super.getCSSClass(message);
	}

}
