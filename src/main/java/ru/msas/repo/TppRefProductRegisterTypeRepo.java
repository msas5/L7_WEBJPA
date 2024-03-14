package ru.msas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.msas.entity.TppRefProductRegisterType;

import java.util.List;

public interface TppRefProductRegisterTypeRepo extends JpaRepository<TppRefProductRegisterType,Long> {


   List<TppRefProductRegisterType> findByProductClassCodeAndAccountType(String productClassCode, String accountType);

   List<TppRefProductRegisterType> findByValue(String value);
}
