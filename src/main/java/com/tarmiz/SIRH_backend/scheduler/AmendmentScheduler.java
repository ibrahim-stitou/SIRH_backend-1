package com.tarmiz.SIRH_backend.scheduler;

import com.tarmiz.SIRH_backend.enums.Contract.AmendmentStatus;
import com.tarmiz.SIRH_backend.model.entity.Contract.Amendment;
import com.tarmiz.SIRH_backend.model.repository.ContractRepos.AmendmentRepository;
import com.tarmiz.SIRH_backend.service.Amendment.AmendmentApplicationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AmendmentScheduler {

    private final AmendmentRepository amendmentRepository;
    private final AmendmentApplicationService amendmentApplicationService;

    @Scheduled(cron = "0 0 1 * * *") // tous les jours à 01:00
    @Transactional
    public void applyEffectiveAmendments() {

        LocalDate today = LocalDate.now();

        List<Amendment> amendments =
                amendmentRepository.findByStatusAndEffectiveDateLessThanEqual(
                        AmendmentStatus.VALIDE,
                        today
                );

        for (Amendment amendment : amendments) {
            amendmentApplicationService.applyLines(amendment);
            amendment.setStatus(AmendmentStatus.APPLIQUE);
        }
    }
}