package org.springframework.samples.petclinic.system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class WelcomeControllerTest {

	@Test
	void welcomeReturnsWelcomeView() {
		WelcomeController controller = new WelcomeController();
		assertEquals("welcome", controller.welcome());
	}

}
