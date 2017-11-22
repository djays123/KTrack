package ktrack;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.CachingResourceVersion;
import org.apache.wicket.resource.NoOpTextCompressor;
import org.apache.wicket.serialize.java.DeflatedJavaSerializer;
import org.apache.wicket.settings.RequestCycleSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.core.request.resource.caching.version.Adler32ResourceVersion;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.CookieThemeProvider;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.extensions.javascript.YuiCssCompressor;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;
import ktrack.repository.DogNamesRepository;
import ktrack.repository.DogRepository;
import ktrack.ui.SnapshotResource;

@Component
public class WebApp extends WicketBootSecuredWebApplication {
	/** The mount path of the snapshot resource. */
	public static final String IMAGE_RES_REF_PATH = "/dogs/snapshot";
	
	/**
	 * The snapshot resource.
	 */
	private static SnapshotResource snapshotResource;
	
	/**
	 * The snapshot resource reference.
	 */
	private static final ResourceReference SNAPSHOT_RES_REF = new ResourceReference("dogsSnapshots") {

		@Override
		public IResource getResource() {
			return snapshotResource;
		}
	};
	
	
	@Autowired
	private DogNamesRepository dogNamesRepository;
	
	@Autowired
	private DogRepository dogRepository;


	@Override
	protected void init() {
		super.init();

		configureBootstrap();
		optimizeForWebPerformance();

		getDebugSettings().setAjaxDebugModeEnabled(true);
		
		snapshotResource = new SnapshotResource(dogNamesRepository);
		
		mountResource(IMAGE_RES_REF_PATH, SNAPSHOT_RES_REF);

	}



	/**
	 * configures wicket-bootstrap and installs the settings.
	 */
	private void configureBootstrap() {
		final IBootstrapSettings settings = new BootstrapSettings();
		Bootstrap.install(this, settings);
		final ThemeProvider themeProvider = new BootswatchThemeProvider(BootswatchTheme.Flatly);

		settings.setJsResourceFilterName("footer-container").setThemeProvider(themeProvider)
				.setActiveThemeProvider(new CookieThemeProvider());

		BootstrapLess.install(this);
	}

	public SnapshotResource getSnapshotResource() {
		return snapshotResource;
	}



	public ResourceReference getSnapshotResourceReference() {
		return SNAPSHOT_RES_REF;
	}



	/**
	 * optimize wicket for a better web performance
	 */
	private void optimizeForWebPerformance() {
		if (usesDeploymentConfig()) {
			getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy("-v-",
					new CachingResourceVersion(new Adler32ResourceVersion())));
			getResourceSettings().setJavaScriptCompressor(new NoOpTextCompressor());
			// getResourceSettings().setJavaScriptCompressor(
			// new
			// GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
			getResourceSettings().setCssCompressor(new YuiCssCompressor());

			getFrameworkSettings().setSerializer(new DeflatedJavaSerializer(getApplicationKey()));
		} else {
			getResourceSettings().setCachingStrategy(new NoOpResourceCachingStrategy());
		}

		setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator());
		getRequestCycleSettings().setRenderStrategy(RequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);
	}
	
	public String getLoggedInUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public DogRepository getDogRepository() {
		return dogRepository;
	}

}
