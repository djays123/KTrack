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

import java.io.InputStream;

import ktrack.entity.DogName;
import ktrack.entity.Sex;

public interface DogNamesRepositoryCustom {

	/**
	 * Gets a random dog name for the specified sex.
	 * 
	 * @param sex
	 *            The sex.
	 */
	DogName getRandomName(Sex sex);

	/**
	 * Saves an image in the repository
	 * @param inputStream The input stream of the image file.
	 * @param fileName The name of the image file.
	 * @param contentType The content type.
	 * @return The id of the saved image.
	 */
	String saveImage(InputStream inputStream, String fileName, String contentType);
	
	/**
	 * Removes the image file specified by the id.
	 * @param imageFileId The image file id.
	 */
	public void removeImage(String imageFileId);
	
	/**
	 * Returns the image as bytes from the image file specified by the id.
	 * @param imageFileId The image file id.
	 * 
	 * @return The image data as bytes. Null if an error occurs or if the file corresponding to the id does not exist.
	 */
	public byte[] getImage(String imageFileId);

}
