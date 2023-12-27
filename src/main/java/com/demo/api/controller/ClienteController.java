package com.demo.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.model.dto.ClienteDto;
import com.demo.api.model.entity.Cliente;
import com.demo.api.model.payload.MensajeResponse;
import com.demo.api.service.IClienteService;

import java.util.*;;

@RestController
@RequestMapping(path = "/api/v1")
public class ClienteController {
    
    @Autowired
    private IClienteService clienteService;

    @PostMapping(path = "/cliente")
    // @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody ClienteDto clienteDto) {
        Cliente cliSave = null;
        try {
            cliSave = clienteService.save(clienteDto);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Guardado correctamente")
                    .object(ClienteDto.builder()
                            .idCliente(cliSave.getIdCliente())
                            .nombre(cliSave.getNombre())
                            .apellido(cliSave.getApellido())
                            .correo(cliSave.getCorreo())
                            .fechaRegistro(cliSave.getFechaRegistro())
                            .build())
                    .build(), HttpStatus.CREATED);    
        } catch (DataAccessException e) {
            return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje(e.getMessage())
                        .object(null).build(), 
                HttpStatus.METHOD_NOT_ALLOWED);
        }
        

        
    }

    @PutMapping(path = "/cliente")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@RequestBody ClienteDto cliente) {
        Cliente cliUpdate = null;

        try {
            if (!clienteService.existsById(cliente.getIdCliente())) {
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Cliente no existe, no se puede actualizar")
                        .object(null).build(), 
                HttpStatus.METHOD_NOT_ALLOWED);
            }
            cliUpdate = clienteService.save(cliente);    
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Actualizado correctamente")
                    .object(ClienteDto.builder()
                            .idCliente(cliUpdate.getIdCliente())
                            .nombre(cliUpdate.getNombre())
                            .apellido(cliUpdate.getApellido())
                            .correo(cliUpdate.getCorreo())
                            .fechaRegistro(cliUpdate.getFechaRegistro())
                            .build())
                    .build(), HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje(e.getMessage())
                        .object(null).build(), 
                HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @DeleteMapping(path = "/cliente/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            Cliente cli = clienteService.findById(id);
            clienteService.delete(cli);
            return new ResponseEntity<>(cli, HttpStatus.NO_CONTENT);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje(e.getMessage())
                        .object(null).build(), 
                HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping(path = "cliente/{id}")
    // @ResponseStatus(HttpStatus.OK)  // Solo si no hay validaciones
    public ResponseEntity<?> showById(@PathVariable Integer id){
        Cliente cli = clienteService.findById(id);
        if (cli == null) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Cliente no existe")
                    .object(null).build(), 
                HttpStatus.NOT_FOUND);
        } 

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("")
                .object(ClienteDto.builder()
                        .idCliente(cli.getIdCliente())
                        .nombre(cli.getNombre())
                        .apellido(cli.getApellido())
                        .correo(cli.getCorreo())
                        .fechaRegistro(cli.getFechaRegistro())
                        .build())
                .build(), HttpStatus.OK);
    }

    @GetMapping("/clientes")
    public ResponseEntity<?> showAll() {
        Iterable<Cliente> clientes = clienteService.findAll();
        // Controlar cuando no existan registros
        return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("")
                    .object(clientes).build(), 
                HttpStatus.OK);
    }
}
