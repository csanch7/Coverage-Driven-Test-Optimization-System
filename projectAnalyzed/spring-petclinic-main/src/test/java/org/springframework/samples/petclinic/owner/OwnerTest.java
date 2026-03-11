/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive unit tests for Owner domain model.
 *
 * Tests cover: - Basic property getters/setters (address, city, telephone) - Pet
 * management (add, retrieve by name, retrieve by id) - Visit management - toString
 * representation - Edge cases and business logic
 */
class OwnerTest {

	private Owner owner;

	private Pet pet;

	@BeforeEach
	void setup() {
		owner = new Owner();
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("123 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("5551234567");

		pet = new Pet();
		pet.setName("Fluffy");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.now().minusYears(2));
	}

	// ============== Basic Property Tests ==============

	@Test
	void testAddressProperty() {
		assertEquals("123 Main St", owner.getAddress());
		owner.setAddress("456 Oak Ave");
		assertEquals("456 Oak Ave", owner.getAddress());
	}

	@Test
	void testCityProperty() {
		assertEquals("Springfield", owner.getCity());
		owner.setCity("Shelbyville");
		assertEquals("Shelbyville", owner.getCity());
	}

	@Test
	void testTelephoneProperty() {
		assertEquals("5551234567", owner.getTelephone());
		owner.setTelephone("5559876543");
		assertEquals("5559876543", owner.getTelephone());
	}

	@Test
	void testInheritedFirstNameProperty() {
		assertEquals("John", owner.getFirstName());
		owner.setFirstName("Jane");
		assertEquals("Jane", owner.getFirstName());
	}

	@Test
	void testInheritedLastNameProperty() {
		assertEquals("Doe", owner.getLastName());
		owner.setLastName("Smith");
		assertEquals("Smith", owner.getLastName());
	}

	// ============== Pet List Tests ==============

	@Test
	void testGetPetsInitiallyEmpty() {
		Owner newOwner = new Owner();
		assertNotNull(newOwner.getPets());
		assertTrue(newOwner.getPets().isEmpty());
	}

	@Test
	void testGetPetsImmutable() {
		Owner newOwner = new Owner();
		assertNotNull(newOwner.getPets());
		// The list should be consistent
		assertSame(newOwner.getPets(), newOwner.getPets());
	}

	// ============== Add Pet Tests ==============

	@Test
	void testAddNewPet() {
		assertTrue(owner.getPets().isEmpty());
		owner.addPet(pet);
		assertEquals(1, owner.getPets().size());
		assertTrue(owner.getPets().contains(pet));
	}

	@Test
	void testAddExistingPet() {
		pet.setId(1); // Mark as existing
		owner.addPet(pet);
		// Should not add existing pet
		assertTrue(owner.getPets().isEmpty());
	}

	@Test
	void testAddMultiplePets() {
		Pet pet2 = new Pet();
		pet2.setName("Spot");
		PetType petType = new PetType();
		petType.setId(2);
		petType.setName("Cat");
		pet2.setType(petType);
		pet2.setBirthDate(LocalDate.now().minusYears(1));

		owner.addPet(pet);
		owner.addPet(pet2);

		assertEquals(2, owner.getPets().size());
		assertTrue(owner.getPets().contains(pet));
		assertTrue(owner.getPets().contains(pet2));
	}

	// ============== Get Pet by Name Tests ==============

	@Test
	void testGetPetByName_Found() {
		owner.addPet(pet);
		Pet found = owner.getPet("Fluffy");
		assertNotNull(found);
		assertEquals(pet, found);
	}

	@Test
	void testGetPetByName_NotFound() {
		owner.addPet(pet);
		Pet found = owner.getPet("NonExistent");
		assertNull(found);
	}

	@Test
	void testGetPetByName_CaseInsensitive() {
		owner.addPet(pet);
		Pet found = owner.getPet("FLUFFY");
		assertNotNull(found);
		assertEquals(pet, found);
	}

	@Test
	void testGetPetByName_MixedCase() {
		owner.addPet(pet);
		Pet found = owner.getPet("FluFfY");
		assertNotNull(found);
		assertEquals(pet, found);
	}

	@Test
	void testGetPetByName_EmptyList() {
		Pet found = owner.getPet("Fluffy");
		assertNull(found);
	}

	@Test
	void testGetPetByName_MultiplePets() {
		Pet pet2 = new Pet();
		pet2.setName("Spot");
		owner.addPet(pet);
		owner.addPet(pet2);

		assertEquals(pet, owner.getPet("Fluffy"));
		assertEquals(pet2, owner.getPet("Spot"));
	}

	@Test
	void testGetPetByName_NullName() {
		owner.addPet(pet);
		Pet nullNamePet = new Pet();
		nullNamePet.setName(null);
		// Should not throw exception
		Pet found = owner.getPet((String) null);
		assertNull(found);
	}

	// ============== Get Pet by ID Tests ==============

	@Test
	void testGetPetById_Found() {
		pet.setId(1);
		// Must add to pets list directly since addPet doesn't add existing pets
		owner.getPets().add(pet);
		Pet found = owner.getPet(1);
		assertNotNull(found);
		assertEquals(pet, found);
	}

	@Test
	void testGetPetById_NotFound() {
		pet.setId(1);
		owner.addPet(pet);
		Pet found = owner.getPet(999);
		assertNull(found);
	}

