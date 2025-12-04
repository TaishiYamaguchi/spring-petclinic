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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test class for {@link Owner}
 *
 * @author Taishi Yamaguchi
 */
class OwnerTests {

	private Owner owner;

	private Pet pet1;

	private Pet pet2;

	private PetType petType;

	@BeforeEach
	void setUp() {
		owner = new Owner();
		owner.setId(1);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");

		petType = new PetType();
		petType.setId(1);
		petType.setName("dog");

		pet1 = new Pet();
		pet1.setName("Max");
		pet1.setType(petType);
		pet1.setBirthDate(LocalDate.of(2020, 1, 1));

		pet2 = new Pet();
		pet2.setId(2);
		pet2.setName("Bella");
		pet2.setType(petType);
		pet2.setBirthDate(LocalDate.of(2019, 6, 15));
	}

	@Test
	void testGettersAndSetters() {
		assertThat(owner.getAddress()).isEqualTo("110 W. Liberty St.");
		assertThat(owner.getCity()).isEqualTo("Madison");
		assertThat(owner.getTelephone()).isEqualTo("6085551023");

		owner.setAddress("123 Main St.");
		owner.setCity("Springfield");
		owner.setTelephone("5551234567");

		assertThat(owner.getAddress()).isEqualTo("123 Main St.");
		assertThat(owner.getCity()).isEqualTo("Springfield");
		assertThat(owner.getTelephone()).isEqualTo("5551234567");
	}

	@Test
	void testGetPetsInitiallyEmpty() {
		assertThat(owner.getPets()).isNotNull();
		assertThat(owner.getPets()).isEmpty();
	}

	@Nested
	class AddPetTests {

		@Test
		void testAddNewPet() {
			owner.addPet(pet1);

			assertThat(owner.getPets()).hasSize(1);
			assertThat(owner.getPets()).contains(pet1);
		}

		@Test
		void testAddMultipleNewPets() {
			Pet pet3 = new Pet();
			pet3.setName("Charlie");
			pet3.setType(petType);

			owner.addPet(pet1);
			owner.addPet(pet3);

			assertThat(owner.getPets()).hasSize(2);
			assertThat(owner.getPets()).containsExactly(pet1, pet3);
		}

		@Test
		void testAddExistingPetDoesNotDuplicate() {
			owner.addPet(pet2); // pet2 already has an ID

			// Existing pet (with ID) should not be added
			assertThat(owner.getPets()).isEmpty();
		}

	}

	@Nested
	class GetPetByNameTests {

		@BeforeEach
		void setUpPets() {
			owner.addPet(pet1);
			owner.getPets().add(pet2);
		}

		@Test
		void testGetPetByExactName() {
			Pet result = owner.getPet("Max");

			assertThat(result).isNotNull();
			assertThat(result.getName()).isEqualTo("Max");
		}

		@Test
		void testGetPetByCaseInsensitiveName() {
			Pet result = owner.getPet("max");

			assertThat(result).isNotNull();
			assertThat(result.getName()).isEqualTo("Max");
		}

		@Test
		void testGetPetByNameNotFound() {
			Pet result = owner.getPet("NonExistent");

			assertThat(result).isNull();
		}

		@Test
		void testGetPetByNameWithIgnoreNewFalse() {
			Pet result = owner.getPet("Max", false);

			assertThat(result).isNotNull();
			assertThat(result.getName()).isEqualTo("Max");
		}

		@Test
		void testGetPetByNameWithIgnoreNewTrue() {
			// pet1 is new (no ID), so it should be ignored
			Pet result = owner.getPet("Max", true);

			assertThat(result).isNull();
		}

		@Test
		void testGetPetByNameIgnoreNewDoesNotIgnoreExisting() {
			// pet2 has an ID, so it should be found even with ignoreNew=true
			Pet result = owner.getPet("Bella", true);

			assertThat(result).isNotNull();
			assertThat(result.getName()).isEqualTo("Bella");
		}

	}

	@Nested
	class GetPetByIdTests {

		@BeforeEach
		void setUpPets() {
			owner.addPet(pet1);
			owner.getPets().add(pet2);
		}

		@Test
		void testGetPetByValidId() {
			Pet result = owner.getPet(2);

			assertThat(result).isNotNull();
			assertThat(result.getId()).isEqualTo(2);
			assertThat(result.getName()).isEqualTo("Bella");
		}

		@Test
		void testGetPetByInvalidId() {
			Pet result = owner.getPet(999);

			assertThat(result).isNull();
		}

		@Test
		void testGetPetByIdIgnoresNewPets() {
			// pet1 is new (no ID), so searching by any ID won't find it
			Pet result = owner.getPet(1);

			assertThat(result).isNull();
		}

	}

	@Nested
	class AddVisitTests {

		private Visit visit;

		@BeforeEach
		void setUpVisit() {
			owner.getPets().add(pet2);

			visit = new Visit();
			visit.setDate(LocalDate.now());
			visit.setDescription("Checkup");
		}

		@Test
		void testAddVisitToExistingPet() {
			owner.addVisit(2, visit);

			assertThat(pet2.getVisits()).hasSize(1);
			assertThat(pet2.getVisits()).contains(visit);
		}

		@Test
		void testAddMultipleVisitsToSamePet() {
			Visit visit2 = new Visit();
			visit2.setDate(LocalDate.now().plusDays(7));
			visit2.setDescription("Follow-up");

			owner.addVisit(2, visit);
			owner.addVisit(2, visit2);

			assertThat(pet2.getVisits()).hasSize(2);
			assertThat(pet2.getVisits()).containsExactly(visit, visit2);
		}

		@Test
		void testAddVisitWithNullPetIdThrowsException() {
			assertThatThrownBy(() -> owner.addVisit(null, visit)).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Pet identifier must not be null");
		}

		@Test
		void testAddVisitWithNullVisitThrowsException() {
			assertThatThrownBy(() -> owner.addVisit(2, null)).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Visit must not be null");
		}

		@Test
		void testAddVisitWithInvalidPetIdThrowsException() {
			assertThatThrownBy(() -> owner.addVisit(999, visit)).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Invalid Pet identifier");
		}

	}

	@Test
	void testToString() {
		String result = owner.toString();

		assertThat(result).contains("id = 1");
		assertThat(result).contains("new = false");
		assertThat(result).contains("lastName = 'Franklin'");
		assertThat(result).contains("firstName = 'George'");
		assertThat(result).contains("address = '110 W. Liberty St.'");
		assertThat(result).contains("city = 'Madison'");
		assertThat(result).contains("telephone = '6085551023'");
	}

	@Test
	void testSerialization() {
		owner.addPet(pet1);

		@SuppressWarnings("deprecation")
		Owner deserialized = (Owner) SerializationUtils.deserialize(SerializationUtils.serialize(owner));

		assertThat(deserialized.getId()).isEqualTo(owner.getId());
		assertThat(deserialized.getFirstName()).isEqualTo(owner.getFirstName());
		assertThat(deserialized.getLastName()).isEqualTo(owner.getLastName());
		assertThat(deserialized.getAddress()).isEqualTo(owner.getAddress());
		assertThat(deserialized.getCity()).isEqualTo(owner.getCity());
		assertThat(deserialized.getTelephone()).isEqualTo(owner.getTelephone());
	}

}
