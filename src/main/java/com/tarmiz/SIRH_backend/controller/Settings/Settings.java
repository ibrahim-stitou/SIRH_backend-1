package com.tarmiz.SIRH_backend.controller.Settings;

import com.tarmiz.SIRH_backend.model.DTO.ParamsDTOs.ManagersDTO;
import com.tarmiz.SIRH_backend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Settings {
    private final GroupService managerService;

    @RequestMapping("/settings/managers")
    @GetMapping
    public List<ManagersDTO> getAllManagers() {
        return managerService.getAllManagers();
    }
}
