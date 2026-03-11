package org.springframework.samples.petclinic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;

class PetClinicRuntimeHintsTest {

	@Test
	void registerHintsExecutesWithoutError() {
		PetClinicRuntimeHints hintsRegistrar = new PetClinicRuntimeHints();
		RuntimeHints hints = new RuntimeHints();
		assertDoesNotThrow(() -> hintsRegistrar.registerHints(hints, getClass().getClassLoader()));
	}

}
