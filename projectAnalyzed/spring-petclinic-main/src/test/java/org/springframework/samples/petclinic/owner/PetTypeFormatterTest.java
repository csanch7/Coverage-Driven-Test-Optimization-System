package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PetTypeFormatterTest {

	@Mock
	private PetTypeRepository types;

	private PetTypeFormatter formatter;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		formatter = new PetTypeFormatter(types);
	}

	@Test
	void printReturnsNameWhenPresent() {
		PetType petType = new PetType();
		petType.setName("hamster");
		assertEquals("hamster", formatter.print(petType, Locale.ENGLISH));
	}

	@Test
	void printReturnsNullMarkerWhenNameMissing() {
		PetType petType = new PetType();
		assertEquals("<null>", formatter.print(petType, Locale.ENGLISH));
	}

	@Test
	void parseReturnsMatchingType() throws Exception {
		PetType dog = new PetType();
		dog.setName("dog");
		when(types.findPetTypes()).thenReturn(List.of(dog));

		assertEquals(dog, formatter.parse("dog", Locale.ENGLISH));
	}

	@Test
	void parseThrowsWhenTypeNotFound() {
		when(types.findPetTypes()).thenReturn(List.of());
		assertThrows(ParseException.class, () -> formatter.parse("dragon", Locale.ENGLISH));
	}

}
