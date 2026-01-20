package com.tarmiz.SIRH_backend;

import com.tarmiz.SIRH_backend.model.entity.Employee;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepo;
import com.tarmiz.SIRH_backend.controller.EmployeeController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.restdocs.test.autoconfigure.AutoConfigureRestDocs;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class EmployeeControllerTest {

    private final MockMvc mockMvc;

    public EmployeeControllerTest(MockMvc mockMvc){
        this.mockMvc= mockMvc;
    }

    @MockBean
    private EmployeeRepo repository;

    @BeforeEach
    void setup() {
        Employee emp = new Employee();
        emp.setId(1L);
        emp.setMatricule("EMP001");
        emp.setFirstName("Tasniim");
        emp.setLastName("Chaouii");
        emp.setCin("AA123456");
        emp.setBirthDate(LocalDate.of(2001, 5, 12));
        emp.setGender("F");
        emp.setPhone("0600000000");
        emp.setEmail("tasniim@example.com");

        when(repository.findAll()).thenReturn(List.of(emp));
    }

    @Test
    void getAllEmployees_shouldGenerateDocs() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andDo(document("get-all-employees",
                        responseFields(
                                fieldWithPath("[].id").description("ID de l'employé"),
                                fieldWithPath("[].matricule").description("Matricule unique"),
                                fieldWithPath("[].firstName").description("Prénom de l'employé"),
                                fieldWithPath("[].lastName").description("Nom de l'employé"),
                                fieldWithPath("[].cin").description("CIN unique"),
                                fieldWithPath("[].birthDate").description("Date de naissance"),
                                fieldWithPath("[].gender").description("Genre"),
                                fieldWithPath("[].phone").description("Numéro de téléphone"),
                                fieldWithPath("[].email").description("Email")
                        )
                ));
    }
}