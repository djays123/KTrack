package ktrack.ui;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/welcome")
public class WelcomePage extends BaseAuthenticatedPage {

	public WelcomePage(final PageParameters pageParams) {
		super(pageParams);
	}

}
