package com.tarmiz.SIRH_backend.controller;

import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.GroupListDTO;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.SiegeCreateDTO;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.SiegeListDTO;
import com.tarmiz.SIRH_backend.model.DTO.CompanyHierarchyDTOs.SiegeWithGroupsDTO;
import com.tarmiz.SIRH_backend.service.GroupService;
import com.tarmiz.SIRH_backend.service.SiegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sieges")
public class SiegeController {

    @Autowired
    private SiegeService siegeService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ApiListResponse<SiegeListDTO> listSieges(
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "10") Integer length,
            @RequestParam(value = "dir", defaultValue = "desc") String dir,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "country", required = false) String country
    ) {
        return siegeService.getSieges(start, length, dir, name, city, country);
    }

    /** ================= GET /siege detailed infos ================= */

    @GetMapping("/{id}")
    public SiegeListDTO getSiegeDetails(@PathVariable Long id) {
        return siegeService.getSiegeDetails(id);
    }

    /** ================= POST /sieges ================= */
    @PostMapping
    public ResponseEntity<ApiListResponse<SiegeListDTO>> createSiege(@RequestBody SiegeCreateDTO dto) {
        SiegeListDTO created = siegeService.createSiege(dto);

        ApiListResponse<SiegeListDTO> response = new ApiListResponse<>();
        response.setStatus("success");
        response.setMessage("Création réussie");
        response.setData(java.util.List.of(created));

        return ResponseEntity.ok(response);
    }

    /** ================= Get /Groups by siege ================= */
    @GetMapping("/{siegeId}/groups")
        public ApiListResponse<SiegeWithGroupsDTO> listGroupsBySiege(
            @PathVariable Long siegeId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code
    ) {
        return groupService.getGroupsBySiege(siegeId, name, code);
    }

    /** ================= Soft Delete siege ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSiege(@PathVariable Long id) {
        siegeService.deleteSiege(id);
        Map<String, String> response = Map.of("status", "success", "message", "Siège supprimé avec succès");
        return ResponseEntity.ok(response);
    }

}