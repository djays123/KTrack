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
   
   /usr/bin/mongod --quiet --config /etc/mongod.conf
    * use KTrack
db.createUser( { user: "admin",
                 pwd: "crypt*2461",
                 customData: { },
                 roles: [ { role: "clusterAdmin", db: "admin" },
                          { role: "readAnyDatabase", db: "admin" },
                          "readWrite"] })
                          
                          sudo /usr/bin/mongo localhost:27017/KTrack --quiet populatedb.js
                          mongo localhost:27017/KTrack  D:/Installables/eclipseworkspace/ktrack/src/main/resources/populatedb.js
 */
package ktrack.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ktrack.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	User findByName(@Param("name") String name);

}