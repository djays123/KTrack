package ktrack.ui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import ktrack.repository.DogNamesRepository;

public class SnapshotResource extends DynamicImageResource {
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(SnapshotResource.class);

	/**
	 * The parameter that holds the image id.
	 */
	public static final String IMAGE_ID = "imageId";

	/**
	 * The parameter that holds the flag indicating whether the image is a
	 * thumbnail.
	 */
	public static final String IMAGE_THUMBNAIL = "image_thumbnail";

	/** The dogs names repository. */
	private DogNamesRepository dogNamesRepository;

	public SnapshotResource(DogNamesRepository dogNamesRepository) {
		this.dogNamesRepository = dogNamesRepository;
	}

	@Override
	protected byte[] getImageData(Attributes attributes) {
		String imageId = getImageId(attributes);

		if (StringUtils.isNotEmpty(imageId)) {
			byte[] imageData = dogNamesRepository.getImage(imageId);
			if (imageData != null) {
				if (isImageThumbnail(attributes)) {
					try {
						String format = null;
						BufferedImage sourceImage = null;
						ImageInputStream imageStream = ImageIO
								.createImageInputStream(new ByteArrayInputStream(imageData));
						Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
						if (readers.hasNext()) {
							ImageReader reader = readers.next();
							format = reader.getFormatName();
							reader.setInput(imageStream);
							sourceImage = reader.read(0);

							BufferedImage scaledImg = Scalr.resize(sourceImage, Method.SPEED, Scalr.Mode.AUTOMATIC, 100,
									50, Scalr.OP_ANTIALIAS);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(scaledImg, format, baos);
							return baos.toByteArray();
						}

						LOGGER.warn("Failed to resize image for image id: " + imageId);

					} catch (IOException exp) {
						LOGGER.error("Failed to resize image for image id: " + imageId, exp);
					}
				}
				return imageData;
			}
		}

		return new byte[0];
	}

	private String getImageId(Attributes attributes) {
		return attributes.getRequest().getQueryParameters().getParameterValue(IMAGE_ID).toString();
	}

	private boolean isImageThumbnail(Attributes attributes) {
		return attributes.getRequest().getQueryParameters().getParameterValue(IMAGE_THUMBNAIL).toBoolean(true);
	}

}
