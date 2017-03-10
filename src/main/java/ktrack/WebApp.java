package ktrack;

import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.CachingResourceVersion;
import org.apache.wicket.serialize.java.DeflatedJavaSerializer;
import org.apache.wicket.settings.RequestCycleSettings;
import org.springframework.stereotype.Component;

import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;
import com.google.javascript.jscomp.CompilationLevel;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.core.request.resource.caching.version.Adler32ResourceVersion;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.CookieThemeProvider;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.extensions.javascript.GoogleClosureJavaScriptCompressor;
import de.agilecoders.wicket.extensions.javascript.YuiCssCompressor;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;

@Component
public class WebApp extends WicketBootSecuredWebApplication {

	@Override
	protected void init() {
		super.init();

		configureBootstrap();
		optimizeForWebPerformance();
		
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

	/**
	 * optimize wicket for a better web performance
	 */
	private void optimizeForWebPerformance() {
		if (usesDeploymentConfig()) {
			getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy("-v-",
					new CachingResourceVersion(new Adler32ResourceVersion())));

			getResourceSettings().setJavaScriptCompressor(
					new GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
			getResourceSettings().setCssCompressor(new YuiCssCompressor());

			getFrameworkSettings().setSerializer(new DeflatedJavaSerializer(getApplicationKey()));
		} else {
			getResourceSettings().setCachingStrategy(new NoOpResourceCachingStrategy());
		}

		setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator());
		getRequestCycleSettings().setRenderStrategy(RequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);
	}

}
