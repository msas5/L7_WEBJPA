package ru.msas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.msas.entity.Agreement;

import java.util.List;

public interface AgreementRepo extends JpaRepository<Agreement,Long> {

    @Query("select count(*) from #{#entityName} where number in :numberList")
    Integer CountByNumberIn(@Param("numberList")List<String> lst);
}
