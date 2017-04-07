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

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import ktrack.db.SampleOperation;
import ktrack.entity.DogName;
import ktrack.entity.Sex;

public class DogNamesRepositoryImpl implements DogNamesRepositoryCustom {

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

	public String saveImage(InputStream inputStream, String fileName, String contentType) {
		DBObject metaData = new BasicDBObject();
		return this.gridFsTemplate.store(inputStream, fileName, contentType, metaData).getId().toString();

	}

}
