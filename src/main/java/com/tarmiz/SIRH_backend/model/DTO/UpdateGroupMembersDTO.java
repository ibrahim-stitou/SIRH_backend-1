package com.tarmiz.SIRH_backend.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateGroupMembersDTO {
    private List<Long> add;
    private List<Long> remove;
}
