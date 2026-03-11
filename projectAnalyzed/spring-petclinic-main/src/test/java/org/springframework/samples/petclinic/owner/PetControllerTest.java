package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class PetControllerTest {

	@Mock
	private OwnerRepository owners;

	@Mock
	private PetTypeRepository types;

	private PetController controller;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		controller = new PetController(owners, types);
	}

	@Test
	void populatePetTypesReturnsRepositoryValues() {
		PetType type = new PetType();
		type.setName("dog");
		when(types.findPetTypes()).thenReturn(List.of(type));

		assertEquals(1, controller.populatePetTypes().size());
	}

	@Test
	void findOwnerReturnsOwnerWhenPresent() {
		Owner owner = new Owner();
		owner.setId(3);
		when(owners.findById(3)).thenReturn(Optional.of(owner));

		assertEquals(owner, controller.findOwner(3));
	}

	@Test
	void findOwnerThrowsWhenMissing() {
		when(owners.findById(3)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> controller.findOwner(3));
	}

	@Test
	void findPetReturnsNewPetWhenPetIdMissing() {
		Pet pet = controller.findPet(1, null);
		assertNotNull(pet);
		assertEquals(null, pet.getId());
	}

	@Test
	void findPetReturnsExistingPetWhenFound() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(11);
		pet.setName("Milo");
		owner.getPets().add(pet);
		when(owners.findById(1)).thenReturn(Optional.of(owner));

		assertEquals(pet, controller.findPet(1, 11));
	}

	@Test
	void initCreationFormAddsPetAndReturnsView() {
		Owner owner = new Owner();
		String view = controller.initCreationForm(owner, new org.springframework.ui.ModelMap());
		assertEquals("pets/createOrUpdatePetForm", view);
		assertEquals(1, owner.getPets().size());
	}

	@Test
	void processCreationFormRejectsDuplicateName() {
		Owner owner = new Owner();
		Pet existing = new Pet();
		existing.setId(1);
		existing.setName("Fluffy");
		owner.getPets().add(existing);

		Pet pet = new Pet();
		pet.setName("Fluffy");
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");

		String view = controller.processCreationForm(owner, pet, result, new RedirectAttributesModelMap());
		assertEquals("pets/createOrUpdatePetForm", view);
	}

	@Test
	void processCreationFormRejectsFutureBirthDate() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.now().plusDays(1));
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");

		String view = controller.processCreationForm(owner, pet, result, new RedirectAttributesModelMap());
		assertEquals("pets/createOrUpdatePetForm", view);
	}

	@Test
	void processCreationFormSavesAndRedirects() {
		Owner owner = new Owner();
		owner.setId(5);
		Pet pet = new Pet();
		pet.setName("Buddy");
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");
		RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

		String view = controller.processCreationForm(owner, pet, result, redirect);

		verify(owners).save(owner);
		assertEquals("redirect:/owners/{ownerId}", view);
		assertEquals("New Pet has been Added", redirect.getFlashAttributes().get("message"));
	}

	@Test
	void initUpdateFormReturnsView() {
		assertEquals("pets/createOrUpdatePetForm", controller.initUpdateForm());
	}

	@Test
	void processUpdateFormRejectsDuplicateNameOnDifferentPet() {
		Owner owner = new Owner();
		Pet existing = new Pet();
		existing.setId(1);
		existing.setName("Rex");
		owner.getPets().add(existing);

		Pet pet = new Pet();
		pet.setId(2);
		pet.setName("Rex");
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");

		String view = controller.processUpdateForm(owner, pet, result, new RedirectAttributesModelMap());
		assertEquals("pets/createOrUpdatePetForm", view);
	}

	@Test
	void processUpdateFormRejectsFutureBirthDate() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(2);
		pet.setName("Rex");
		pet.setBirthDate(LocalDate.now().plusDays(1));
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");

		String view = controller.processUpdateForm(owner, pet, result, new RedirectAttributesModelMap());
		assertEquals("pets/createOrUpdatePetForm", view);
	}

	@Test
	void processUpdateFormUpdatesExistingPetAndRedirects() {
		Owner owner = new Owner();
		Pet existing = new Pet();
		existing.setId(10);
		existing.setName("Old");
		owner.getPets().add(existing);

		PetType cat = new PetType();
		cat.setName("cat");
		Pet pet = new Pet();
		pet.setId(10);
		pet.setName("NewName");
		pet.setBirthDate(LocalDate.of(2020, 1, 1));
		pet.setType(cat);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");
		RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

		String view = controller.processUpdateForm(owner, pet, result, redirect);

		verify(owners).save(owner);
		assertEquals("redirect:/owners/{ownerId}", view);
		assertEquals("NewName", existing.getName());
		assertEquals("Pet details has been edited", redirect.getFlashAttributes().get("message"));
	}

	@Test
	void processUpdateFormThrowsWhenPetIdMissing() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("NoIdPet");
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");

		assertThrows(IllegalStateException.class,
				() -> controller.processUpdateForm(owner, pet, result, new RedirectAttributesModelMap()));
	}

}
