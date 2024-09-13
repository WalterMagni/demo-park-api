package com.walter.demopark.web.exception;

import com.walter.demopark.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Classe ApiExceptionHandler que atua como um manipulador global de exceções em uma API REST.
 * A anotação @RestControllerAdvice permite interceptar exceções lançadas nos controladores REST,
 * capturá-las e fornecer uma resposta apropriada, com mensagens de erro detalhadas e status HTTP específicos.
 * A anotação @Slf4j é usada para habilitar o registro de logs via o framework Lombok.
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Manipulador de exceção para AccessDeniedException.
     * Essa exceção é lançada quando um usuário tenta acessar um recurso para o qual ele não tem permissão.
     *
     * @param ex A exceção de acesso negado.
     * @param request O objeto HttpServletRequest da requisição que gerou a exceção.
     * @return Um ResponseEntity com status HTTP 403 (FORBIDDEN) e uma mensagem de erro em formato JSON.
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    /**
     * Manipulador de exceção para MethodArgumentNotValidException.
     * Essa exceção ocorre quando um argumento de método anotado com @Valid falha na validação.
     *
     * @param ex A exceção MethodArgumentNotValidException.
     * @param request O objeto HttpServletRequest da requisição que gerou a exceção.
     * @param result O objeto BindingResult contendo detalhes sobre os erros de validação.
     * @return Um ResponseEntity com status HTTP 422 (UNPROCESSABLE_ENTITY) e uma mensagem de erro em formato JSON.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                        HttpServletRequest request,
                                                                        BindingResult result) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Parâmetros inválidos", result));
    }

    /**
     * Manipulador de exceção para violações de unicidade, como nome de usuário, CPF ou código já existente.
     * As exceções personalizadas UsernameUniqueViolationException, CpfUniqueViolationException e CodigoUniqueViolationException
     * são tratadas aqui.
     *
     * @param ex A exceção lançada em caso de violação de unicidade.
     * @param request O objeto HttpServletRequest da requisição que gerou a exceção.
     * @return Um ResponseEntity com status HTTP 409 (CONFLICT) e uma mensagem de erro em formato JSON.
     */
    @ExceptionHandler({UsernameUniqueViolationException.class, CpfUniqueViolationException.class, CodigoUniqueViolationException.class})
    public ResponseEntity<ErrorMessage> uniqueViolationException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
    }

    /**
     * Manipulador de exceção para EntityNotFoundException.
     * Essa exceção ocorre quando uma entidade não é encontrada no banco de dados.
     *
     * @param ex A exceção lançada quando a entidade não é encontrada.
     * @param request O objeto HttpServletRequest da requisição que gerou a exceção.
     * @return Um ResponseEntity com status HTTP 404 (NOT_FOUND) e uma mensagem de erro em formato JSON.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    /**
     * Manipulador de exceção para PasswordInvalidException.
     * Essa exceção é lançada quando uma senha fornecida é inválida ou não coincide com a confirmação de senha.
     *
     * @param ex A exceção PasswordInvalidException.
     * @param request O objeto HttpServletRequest da requisição que gerou a exceção.
     * @return Um ResponseEntity com status HTTP 400 (BAD_REQUEST) e uma mensagem de erro em formato JSON.
     */
    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> passwordInvalidException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> internalServerErrorException(Exception ex, HttpServletRequest request) {
        ErrorMessage error = new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        log.error("Internal Server Error {} {} - ", error, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }
}

