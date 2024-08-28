package com.walter.demopark.repository;

import com.walter.demopark.entity.Cliente;
import com.walter.demopark.repository.projection.ClienteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c")
    Page<ClienteProjection> findAllPageable(Pageable pageable);

}
