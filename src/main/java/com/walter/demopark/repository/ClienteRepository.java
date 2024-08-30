package com.walter.demopark.repository;

import com.walter.demopark.entity.Cliente;
import com.walter.demopark.repository.projection.ClienteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c")
    Page<ClienteProjection> findAllPageable(Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.usuario.id = :id")
    Cliente buscarPorClienteId(@Param("id") Long id);
}
