package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class OwnerControllerTest {

	@Mock
	private OwnerRepository owners;

	private OwnerController controller;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		controller = new OwnerController(owners);
	}

	@Test
	void initCreationFormShowsForm() {
		assertEquals("owners/createOrUpdateOwnerForm", controller.initCreationForm());
	}

	@Test
	void processCreationFormWithErrorsReturnsForm() {
		Owner owner = new Owner();
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(owner, "owner");
		result.rejectValue("lastName", "required", "required");

		RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();
		String view = controller.processCreationForm(owner, result, redirect);

		assertEquals("owners/createOrUpdateOwnerForm", view);
		assertEquals("There was an error in creating the owner.", redirect.getFlashAttributes().get("error"));
	}

	@Test
	void processCreationFormWithoutErrorsRedirectsToOwner() {
		Owner owner = new Owner();
		owner.setId(7);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(owner, "owner");
		RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

		String view = controller.processCreationForm(owner, result, redirect);

		verify(owners).save(owner);
		assertEquals("redirect:/owners/7", view);
		assertEquals("New Owner Created", redirect.getFlashAttributes().get("message"));
	}

	@Test
	void processFindFormNoResultsReturnsFindOwners() {
		Owner owner = new Owner();
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(owner, "owner");
		org.springframework.ui.ConcurrentModel model = new org.springframework.ui.ConcurrentModel();

		when(owners.findByLastNameStartingWith(any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));

		String view = controller.processFindForm(1, owner, result, model);

		assertEquals("owners/findOwners", view);
	}

	@Test
	void showOwnerReturnsDetailsWhenFound() {
		Owner owner = new Owner();
		owner.setId(1);
		when(owners.findById(1)).thenReturn(Optional.of(owner));

		ModelAndView mav = controller.showOwner(1);

		assertEquals("owners/ownerDetails", mav.getViewName());
	}

	@Test
	void showOwnerThrowsWhenNotFound() {
		when(owners.findById(999)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> controller.showOwner(999));
	}

}
