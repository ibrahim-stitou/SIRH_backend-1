package com.tarmiz.SIRH_backend.service;

import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.mapper.ContractListMapper;
import com.tarmiz.SIRH_backend.model.DTO.ContractListDTO;
import com.tarmiz.SIRH_backend.model.entity.Contract.Contract;
import com.tarmiz.SIRH_backend.model.entity.Files.File;
import com.tarmiz.SIRH_backend.model.repository.ContractRepository;
import com.tarmiz.SIRH_backend.model.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final FileRepository fileRepository;
    private final ContractListMapper contractListMapper;

    public ContractListDTO getContract(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        File signedFile = fileRepository
                .findFirstByEntityTypeAndEntityIdAndPurposeAndDeletedAtIsNull(
                        EntityType.CONTRACT,
                        contract.getId(),
                        FilePurpose.SIGNED_CONTRACT
                )
                .orElse(null);

        return contractListMapper.toListDTO(contract, signedFile);
    }


}
