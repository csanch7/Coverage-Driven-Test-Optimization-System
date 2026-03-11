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
 * Comprehensive unit tests for Visit domain model.
 *
 * Tests cover: - Default constructor (sets current date) - Date property (get/set) -
 * Description property (get/set) - Validation and edge cases
 */
class VisitTest {

	private Visit visit;

	@BeforeEach
	void setup() {
		visit = new Visit();
	}

	// ============== Constructor Tests ==============

	@Test
	void testDefaultConstructorSetsCurrentDate() {
		Visit newVisit = new Visit();
		assertNotNull(newVisit.getDate());
		assertEquals(LocalDate.now(), newVisit.getDate());
	}

	@Test
	void testDefaultConstructorDateIsToday() {
		Visit newVisit = new Visit();
		LocalDate today = LocalDate.now();
		assertEquals(today, newVisit.getDate());
	}

	@Test
	void testDefaultConstructorInitializesWithCurrectPrecision() {
		// Multiple instances should get the same date if created in quick succession
		Visit visit1 = new Visit();
		Visit visit2 = new Visit();
		assertEquals(visit1.getDate(), visit2.getDate());
	}

	// ============== Date Property Tests ==============

	@Test
	void testDatePropertyInitialized() {
		assertNotNull(visit.getDate());
	}

	@Test
	void testDatePropertyGet() {
		LocalDate date = LocalDate.now();
		assertEquals(date, visit.getDate());
	}

	@Test
	void testDatePropertySet() {
		LocalDate newDate = LocalDate.of(2023, 6, 15);
		visit.setDate(newDate);
		assertEquals(newDate, visit.getDate());
	}

	@Test
	void testDatePropertySetNull() {
		visit.setDate(null);
		assertNull(visit.getDate());
	}

	@Test
	void testDatePropertySetPastDate() {
		LocalDate pastDate = LocalDate.of(2020, 1, 1);
		visit.setDate(pastDate);
		assertEquals(pastDate, visit.getDate());
	}

	@Test
	void testDatePropertySetFutureDate() {
		LocalDate futureDate = LocalDate.of(2030, 12, 31);
		visit.setDate(futureDate);
		assertEquals(futureDate, visit.getDate());
	}

	@Test
	void testDatePropertyReplacement() {
		LocalDate date1 = LocalDate.of(2022, 3, 15);
		LocalDate date2 = LocalDate.of(2023, 5, 20);
		visit.setDate(date1);
		assertEquals(date1, visit.getDate());
		visit.setDate(date2);
		assertEquals(date2, visit.getDate());
	}

	@Test
	void testDatePropertyAfterMultipleUpdates() {
		visit.setDate(LocalDate.of(2021, 1, 1));
		visit.setDate(LocalDate.of(2022, 6, 15));
		visit.setDate(LocalDate.of(2023, 12, 25));
		assertEquals(LocalDate.of(2023, 12, 25), visit.getDate());
	}

	// ============== Description Property Tests ==============

	@Test
	void testDescriptionPropertyInitiallyNull() {
		assertNull(visit.getDescription());
	}

	@Test
	void testDescriptionPropertySet() {
		String description = "Routine checkup";
		visit.setDescription(description);
		assertEquals(description, visit.getDescription());
	}

	@Test
	void testDescriptionPropertySetNull() {
		visit.setDescription("Initial description");
		visit.setDescription(null);
		assertNull(visit.getDescription());
	}

	@Test
	void testDescriptionPropertySetEmpty() {
		visit.setDescription("");
		assertEquals("", visit.getDescription());
	}

	@Test
	void testDescriptionPropertySetLong() {
		String longDescription = "This is a very long description that contains detailed information about the visit including symptoms, observations, and treatment recommendations. The length should not pose any issue for storage.";
		visit.setDescription(longDescription);
		assertEquals(longDescription, visit.getDescription());
	}

	@Test
	void testDescriptionPropertyWithSpecialCharacters() {
		String description = "Visit with notes: vaccinations, check-ups & tests (urgent!)";
		visit.setDescription(description);
		assertEquals(description, visit.getDescription());
	}

	@Test
	void testDescriptionPropertyReplacement() {
		visit.setDescription("Initial description");
		assertEquals("Initial description", visit.getDescription());
		visit.setDescription("Updated description");
		assertEquals("Updated description", visit.getDescription());
	}

	@Test
	void testDescriptionPropertyWithNewlines() {
		String multilineDescription = "Line 1\nLine 2\nLine 3";
		visit.setDescription(multilineDescription);
		assertEquals(multilineDescription, visit.getDescription());
	}

	// ============== Combined Property Tests ==============

	@Test
	void testVisitWithDateAndDescription() {
		LocalDate visitDate = LocalDate.of(2023, 7, 10);
		String description = "Annual checkup";

		visit.setDate(visitDate);
		visit.setDescription(description);

		assertEquals(visitDate, visit.getDate());
		assertEquals(description, visit.getDescription());
	}

	@Test
	void testMultipleVisitInstancesAreIndependent() {
		Visit visit1 = new Visit();
		Visit visit2 = new Visit();

		visit1.setDate(LocalDate.of(2023, 1, 1));
		visit1.setDescription("First visit");

		visit2.setDate(LocalDate.of(2023, 6, 15));
		visit2.setDescription("Second visit");

		assertEquals(LocalDate.of(2023, 1, 1), visit1.getDate());
		assertEquals("First visit", visit1.getDescription());
		assertEquals(LocalDate.of(2023, 6, 15), visit2.getDate());
		assertEquals("Second visit", visit2.getDescription());
	}

	// ============== Inherited Properties Tests ==============

	@Test
	void testInheritedIdProperty() {
		assertNull(visit.getId());
		assertTrue(visit.isNew());

		visit.setId(1);
		assertEquals(1, visit.getId());
		assertFalse(visit.isNew());
	}

	@Test
	void testCompleteVisitState() {
		visit.setId(5);
		visit.setDate(LocalDate.of(2023, 10, 20));
		visit.setDescription("Post-surgery follow-up");

		assertEquals(5, visit.getId());
		assertFalse(visit.isNew());
		assertEquals(LocalDate.of(2023, 10, 20), visit.getDate());
		assertEquals("Post-surgery follow-up", visit.getDescription());
	}

	// ============== Edge Cases ==============

	@Test
	void testVisitCreationWithLeapYearDate() {
		visit.setDate(LocalDate.of(2020, 2, 29)); // Leap year
		assertEquals(LocalDate.of(2020, 2, 29), visit.getDate());
	}

	@Test
	void testVisitCreationWithYearBoundary() {
		visit.setDate(LocalDate.of(2023, 12, 31));
		assertEquals(LocalDate.of(2023, 12, 31), visit.getDate());
	}

	@Test
	void testVisitCreationWithNewYear() {
		visit.setDate(LocalDate.of(2024, 1, 1));
		assertEquals(LocalDate.of(2024, 1, 1), visit.getDate());
	}

	@Test
	void testVisitDateSequence() {
		LocalDate date1 = LocalDate.of(2023, 1, 15);
		LocalDate date2 = LocalDate.of(2023, 2, 10);
		LocalDate date3 = LocalDate.of(2023, 3, 5);

		visit.setDate(date1);
		assertTrue(visit.getDate().isBefore(date2));

		visit.setDate(date2);
		assertTrue(visit.getDate().isBefore(date3));

		visit.setDate(date3);
		assertFalse(visit.getDate().isBefore(date3));
	}

}
