package ktrack.ui.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * A panel that renders the Save button for the form.
 * 
 * @author dsharma
 *
 */
public class SaveButtonPanel extends Panel {
	/**
	 * The possible texts for the save button rendered in the form
	 * 
	 */
	public enum SaveText {
		SAVE {

			@Override
			public String getTextKey() {
				return "save";
			}

		},
		SEARCH {

			@Override
			public String getTextKey() {
				return "search";
			}

		};

		public abstract String getTextKey();
	};

	/**
	 * The constructor.
	 * 
	 * @param id
	 */
	public SaveButtonPanel(String id, SaveText saveText) {
		super(id);
		add(new Label("saveText", Model.of(getString(saveText.getTextKey()))));

	}

}
