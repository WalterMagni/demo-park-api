package com.walter.demopark.service;

import com.walter.demopark.entity.Vaga;
import com.walter.demopark.exception.CodigoUniqueViolationException;
import com.walter.demopark.exception.EntityNotFoundException;
import com.walter.demopark.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
    public Vaga save(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException(String.format("O código %s já existe no sistema", vaga.getCodigo()));
        }
    }

    @Transactional(readOnly = true)
    public Vaga findByCodigo(String Codigo) {

        return vagaRepository.findByCodigo(Codigo).orElseThrow(
                () -> new EntityNotFoundException(String.format("Vaga %s não encontrada", Codigo)));
    }

}
