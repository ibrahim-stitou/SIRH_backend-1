package com.tarmiz.SIRH_backend.controller.Settings.Contract;

import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.TrialCriteriaUpdateDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.TrialCriteria;
import com.tarmiz.SIRH_backend.service.SettingsServices.TrialCriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings/conditions-periode-essaie")
@RequiredArgsConstructor
public class TrialCriteriaController {

    private final TrialCriteriaService service;

    /* ================= GET LIST ================= */
    @GetMapping
    public List<TrialCriteria> list() {
        return service.listAll();
    }

    /* ================= GET ONE ================= */
    @GetMapping("/{id}")
    public TrialCriteria get(@PathVariable Long id) {
        return service.getById(id);
    }

    /* ================= CREATE ================= */
    @PostMapping
    public TrialCriteria create(
            @RequestBody TrialCriteriaUpdateDTO dto
    ) {
        return service.create(dto);
    }

    /* ================= UPDATE ================= */
    @PutMapping("/{id}")
    public TrialCriteria update(
            @PathVariable Long id,
            @RequestBody TrialCriteriaUpdateDTO dto
    ) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public TrialCriteria patch(
            @PathVariable Long id,
            @RequestBody TrialCriteriaUpdateDTO dto
    ) {
        return service.update(id, dto);
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
