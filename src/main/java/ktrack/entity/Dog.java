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

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dogs")
public class Dog implements Serializable {

	/**
	 * The default serial version id.
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String comments;
	
	private Sex sex = Sex.F;
	
	private Sterilized sterilized = Sterilized.NOT_STERLIZED;
	
	private Behavior behavior = Behavior.FRIENDLY;
}
