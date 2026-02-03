package com.tarmiz.SIRH_backend.controller.Settings.RessourcesHumaines;

import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.MetierDTO;
import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.JobDTOs.MetierDetailDTO;
import com.tarmiz.SIRH_backend.service.SettingsServices.JobServices.MetierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings/metiers")
public class MetierController {

    private final MetierService metierService;

    public MetierController(MetierService metierService) {
        this.metierService = metierService;
    }

    @GetMapping
    public List<MetierDTO> list() {
        return metierService.listAll();
    }

    @GetMapping("/{id}")
    public MetierDetailDTO details(@PathVariable Long id) {
        return metierService.getDetails(id);
    }

    @PostMapping
    public MetierDTO create(@RequestBody MetierDTO dto) {
        return metierService.create(dto);
    }

    @PatchMapping("/{id}")
    public MetierDTO update(@PathVariable Long id, @RequestBody MetierDTO dto) {
        return metierService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        metierService.delete(id);
    }

    @PatchMapping("/{id}/activate")
    public void activate(@PathVariable Long id) {
        metierService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        metierService.deactivate(id);
    }
}
