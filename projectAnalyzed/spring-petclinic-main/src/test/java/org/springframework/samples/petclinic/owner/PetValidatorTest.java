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
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Errors;

/**
 * Comprehensive unit tests for PetValidator.
 *
 * Tests cover: - Name validation (blank, empty, null) - Type validation for new pets -
 * Birth date validation - Edge cases and validator support
 */
class PetValidatorTest {

	private PetValidator validator;

	private Pet pet;

	private Errors errors;

	@BeforeEach
	void setup() {
		validator = new PetValidator();
		pet = new Pet();
		errors = new DirectFieldBindingResult(pet, "pet");
	}

	// ============== Name Validation Tests ==============

	@Test
	void testNameValidation_BlankName() {
		pet.setName("");
		validator.validate(pet, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals(1, errors.getFieldErrorCount("name"));
	}

	@Test
	void testNameValidation_NullName() {
		pet.setName(null);
		validator.validate(pet, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals(1, errors.getFieldErrorCount("name"));
	}

	@Test
	void testNameValidation_WhitespaceOnlyName() {
		pet.setName("   ");
		validator.validate(pet, errors);
		assertTrue(errors.hasFieldErrors("name"));
	}

	@Test
	void testNameValidation_ValidName() {
		pet.setName("Fluffy");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.now());
		validator.validate(pet, errors);
		assertFalse(errors.hasFieldErrors("name"));
	}

	@Test
	void testNameValidation_SingleCharacterName() {
		pet.setName("F");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.now());
		validator.validate(pet, errors);
		assertFalse(errors.hasFieldErrors("name"));
	}

	@Test
	void testNameValidation_LongName() {
		pet.setName("VeryLongPetNameWithManyCharactersToTestEdgeCase");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.now());
		validator.validate(pet, errors);
		assertFalse(errors.hasFieldErrors("name"));
	}

	// ============== Type Validation Tests ==============

	@Test
	void testTypeValidation_NewPetWithoutType() {
		// New pet (id is null)
		pet.setName("Fluffy");
		pet.setType(null);
		pet.setBirthDate(LocalDate.now());
		validator.validate(pet, errors);
		assertTrue(errors.hasFieldErrors("type"));
		assertEquals(1, errors.getFieldErrorCount("type"));
	}

	@Test
	void testTypeValidation_NewPetWithType() {
		pet.setName("Fluffy");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.now());
		validator.validate(pet, errors);
		assertFalse(errors.hasFieldErrors("type"));
	}

	@Test
	void testTypeValidation_ExistingPetWithoutType() {
		// Existing pet (has id set)
		pet.setId(1);
		pet.setName("Fluffy");
		pet.setType(null);
		pet.setBirthDate(LocalDate.now());
		validator.validate(pet, errors);
		// Should not validate type for existing pets
		assertFalse(errors.hasFieldErrors("type"));
	}

	@Test
	void testTypeValidation_ExistingPetWithType() {
		pet.setId(1);
		pet.setName("Fluffy");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.now());
		validator.validate(pet, errors);
		assertFalse(errors.hasFieldErrors("type"));
	}

	// ============== Birth Date Validation Tests ==============

	@Test
	void testBirthDateValidation_NullBirthDate() {
		pet.setName("Fluffy");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(null);
		validator.validate(pet, errors);
		assertTrue(errors.hasFieldErrors("birthDate"));
		assertEquals(1, errors.getFieldErrorCount("birthDate"));
	}

	@Test
	void testBirthDateValidation_ValidBirthDate() {
		pet.setName("Fluffy");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.now().minusYears(1));
		validator.validate(pet, errors);
		assertFalse(errors.hasFieldErrors("birthDate"));
	}

	@Test
	void testBirthDateValidation_TodayAsDate() {
		pet.setName("Fluffy");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.now());
		validator.validate(pet, errors);
		assertFalse(errors.hasFieldErrors("birthDate"));
	}

	@Test
	void testBirthDateValidation_OldDate() {
		pet.setName("Fluffy");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.of(1990, 1, 1));
		validator.validate(pet, errors);
		assertFalse(errors.hasFieldErrors("birthDate"));
	}

	// ============== Multiple Field Error Tests ==============

	@Test
	void testMultipleFieldErrors() {
		// All fields invalid
		pet.setName("");
		pet.setType(null);
		pet.setBirthDate(null);
		validator.validate(pet, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertTrue(errors.hasFieldErrors("type"));
		assertTrue(errors.hasFieldErrors("birthDate"));
		assertEquals(3, errors.getErrorCount());
	}

	@Test
	void testMultipleFieldErrors_NameAndBirthDate() {
		// Name and birth date invalid
		pet.setName("");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(null);
		validator.validate(pet, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertFalse(errors.hasFieldErrors("type"));
		assertTrue(errors.hasFieldErrors("birthDate"));
		assertEquals(2, errors.getErrorCount());
	}

	// ============== Validator Support Tests ==============

	@Test
	void testSupports_PetClass() {
		assertTrue(validator.supports(Pet.class));
	}

	@Test
	void testSupports_PetSubclass() {
		// Any subclass of Pet should be supported
		class PetSubclass extends Pet {

		}
		assertTrue(validator.supports(PetSubclass.class));
	}

	@Test
	void testSupports_OwnerClass() {
		assertFalse(validator.supports(Owner.class));
	}

	@Test
	void testSupports_StringClass() {
		assertFalse(validator.supports(String.class));
	}

	@Test
	void testSupports_NullClass() {
		assertFalse(validator.supports(null));
	}

	// ============== Edge Cases and Combined Tests ==============

	@Test
	void testCompleteValid_NewPet() {
		pet.setName("Buddy");
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Cat");
		pet.setType(petType);
		pet.setBirthDate(LocalDate.of(2020, 5, 15));
		validator.validate(pet, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	void testCompleteValid_ExistingPet() {
		pet.setId(5);
		pet.setName("Max");
		pet.setType(null); // Type not required for existing pets
		pet.setBirthDate(LocalDate.of(2015, 3, 20));
		validator.validate(pet, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	void testValidationErrorMessages() {
		pet.setName("");
		pet.setType(null);
		pet.setBirthDate(null);
		validator.validate(pet, errors);
		assertTrue(errors.hasErrors());
		assertNotNull(errors.getFieldError("name"));
		assertEquals("required", errors.getFieldError("name").getCode());
	}

}
