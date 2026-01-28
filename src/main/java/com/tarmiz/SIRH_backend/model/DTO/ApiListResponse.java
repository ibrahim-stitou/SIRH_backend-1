package com.tarmiz.SIRH_backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiListResponse<T> {
    private String status;            // e.g., "success" or "error"
    private String message;           // human-readable message
    private List<T> data;             // actual list of DTOs
    private long recordsTotal;        // total number of records in DB
    private long recordsFiltered;     // filtered records count (after filters)
}