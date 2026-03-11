package org.springframework.samples.petclinic.vet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VetsTest {

	@Test
	void getVetListInitializesAndRetainsList() {
		Vets vets = new Vets();
		Vet vet = new Vet();
		vet.setFirstName("Alex");

		vets.getVetList().add(vet);

		assertEquals(1, vets.getVetList().size());
		assertEquals("Alex", vets.getVetList().get(0).getFirstName());
	}

}
