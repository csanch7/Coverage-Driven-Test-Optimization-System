package org.springframework.samples.petclinic.vet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SpecialtyTest {

	@Test
	void nameCanBeSetAndRead() {
		Specialty specialty = new Specialty();
		specialty.setName("Dentistry");
		assertEquals("Dentistry", specialty.getName());
	}

}
