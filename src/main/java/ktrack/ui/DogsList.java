package ktrack.ui;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ktrack.repository.DogNamesRepository;
import ktrack.repository.DogRepository;
import ktrack.ui.panels.DogListPanel;

@MountPath("/dogs")
public class DogsList extends BaseAuthenticatedPage {

	@SpringBean
	private DogRepository dogRepository;

	@SpringBean
	private DogNamesRepository dogNamesRepository;

	/**
	 * The default constructor.
	 * 
	 * @param pageParams
	 */
	public DogsList(PageParameters pageParams) {
		super(pageParams);
		add(new DogListPanel("dog-list-panel", null).setRenderBodyOnly(true));

	}


}
