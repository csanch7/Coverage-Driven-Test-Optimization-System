package org.springframework.samples.petclinic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class PetClinicApplicationTest {

	@Test
	void mainDelegatesToSpringApplicationRun() {
		try (MockedStatic<SpringApplication> springApplication = Mockito.mockStatic(SpringApplication.class)) {
			springApplication.when(() -> SpringApplication.run(eq(PetClinicApplication.class), any(String[].class)))
				.thenReturn(null);

			PetClinicApplication.main(new String[] { "--test" });

			springApplication.verify(() -> SpringApplication.run(eq(PetClinicApplication.class), any(String[].class)),
					times(1));
		}
	}

}
