package com.demo.api.service;

import com.demo.api.model.dto.ClienteDto;
import com.demo.api.model.entity.Cliente;

public interface IClienteService {
    
    Cliente save(ClienteDto cliente);

    Cliente findById(Integer id);

    void delete(Cliente cliente);

    boolean existsById(Integer id);

    Iterable<Cliente> findAll();  // Hacerlo con el ClienteDto
}
