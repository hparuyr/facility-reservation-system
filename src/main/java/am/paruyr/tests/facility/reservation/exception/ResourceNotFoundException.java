package am.paruyr.tests.facility.reservation.exception;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ResourceAccessException {
  private static final String MESSAGE = "Resource with id:%s is not found";

  public ResourceNotFoundException(Long id) {
    super(format(MESSAGE, id));
  }
}
