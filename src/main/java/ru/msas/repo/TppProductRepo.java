package ru.msas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.msas.entity.TppProduct;

import java.util.Optional;


public interface TppProductRepo extends JpaRepository<TppProduct,Long> {


      @Query("select count(*) from #{#entityName} u where u.number = :contractnumber")
       int findByNumber(@Param("contractnumber") String number);

      Optional<TppProduct> findById(Long id );


}
