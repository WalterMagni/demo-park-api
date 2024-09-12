package com.walter.demopark.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe ErrorMessage que representa um objeto de resposta padronizado para erros em uma API REST.
 * Ela é utilizada para enviar detalhes sobre erros ocorridos nas requisições, incluindo informações como o caminho da requisição,
 * o método HTTP, o status do erro e uma mensagem descritiva.
 * Além disso, pode incluir uma lista de erros específicos em casos de validação de campos.
 */
public class ErrorMessage {

    /**
     * O caminho da requisição onde ocorreu o erro.
     */
    private String path;

    /**
     * O método HTTP utilizado na requisição (GET, POST, etc.).
     */
    private String method;

    /**
     * O código de status HTTP associado ao erro (por exemplo, 404, 400, 500).
     */
    private int status;

    /**
     * O texto associado ao status HTTP (por exemplo, "Not Found", "Bad Request").
     */
    private String statusText;

    /**
     * A mensagem descritiva do erro.
     */
    private String message;

    /**
     * Um mapa opcional que contém os erros de validação de campos.
     * Cada chave no mapa representa o nome do campo que falhou na validação, e o valor representa a mensagem de erro correspondente.
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Map<String, String> errors; // <field, message>

    /**
     * Construtor padrão sem argumentos.
     * Necessário para a serialização/deserialização do objeto ErrorMessage.
     */
    public ErrorMessage() {}

    /**
     * Construtor que inicializa o objeto ErrorMessage com os detalhes do erro.
     *
     * @param request O objeto HttpServletRequest da requisição que gerou o erro.
     * @param status O status HTTP associado ao erro.
     * @param message A mensagem descritiva do erro.
     */
    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
    }

    /**
     * Construtor que inicializa o objeto ErrorMessage com os detalhes do erro e os erros de validação de campos.
     *
     * @param request O objeto HttpServletRequest da requisição que gerou o erro.
     * @param status O status HTTP associado ao erro.
     * @param message A mensagem descritiva do erro.
     * @param bindingResult O objeto BindingResult contendo detalhes sobre os erros de validação.
     */
    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult bindingResult) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
        addErrors(bindingResult); // Adiciona os erros de validação dos campos
    }

    /**
     * Método privado que extrai os erros de validação de campos do BindingResult e os adiciona ao mapa 'errors'.
     *
     * @param bindingResult O objeto BindingResult que contém os erros de validação de campos.
     */
    private void addErrors(BindingResult bindingResult) {
        this.errors = new HashMap<>(); // Inicializa o mapa de erros

        bindingResult.getFieldErrors().forEach(error -> {
            this.errors.put(error.getField(), error.getDefaultMessage()); // Adiciona o campo e sua mensagem de erro
        });
    }
}

