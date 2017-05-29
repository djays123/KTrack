package ktrack.ui;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import ktrack.entity.Dog;
import ktrack.repository.DogRepository;

public class DogsDataProvider extends SortableDataProvider<Dog, String> {
	/** The dog properties shown in the grid. */
	protected static final String[] DOG_PROPERTIES = { "name", "age", "sex", "sterilized", "behavior", "location",
			"comments" };


	/** The dogs repository. */
	private DogRepository dogRepository;
	
	/** The page parameters - these hold information like sorting and searching. */
	private PageParameters pageParameters;
	

	/**
	 * The constructor.
	 * 
	 * @param dogRepository
	 *            The dog repository.
	 */
	public DogsDataProvider(DogRepository dogRepository, PageParameters pageParameters) {
		this.dogRepository = dogRepository;
		this.pageParameters = pageParameters;
	}

	@Override
	public void detach() {

	}

	@Override
	public Iterator<? extends Dog> iterator(long first, long count) {
		String orderColumnIndexParam = pageParameters.get("order[0][column]").toString();
		if(StringUtils.isNotEmpty(orderColumnIndexParam)) {
			Integer orderColumnIndex = Integer.parseInt(orderColumnIndexParam);
			String columnData = pageParameters.get("columns[" + orderColumnIndex +"][data]").toString();
			String columnOrderDir = pageParameters.get("order[0][dir]").toString();
			Sort.Direction sortDirection = StringUtils.equalsIgnoreCase("asc", columnOrderDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
			return dogRepository.findAll(new Sort(sortDirection, columnData)).iterator();
		}
		return dogRepository.findAll(new PageRequest((int) first, (int) count)).iterator();
	}

	@Override
	public long size() {
		return dogRepository.count();
	}

	@Override
	public IModel<Dog> model(Dog object) {	
		return Model.<Dog>of(object);
	}


}