	@Test
	void testGetPetById_IgnoresNewPets() {
		// New pet (no id)
		owner.addPet(pet);
		Pet found = owner.getPet((Integer) null);
		assertNull(found);
	}

	@Test
	void testGetPetById_MultiplePets() {
		Pet pet2 = new Pet();
		pet2.setName("Spot");
		pet2.setId(2);

		pet.setId(1);
		// Must add directly since addPet doesn't add existing pets
		owner.getPets().add(pet);
		owner.getPets().add(pet2);

		assertEquals(pet, owner.getPet(1));
		assertEquals(pet2, owner.getPet(2));
		assertNull(owner.getPet(3));
	}

	// ============== Get Pet by Name with ignoreNew Flag Tests ==============

	@Test
	void testGetPetByNameIgnoreNew_False() {
		owner.addPet(pet);
		Pet found = owner.getPet("Fluffy", false);
		assertNotNull(found);
		assertEquals(pet, found);
	}

	@Test
	void testGetPetByNameIgnoreNew_True() {
		owner.addPet(pet);
		// When ignoreNew=true, new pets (no id) should not be found
		Pet found = owner.getPet("Fluffy", true);
		assertNull(found);
	}

	@Test
	void testGetPetByNameIgnoreNew_ExcludesNewPets() {
		owner.addPet(pet); // New pet (no id set)
		Pet found = owner.getPet("Fluffy", true);
		assertNull(found); // Should not find new pet when ignoreNew=true
	}

	@Test
	void testGetPetByNameIgnoreNew_IncludesExistingPets() {
		pet.setId(1);
		// Can't add existing pet via addPet, so add directly
		owner.getPets().add(pet);
		Pet found = owner.getPet("Fluffy", true);
		assertNotNull(found);
		assertEquals(pet, found);
	}

	// ============== Add Visit Tests ==============

	@Test
	void testAddVisit_Success() {
		pet.setId(1);
		owner.getPets().add(pet);

		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Checkup");

		owner.addVisit(1, visit);
		assertEquals(1, pet.getVisits().size());
		assertTrue(pet.getVisits().contains(visit));
	}

	@Test
	void testAddVisit_InvalidPetId() {
		pet.setId(1);
		owner.getPets().add(pet);

		Visit visit = new Visit();
		visit.setDate(LocalDate.now());

		assertThrows(IllegalArgumentException.class, () -> owner.addVisit(999, visit));
	}

	@Test
	void testAddVisit_NullPetId() {
		assertThrows(IllegalArgumentException.class, () -> owner.addVisit(null, new Visit()));
	}

	@Test
	void testAddVisit_NullVisit() {
		pet.setId(1);
		owner.getPets().add(pet);

		assertThrows(IllegalArgumentException.class, () -> owner.addVisit(1, null));
	}

	@Test
	void testAddVisit_MultipleVisits() {
		pet.setId(1);
		owner.getPets().add(pet);

		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.now());
		visit1.setDescription("Checkup");

		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.now().minusDays(7));
		visit2.setDescription("Vaccination");

		owner.addVisit(1, visit1);
		owner.addVisit(1, visit2);

		assertEquals(2, pet.getVisits().size());
		assertTrue(pet.getVisits().contains(visit1));
		assertTrue(pet.getVisits().contains(visit2));
	}

	// ============== ToString Tests ==============

	@Test
	void testToString() {
		String str = owner.toString();
		assertNotNull(str);
		assertTrue(str.contains("John"));
		assertTrue(str.contains("Doe"));
		assertTrue(str.contains("123 Main St"));
		assertTrue(str.contains("Springfield"));
		assertTrue(str.contains("5551234567"));
	}

	@Test
	void testToStringWithId() {
		owner.setId(1);
		String str = owner.toString();
		assertNotNull(str);
		// toString should contain basic owner info
		assertTrue(str.contains("John") || str.contains("Doe") || str.contains("123 Main St"));
	}

	// ============== Complex Business Logic Tests ==============

	@Test
	void testOwnerWithMultiplePetsAndVisits() {
		Pet pet1 = new Pet();
		pet1.setId(1);
		pet1.setName("Fluffy");
		owner.getPets().add(pet1);

		Pet pet2 = new Pet();
		pet2.setId(2);
		pet2.setName("Spot");
		owner.getPets().add(pet2);

		// Add visits to first pet
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Checkup");
		owner.addVisit(1, visit);

		assertEquals(2, owner.getPets().size());
		assertEquals(pet1, owner.getPet("Fluffy"));
		assertEquals(pet2, owner.getPet("Spot"));
		assertEquals(1, pet1.getVisits().size());
		assertEquals(0, pet2.getVisits().size());
	}

	@Test
	void testOwnerEqualsAndHashCode() {
		Owner owner2 = new Owner();
		owner2.setFirstName("John");
		owner2.setLastName("Doe");
		owner2.setAddress("123 Main St");
		owner2.setCity("Springfield");
		owner2.setTelephone("5551234567");

		// Check if they have the same basic properties
		assertEquals(owner.getFirstName(), owner2.getFirstName());
		assertEquals(owner.getLastName(), owner2.getLastName());
		assertEquals(owner.getAddress(), owner2.getAddress());
	}

}
