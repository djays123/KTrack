package ktrack.ui.panels;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;

public class CommentPanel extends Panel {
	public CommentPanel(String id) {
		super(id);
		add(new Icon("comment-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.comment).build()));
		
		add(new TextArea<String>("comments"));
	}


}
