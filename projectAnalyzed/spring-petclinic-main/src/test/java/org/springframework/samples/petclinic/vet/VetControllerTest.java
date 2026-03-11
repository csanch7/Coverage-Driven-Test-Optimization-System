package org.springframework.samples.petclinic.vet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;

class VetControllerTest {

	@Mock
	private VetRepository vetRepository;

	private VetController controller;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		controller = new VetController(vetRepository);
	}

	@Test
	void showVetListAddsPaginationModelAndReturnsView() {
		Vet vet = new Vet();
		vet.setFirstName("Jane");
		when(vetRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
			.thenReturn(new PageImpl<>(List.of(vet)));

		org.springframework.ui.ConcurrentModel model = new org.springframework.ui.ConcurrentModel();
		String view = controller.showVetList(1, model);

		assertEquals("vets/vetList", view);
		assertEquals(1, model.getAttribute("currentPage"));
		assertEquals(1, model.getAttribute("totalPages"));
		assertEquals(1L, model.getAttribute("totalItems"));
	}

	@Test
	void showResourcesVetListReturnsAllVets() {
		Vet vet = new Vet();
		vet.setFirstName("Sam");
		when(vetRepository.findAll()).thenReturn(List.of(vet));

		Vets vets = controller.showResourcesVetList();

		assertEquals(1, vets.getVetList().size());
	}

}
