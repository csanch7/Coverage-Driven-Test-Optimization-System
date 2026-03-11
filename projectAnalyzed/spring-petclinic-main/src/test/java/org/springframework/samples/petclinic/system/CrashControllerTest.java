package org.springframework.samples.petclinic.system;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CrashControllerTest {

	@Test
	void crashEndpointThrowsException() {
		CrashController controller = new CrashController();
		assertThrows(RuntimeException.class, () -> controller.triggerException());
	}

}
