package com.tarmiz.SIRH_backend.controller.Settings.RessourcesHumaines;

import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.EmploiDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.EmploiDetailDTO;
import com.tarmiz.SIRH_backend.service.SettingsServices.JobServices.EmploiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings/emplois")
@Tag(name = "Emplois", description = "API pour gérer les emplois")
public class EmploiController {

    private final EmploiService service;

    public EmploiController(EmploiService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Liste tous les emplois")
    public List<EmploiDTO> list() { return service.listAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Détails d'un emploi")
    public EmploiDetailDTO details(@PathVariable Long id) { return service.getDetails(id); }

    @PostMapping
    @Operation(summary = "Créer un emploi")
    public EmploiDTO create(@RequestBody EmploiDTO dto, @RequestParam Long metierId) {
        return service.create(dto, metierId);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Mettre à jour un emploi")
    public EmploiDTO update(@PathVariable Long id, @RequestBody EmploiDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un emploi")
    public void delete(@PathVariable Long id) { service.delete(id); }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activer un emploi")
    public void activate(@PathVariable Long id) { service.activate(id); }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Désactiver un emploi")
    public void deactivate(@PathVariable Long id) { service.deactivate(id); }
}
