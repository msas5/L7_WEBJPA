package ru.msas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.msas.entity.Account;

import java.util.List;

public interface AccountRepo extends JpaRepository<Account,Long> {

      List<Account> findByAccountPoolIdAndBussyOrderByIdAsc(Long accountPoolId, Boolean bussy);
}
