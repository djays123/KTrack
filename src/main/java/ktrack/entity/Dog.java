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
package ktrack.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dogs")
public class Dog implements Serializable {
	
	/** The id. */
	@Id
	private String id;

	/**
	 * The default serial version id.
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String comments;

	private Sex sex = Sex.F;

	private Sterilized sterilized = Sterilized.NOT_STERLIZED;

	private Behavior behavior = Behavior.FRIENDLY;

	private List<String> imageIds = new LinkedList<String>();

	private Double latitude;

	private Double longitude;
	
	private String userId;
	
	private Integer age;
	
	private String location;
	
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
        return id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Sterilized getSterilized() {
		return sterilized;
	}

	public void setSterilized(Sterilized sterilized) {
		this.sterilized = sterilized;
	}

	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}

	public List<String> getImageIds() {
		return imageIds;
	}

	public void setImageIds(List<String> imageIds) {
		this.imageIds = imageIds;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
