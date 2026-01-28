package com.tarmiz.SIRH_backend.controller;


import com.tarmiz.SIRH_backend.model.DTO.ApiListResponse;
import com.tarmiz.SIRH_backend.model.DTO.GroupListDTO;
import com.tarmiz.SIRH_backend.model.DTO.UpdateGroupMembersDTO;
import com.tarmiz.SIRH_backend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/groups")
public class GroupsController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/")
    public ApiListResponse<GroupListDTO> listGroups(
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "10") Integer length,
            @RequestParam(value = "dir", defaultValue = "desc") String dir,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "managerId", required = false) Long managerId,
            @RequestParam(value = "siegeId", required = false) Long siegeId
    ) {
        return groupService.getGroups(start, length, dir, name, code, managerId, siegeId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupDetails(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(groupService.getGroupDetails(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(groupService.getGroupMembers(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/members")
    public ResponseEntity<?> updateGroupMembers(@PathVariable Long id, @RequestBody UpdateGroupMembersDTO body) {
        try {
            return ResponseEntity.ok(groupService.updateGroupMembers(id, body.getAdd(), body.getRemove()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Group updated successfully",
                    "data", groupService.updateGroup(id, updates)
            ));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        try {
            groupService.deleteGroup(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Group deleted successfully"
            ));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getGroupByEmployee(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(groupService.getGroupByEmployee(employeeId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}