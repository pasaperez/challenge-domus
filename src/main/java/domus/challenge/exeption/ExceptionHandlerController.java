package domus.challenge.exeption;

import domus.challenge.dto.ErrorMessageDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler({BadRequestException.class, MissingServletRequestParameterException.class})
  public ResponseEntity<ErrorMessageDTO> parameterError(Exception e) {
    return ResponseEntity.badRequest()
            .body(new ErrorMessageDTO("Required parameter missing", e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorMessageDTO> serviceError(Exception e) {
    return ResponseEntity.internalServerError()
            .body(new ErrorMessageDTO("Error consuming service", e.getMessage(), HttpStatus.BAD_GATEWAY, LocalDateTime.now()));
  }
}
