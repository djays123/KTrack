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
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
@Entity
@Document(collection = "dogs")
@CompoundIndexes({
    @CompoundIndex(name = "arrivalDate_kennel", def = "{'arrivalDate' : 1, 'kennel': 1}", unique = true, dropDups=true)
})
public class Dog implements Serializable {
	
	/** Indicates an unknown dog id. */
	public static final String UNKOWN_ID = "-1";
	
	/** The id. */
	@Id
	private String id;

	/**
	 * The default serial version id.
	 */
	private static final long serialVersionUID = 1L;

	@TextIndexed(weight = 8)
	private String name;

	@TextIndexed(weight = 9)
	private String comments;

	private Sex sex = Sex.F;

	private Sterilized sterilized = Sterilized.NOT_STERLIZED;

	private Behavior behavior = Behavior.FRIENDLY;

	private Collection<String> imageIds = new LinkedList<String>();

	private Double latitude;

	private Double longitude;
	
	private String userId;
	
	private Integer age;
	
	private Integer kennel;
	
	@TextIndexed(weight = 10)
	private String location;
	
	private String vetrinarian;
	
	private Date arrivalDate;
	
	private Date surgeryDate;
	
	private Date releaseDate;
	
	@Indexed
	private String caregiver;
	
	@Indexed
	private String caregiverMobile;
	
	@Indexed
	private String caregiverEmail;
	
	
	public String getCaregiverEmail() {
		return caregiverEmail;
	}

	public void setCaregiverEmail(String caregiverEmail) {
		this.caregiverEmail = caregiverEmail;
	}

	public String getCaregiver() {
		return caregiver;
	}

	public void setCaregiver(String caregiver) {
		this.caregiver = caregiver;
	}

	public String getCaregiverMobile() {
		return caregiverMobile;
	}

	public void setCaregiverMobile(String caregiverMobile) {
		this.caregiverMobile = caregiverMobile;
	}

	public Date getSurgeryDate() {
		return surgeryDate;
	}

	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getVetrinarian() {
		return vetrinarian;
	}

	public void setVetrinarian(String vetrinarian) {
		this.vetrinarian = vetrinarian;
	}

	public Integer getKennel() {
		return kennel;
	}

	public void setKennel(Integer kennel) {
		this.kennel = kennel;
	}

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

	public Collection<String> getImageIds() {
		return imageIds;
	}

	public void setImageIds(Collection<String> imageIds) {
		this.imageIds = imageIds;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
