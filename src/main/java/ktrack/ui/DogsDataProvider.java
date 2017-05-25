package ktrack.ui;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.data.domain.PageRequest;

import ktrack.entity.Dog;
import ktrack.repository.DogRepository;

public class DogsDataProvider implements IDataProvider<Dog> {

	private DogRepository dogRepository;

	/**
	 * The constructor.
	 * 
	 * @param dogRepository
	 *            The dog repository.
	 */
	public DogsDataProvider(DogRepository dogRepository) {
		this.dogRepository = dogRepository;
	}

	@Override
	public void detach() {

	}

	@Override
	public Iterator<? extends Dog> iterator(long first, long count) {
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
