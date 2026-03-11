package org.springframework.samples.petclinic.vet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VetTest {

	@Test
	void addSpecialtyUpdatesCountAndSortedList() {
		Vet vet = new Vet();
		Specialty surgery = new Specialty();
		surgery.setName("Surgery");
		Specialty anesthesia = new Specialty();
		anesthesia.setName("Anesthesia");

		vet.addSpecialty(surgery);
		vet.addSpecialty(anesthesia);

		assertEquals(2, vet.getNrOfSpecialties());
		assertEquals("Anesthesia", vet.getSpecialties().get(0).getName());
		assertEquals("Surgery", vet.getSpecialties().get(1).getName());
	}

}
