package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class VisitControllerTest {

	@Mock
	private OwnerRepository owners;

	private VisitController controller;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		controller = new VisitController(owners);
	}

	@Test
	void loadPetWithVisitAddsModelAttributesAndReturnsVisit() {
		Owner owner = new Owner();
		owner.setId(1);
		Pet pet = new Pet();
		pet.setId(2);
		owner.getPets().add(pet);
		when(owners.findById(1)).thenReturn(Optional.of(owner));

		Map<String, Object> model = new HashMap<>();
		Visit visit = controller.loadPetWithVisit(1, 2, model);

		assertNotNull(visit);
		assertEquals(owner, model.get("owner"));
		assertEquals(pet, model.get("pet"));
		assertEquals(1, pet.getVisits().size());
	}

	@Test
	void loadPetWithVisitThrowsWhenOwnerMissing() {
		when(owners.findById(1)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> controller.loadPetWithVisit(1, 2, new HashMap<>()));
	}

	@Test
	void loadPetWithVisitThrowsWhenPetMissing() {
		Owner owner = new Owner();
		when(owners.findById(1)).thenReturn(Optional.of(owner));
		assertThrows(IllegalArgumentException.class, () -> controller.loadPetWithVisit(1, 2, new HashMap<>()));
	}

	@Test
	void initNewVisitFormReturnsView() {
		assertEquals("pets/createOrUpdateVisitForm", controller.initNewVisitForm());
	}

	@Test
	void processNewVisitFormReturnsFormOnValidationErrors() {
		Owner owner = new Owner();
		Visit visit = new Visit();
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(visit, "visit");
		result.rejectValue("description", "required", "required");

		String view = controller.processNewVisitForm(owner, 2, visit, result, new RedirectAttributesModelMap());

		assertEquals("pets/createOrUpdateVisitForm", view);
	}

	@Test
	void processNewVisitFormSavesAndRedirects() {
		Owner owner = new Owner();
		owner.setId(1);
		Pet pet = new Pet();
		pet.setId(2);
		owner.getPets().add(pet);
		Visit visit = new Visit();
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(visit, "visit");
		RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

		String view = controller.processNewVisitForm(owner, 2, visit, result, redirect);

		verify(owners).save(owner);
		assertEquals("redirect:/owners/{ownerId}", view);
		assertEquals("Your visit has been booked", redirect.getFlashAttributes().get("message"));
	}

}
