package com.tarmiz.SIRH_backend.model.repository;

import com.tarmiz.SIRH_backend.enums.EntityType;
import com.tarmiz.SIRH_backend.enums.FilePurpose;
import com.tarmiz.SIRH_backend.model.entity.Files.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByEntityTypeAndEntityIdAndDeletedAtIsNull(
            EntityType entityType,
            Long entityId
    );

    List<File> findByEntityTypeAndEntityIdAndPurposeAndDeletedAtIsNull(
            EntityType entityType,
            Long entityId,
            FilePurpose purpose
    );
    Optional<File> findFirstByEntityTypeAndEntityIdAndPurposeAndDeletedAtIsNull(
            EntityType entityType,
            Long entityId,
            FilePurpose purpose
    );

    Optional<File> findByIdAndDeletedAtIsNull(Long id);
}