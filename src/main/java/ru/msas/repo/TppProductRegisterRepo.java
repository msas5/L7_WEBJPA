package ru.msas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.msas.entity.TppProductRegister;
import java.util.List;

public interface TppProductRegisterRepo extends JpaRepository<TppProductRegister,Long> {

    List<TppProductRegister> findByProductIdAndType(Long productId,String type);
}
