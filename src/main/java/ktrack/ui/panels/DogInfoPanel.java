package ktrack.ui.panels;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder.FontAwesomeGraphic;
import ktrack.entity.Dog;
import ktrack.repository.DogNamesRepository;

public class DogInfoPanel extends Panel {
	/** The dog name. */
	private TextField<String> dogName;
	
	@SpringBean
	private DogNamesRepository dogNamesRepository;

	
	public DogInfoPanel(String id) {
		super(id);
		add(new Icon("paw-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.paw).build()));
		add(new Icon("age-fa", FontAwesomeIconTypeBuilder.on(FontAwesomeGraphic.calendar_check_o).build()));

		add(new NumberTextField<Integer>("age", Integer.class).setMinimum(0).setMaximum(15).setStep(1));
		dogName = new TextField<String>("name");
		dogName.setOutputMarkupId(true);
		
		add(dogName);

	}
	
	/**
	 * Updates info on save.
	 */
	public void update(AjaxRequestTarget target) {
		Dog dog = (Dog)dogName.getForm().getModel().getObject();
		if (StringUtils.isEmpty(dog.getName())) {
			dog.setName(dogNamesRepository.getRandomName(dog.getSex()).getName());
			target.add(dogName);
		}
	}
	


}
