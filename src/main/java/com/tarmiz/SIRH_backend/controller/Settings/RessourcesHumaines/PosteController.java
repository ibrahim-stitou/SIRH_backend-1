package com.tarmiz.SIRH_backend.controller.Settings.RessourcesHumaines;

import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.PosteDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.PosteDetailDTO;
import com.tarmiz.SIRH_backend.service.SettingsServices.JobServices.PosteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings/postes")
@Tag(name = "Postes", description = "API pour gérer les postes")
public class PosteController {

    private final PosteService service;

    public PosteController(PosteService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Liste tous les postes")
    public List<PosteDTO> list() { return service.listAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Détails d'un poste")
    public PosteDetailDTO details(@PathVariable Long id) { return service.getDetails(id); }

    @PostMapping
    @Operation(summary = "Créer un poste")
    public PosteDTO create(@RequestBody PosteDTO dto,
                           @RequestParam Long emploiId,
                           @RequestParam Long departementId) {
        return service.create(dto, emploiId, departementId);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Mettre à jour un poste")
    public PosteDTO update(@PathVariable Long id, @RequestBody PosteDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un poste")
    public void delete(@PathVariable Long id) { service.delete(id); }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activer un poste")
    public void activate(@PathVariable Long id) { service.activate(id); }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Désactiver un poste")
    public void deactivate(@PathVariable Long id) { service.deactivate(id); }
}
