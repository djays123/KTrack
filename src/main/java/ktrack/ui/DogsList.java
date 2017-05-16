package ktrack.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.InfiniteScrollingBehavior;
import de.agilecoders.wicket.core.util.CssClassNames.Grid;
import ktrack.entity.Dog;
import ktrack.repository.DogRepository;

@MountPath("/dogs")
public class DogsList extends BaseAuthenticatedPage {
	
	
	@SpringBean
	private DogRepository dogRepository;

	/**
	 * The default constructor.
	 * @param pageParams
	 */
	public DogsList(PageParameters pageParams) {
		super(pageParams);	
		List<IColumn<Dog>> columns = new
				ArrayList<IColumn<Dog>>();
		InfinitePaginationPanel
	}

}
