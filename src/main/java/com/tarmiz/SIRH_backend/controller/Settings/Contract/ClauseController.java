package com.tarmiz.SIRH_backend.controller.Settings.Contract;


import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.ClauseUpdateDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.Clause;
import com.tarmiz.SIRH_backend.service.SettingsServices.ClauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings/conditions-contrat")
@RequiredArgsConstructor
public class ClauseController {

    private final ClauseService clauseService;

    /* ================= GET LIST ================= */
    @GetMapping
    public List<Clause> list() {
        return clauseService.listAll();
    }

    /* ================= CREATE ================= */
    @PostMapping
    public Clause create(@RequestBody ClauseUpdateDTO dto) {
        return clauseService.create(dto);
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public Clause update(
            @PathVariable Long id,
            @RequestBody ClauseUpdateDTO dto
    ) {
        return clauseService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public Clause patch(
            @PathVariable Long id,
            @RequestBody ClauseUpdateDTO dto
    ) {
        return clauseService.update(id, dto);
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clauseService.delete(id);
    }
}