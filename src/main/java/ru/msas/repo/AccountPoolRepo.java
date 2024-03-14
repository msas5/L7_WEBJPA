package ru.msas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.msas.entity.AccountPool;

import java.util.List;

public interface AccountPoolRepo extends JpaRepository<AccountPool,Long> {

    List<AccountPool> findTopByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCodeOrderByIdAsc(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode);

}
