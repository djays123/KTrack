package ktrack.task;

import org.apache.log4j.Logger;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import ktrack.repository.DogNamesRepository;

/**
 * Task that is scheduled to run every hour to remove image files that are no
 * longer associated with any dog records.
 * 
 * @author dsharma
 *
 */
@Configuration
public class OrphanedImagesRemoverTask {
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(OrphanedImagesRemoverTask.class);
	
	@SpringBean
	@Autowired
	private DogNamesRepository dogNamesRepository;

	
	@Scheduled(fixedRate = 60*60*1000) // runs every hour
	public void removeOrphanedImages() {
		LOGGER.info("Removing orphaned files.");
		dogNamesRepository.removeOrphanedImages();
	}

}
