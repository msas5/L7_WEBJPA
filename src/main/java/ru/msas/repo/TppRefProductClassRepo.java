package ru.msas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.msas.entity.TppRefProductClass;
import java.util.List;

public interface TppRefProductClassRepo extends JpaRepository<TppRefProductClass,Long> {

    List<TppRefProductClass>  findByValue(String value);
}
