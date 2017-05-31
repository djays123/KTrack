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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import ktrack.repository.DogNamesRepository;

public abstract class SnapshotResource extends DynamicImageResource {
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(SnapshotResource.class);

	@Override
	protected byte[] getImageData(Attributes attributes) {
		String imageId = getImageId(attributes);

		if (StringUtils.isNotEmpty(imageId)) {
			byte[] imageData = getDogNamesRepository().getImage(imageId);
			if (imageData != null) {
				if (!isThumbnail()) {
					return imageData;
				} else {
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
						return imageData;

					} catch (IOException exp) {
						LOGGER.error("Failed to resize image for image id: " + imageId, exp);
					}
				}
			}

		}

		return new byte[0];
	}

	protected abstract DogNamesRepository getDogNamesRepository();

	protected abstract boolean isThumbnail();

	protected String getImageId(Attributes attributes) {
		PageParameters parameters = attributes.getParameters();
		return parameters.get("imageId").toString();

	}

}
