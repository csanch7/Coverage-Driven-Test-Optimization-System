package org.springframework.samples.petclinic.system;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import javax.cache.CacheManager;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CacheConfigurationTest {

	@Test
	void customizerCreatesVetsCacheWithConfiguration() {
		CacheConfiguration configuration = new CacheConfiguration();
		CacheManager cacheManager = org.mockito.Mockito.mock(CacheManager.class);

		configuration.petclinicCacheConfigurationCustomizer().customize(cacheManager);

		ArgumentCaptor<javax.cache.configuration.Configuration> captor = ArgumentCaptor
			.forClass(javax.cache.configuration.Configuration.class);
		verify(cacheManager).createCache(org.mockito.Mockito.eq("vets"), captor.capture());
		assertNotNull(captor.getValue());
	}

	@Test
	void customizerInvokesCreateCache() {
		CacheConfiguration configuration = new CacheConfiguration();
		CacheManager cacheManager = org.mockito.Mockito.mock(CacheManager.class);

		configuration.petclinicCacheConfigurationCustomizer().customize(cacheManager);

		verify(cacheManager).createCache(org.mockito.Mockito.eq("vets"), any());
	}

}
