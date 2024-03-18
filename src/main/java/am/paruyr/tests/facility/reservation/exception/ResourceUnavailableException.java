package am.paruyr.tests.facility.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceUnavailableException extends ResourceAccessException {
  private static final String MESSAGE = "Resource with id:%s is in use";

  public ResourceUnavailableException(Long id) {
    super(String.format(MESSAGE, id));
  }
}
