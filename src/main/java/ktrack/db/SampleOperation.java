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
package ktrack.db;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SampleOperation implements AggregationOperation  {

    private int size;

    public SampleOperation(int size) {
        Assert.isTrue(size > 0, " Size must be positive!");
        this.size = size;
    }

    public AggregationOperation setSize(int size) {
        Assert.isTrue(size > 0, " Size must be positive!");
        this.size = size;
        return this;
    }

    @Override
    public DBObject toDBObject(AggregationOperationContext context) {
        return new BasicDBObject("$sample", context.getMappedObject(Criteria.where("size").is(size).getCriteriaObject()));
    }

}