package com.tarmiz.SIRH_backend;
/*
import com.tarmiz.SIRH_backend.enums.EmployeeStatus;
import com.tarmiz.SIRH_backend.enums.Gender;
import com.tarmiz.SIRH_backend.model.entity.CompanyHierarchy.Department;
import com.tarmiz.SIRH_backend.model.repository.DepartmentRepository;
import tools.jackson.databind.ObjectMapper;
import com.tarmiz.SIRH_backend.model.DTO.AttestationDTO;
import com.tarmiz.SIRH_backend.model.DTO.AttestationRequestDTO;
import com.tarmiz.SIRH_backend.model.entity.EmployeeInfos.Employee;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AttestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    private Long employeeId;

    @BeforeEach
    public void setup() {
        // Ensure department exists
        Department department = departmentRepo.findByName("IT").orElseGet(() -> {
            Department dep = new Department();
            dep.setName("IT");
            return departmentRepo.save(dep);
        });

        // Create unique employee
        String uuid = UUID.randomUUID().toString();
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Doe");
        emp.setMatricule("EMP-" + uuid);
        emp.setCin("CIN-" + uuid);
        emp.setEmail("john.doe+" + uuid + "@example.com");
        emp.setCreatedBy("SYSTEM");
        emp.setDepartment(department);

        // Required fields
        emp.setStatus(EmployeeStatus.ACTIF);
        emp.setBirthDate(java.time.LocalDate.of(1990, 1, 1)); 
        emp.setGender(Gender.Male);

        emp = employeeRepo.save(emp);
        employeeId = emp.getId();
    }


    /* ==================== Attestation Requests ==================== */

/*
import com.tarmiz.SIRH_backend.model.DTO.AttestationRequestDTO;

@Test
    public void testCreateAttestationRequest() throws Exception {
        String payload = String.format(
                "{\"employeeId\":%d,\"typeAttestation\":\"SALARY\",\"dateRequest\":\"2026-01-26\",\"dateSouhaitee\":\"2026-01-30\",\"note\":\"Test Note\"}",
                employeeId
        );

        mockMvc.perform(post("/attestationRequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(employeeId.intValue())))
                .andExpect(jsonPath("$.status", is("En attente")))
                .andExpect(jsonPath("$.typeAttestation", is("SALARY")));
    }

    @Test
    public void testGetAttestationRequest() throws Exception {
        // Create request first
        AttestationDTO created = objectMapper.readValue(
                mockMvc.perform(post("/attestationRequests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format(
                                        "{\"employeeId\":%d,\"typeAttestation\":\"SALARY\",\"dateRequest\":\"2026-01-26\",\"dateSouhaitee\":\"2026-01-30\",\"note\":\"Test Note\"}",
                                        employeeId
                                )))
                        .andReturn().getResponse().getContentAsString(),
                AttestationDTO.class
        );

        mockMvc.perform(get("/attestationRequests/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(created.getId().intValue())));
    }

    @Test
    public void testUpdateRequestStatus_Approved() throws Exception {
        AttestationDTO created = objectMapper.readValue(
                mockMvc.perform(post("/attestationRequests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format(
                                        "{\"employeeId\":%d,\"typeAttestation\":\"SALARY\",\"dateRequest\":\"2026-01-26\",\"dateSouhaitee\":\"2026-01-30\"}",
                                        employeeId
                                )))
                        .andReturn().getResponse().getContentAsString(),
                AttestationDTO.class
        );

        mockMvc.perform(put("/attestationRequests/" + created.getId())
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Approuvée")));
    }

    @Test
    public void testCancelAttestationRequest() throws Exception {
        AttestationDTO created = objectMapper.readValue(
                mockMvc.perform(post("/attestationRequests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format(
                                        "{\"employeeId\":%d,\"typeAttestation\":\"SALARY\",\"dateRequest\":\"2026-01-26\",\"dateSouhaitee\":\"2026-01-30\"}",
                                        employeeId
                                )))
                        .andReturn().getResponse().getContentAsString(),
                AttestationDTO.class
        );

        mockMvc.perform(delete("/attestationRequests/" + created.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/attestationRequests/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Annulée")));
    }

    /* ==================== Attestations ==================== */
/*
    @Test
    public void testCreateManualAttestation() throws Exception {
        AttestationDTO request = objectMapper.readValue(
                mockMvc.perform(post("/attestationRequests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format(
                                        "{\"employeeId\":%d,\"typeAttestation\":\"SALARY\",\"dateRequest\":\"2026-01-26\",\"dateSouhaitee\":\"2026-01-30\"}",
                                        employeeId
                                )))
                        .andReturn().getResponse().getContentAsString(),
                AttestationDTO.class
        );

        String payload = String.format(
                "{\"requestId\":%d,\"typeAttestation\":\"SALARY\",\"numero\":\"AT-2026-001\",\"dateGeneration\":\"2026-01-26\",\"notes\":\"Manual attestation\"}",
                request.getId()
        );

        mockMvc.perform(post("/attestations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId", is(request.getId().intValue())))
                .andExpect(jsonPath("$.numeroAttestation", is("AT-2026-001")));
    }

    @Test
    public void testGetAttestation() throws Exception {
        AttestationDTO request = objectMapper.readValue(
                mockMvc.perform(post("/attestationRequests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format(
                                        "{\"employeeId\":%d,\"typeAttestation\":\"SALARY\",\"dateRequest\":\"2026-01-26\",\"dateSouhaitee\":\"2026-01-30\"}",
                                        employeeId
                                )))
                        .andReturn().getResponse().getContentAsString(),
                AttestationDTO.class
        );

        AttestationRequestDTO attestation = objectMapper.readValue(
                mockMvc.perform(post("/attestations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format(
                                        "{\"requestId\":%d,\"typeAttestation\":\"SALARY\",\"numero\":\"AT-2026-001\",\"dateGeneration\":\"2026-01-26\"}",
                                        request.getId()
                                )))
                        .andReturn().getResponse().getContentAsString(),
                AttestationRequestDTO.class
        );

        mockMvc.perform(get("/attestations/" + attestation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(attestation.getId().intValue())));
    }
}
*/