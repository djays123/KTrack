/**
	Copyright 2017 Dhananjay Sharma (djays123@yahoo.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   */
package ktrack.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

import ktrack.db.SampleOperation;
import ktrack.entity.Dog;
import ktrack.entity.DogName;
import ktrack.entity.Sex;

public class DogNamesRepositoryImpl implements DogNamesRepositoryCustom {
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(DogNamesRepositoryImpl.class);

	/** The metadata field that indicates the id of the record. */
	private static final String DOG_ID = "dogId";
	
	/** Orphaned files will be deleted after this number of hours. */
	private static final int ORPHANED_FILES_LIFETIME_HOURS = 24;

	private final MongoTemplate mongoTemplate;

	@Autowired
	GridFsTemplate gridFsTemplate;

	@Autowired
	public DogNamesRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public DogName getRandomName(Sex sex) {
		return mongoTemplate.aggregate(newAggregation(match(Criteria.where("sex").is(sex)), new SampleOperation(1)),
				DogName.class, DogName.class).getUniqueMappedResult();
	}

	@Override
	public String saveImage(InputStream inputStream, String fileName, String contentType) {
		DBObject metaData = new BasicDBObject(DOG_ID, Dog.UNKOWN_ID);
		return this.gridFsTemplate.store(inputStream, fileName, contentType, metaData).getId().toString();

	}

	@Override
	public void associateImages(String dogId, Collection<String> imageIds) {
		for (final String imageFileId : imageIds) {
			GridFSDBFile imageFile = getGridFSDBFile(imageFileId);
			if (imageFile.getMetaData() == null) {
				imageFile.setMetaData(new BasicDBObject(DOG_ID, dogId));
			} else {
				imageFile.getMetaData().put(DOG_ID, dogId);
			}
			imageFile.save();
		}

	}

	@Override
	public void disAssociateImages(String dogId, Collection<String> imageIds) {
		for (final String imageFileId : imageIds) {
			GridFSDBFile imageFile = getGridFSDBFile(imageFileId);
			if (imageFile.getMetaData() != null) {
				imageFile.getMetaData().put(DOG_ID, Dog.UNKOWN_ID);
			}
			imageFile.save();
		}
	}

	@Override
	public void removeOrphanedImages() {
		
		Query getOrphanedFiles = new Query(Criteria.where("metadata." + DOG_ID).is(Dog.UNKOWN_ID));
		getOrphanedFiles.addCriteria(Criteria.where("uploadDate").lte(new DateTime().minusHours(ORPHANED_FILES_LIFETIME_HOURS).toDate()));
		LOGGER.info("Size of orphaned files: "
				+ gridFsTemplate.find(getOrphanedFiles).size());
		gridFsTemplate.delete(getOrphanedFiles);
	}

	@Override
	public byte[] getImage(String imageFileId) {
		try {
			GridFSDBFile imageFile = getGridFSDBFile(imageFileId);
			if (imageFile != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				imageFile.writeTo(baos);
				return baos.toByteArray();
			}
		} catch (IOException exception) {
			// TODO: Log error!
		}

		return null;
	}

	@Override
	public Object[] getImageNameAndLength(String imageFileId) {
		GridFSDBFile imageFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(imageFileId)));
		return new Object[] { imageFile.getFilename(), imageFile.getLength() };
	}

	/**
	 * Returns the GridFSDBFile associated with the id, null if none exists.
	 * 
	 * @param imageFileId
	 *            the id of the image file.
	 * 
	 * @return GridFSDBFile for the file, null if none exists.
	 */
	private GridFSDBFile getGridFSDBFile(String imageFileId) {
		return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(imageFileId)));
	}

}
