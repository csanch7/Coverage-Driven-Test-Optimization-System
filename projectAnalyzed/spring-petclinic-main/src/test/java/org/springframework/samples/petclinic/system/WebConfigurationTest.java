package org.springframework.samples.petclinic.system;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

class WebConfigurationTest {

	@Test
	void localeResolverDefaultsToEnglish() {
		WebConfiguration configuration = new WebConfiguration();
		LocaleResolver resolver = configuration.localeResolver();
		assertInstanceOf(SessionLocaleResolver.class, resolver);
		assertEquals(Locale.ENGLISH, ((SessionLocaleResolver) resolver)
			.resolveLocale(new org.springframework.mock.web.MockHttpServletRequest()));
	}

	@Test
	void localeChangeInterceptorUsesLangParameter() {
		WebConfiguration configuration = new WebConfiguration();
		LocaleChangeInterceptor interceptor = configuration.localeChangeInterceptor();
		assertEquals("lang", interceptor.getParamName());
	}

	@Test
	void addInterceptorsRegistersWithoutError() {
		WebConfiguration configuration = new WebConfiguration();
		assertDoesNotThrow(() -> configuration.addInterceptors(new InterceptorRegistry()));
	}

}
