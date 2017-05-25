package ktrack.ui;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import ktrack.entity.Dog;
import ktrack.repository.DogRepository;

@MountPath("/dogs")
public class DogsList extends BaseAuthenticatedPage {

	@SpringBean
	private DogRepository dogRepository;

	/**
	 * The default constructor.
	 * 
	 * @param pageParams
	 */
	public DogsList(PageParameters pageParams) {
		super(pageParams);
		DogsDataProvider dogsDataProvider = new DogsDataProvider(dogRepository);
		DataView<Dog> dogDataView = new DataView<Dog>("dogsData", dogsDataProvider) {

			@Override
			protected void populateItem(Item<Dog> item) {

				final Dog dog = item.getModelObject();
				item.add(new Label("id", dog.getId()));
			}
		};
		dogDataView.setItemsPerPage(2);
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dogsDataContainer");
		dataContainer.setOutputMarkupId(true);
		add(dataContainer);
		dataContainer.add(dogDataView);
		
		
	
		dataContainer.add(new BootstrapAjaxPagingNavigator("pager", dogDataView));
	}

}
