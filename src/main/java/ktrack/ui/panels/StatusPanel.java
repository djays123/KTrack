package ktrack.ui.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class StatusPanel extends FeedbackPanel {

	public StatusPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		get("feedbackul").add(new AttributeModifier("class", "list-group"));
	}
	
	@Override
	protected String getCSSClass(FeedbackMessage message) {
	    String listClass = "list-unstyled list-group-item small";
	    
		switch (message.getLevel()) {
		case FeedbackMessage.SUCCESS:
		    return listClass + " list-group-item-success";
		case FeedbackMessage.ERROR:
		    return listClass + " list-group-item-danger";
			
		}

		return listClass;
	}

}
