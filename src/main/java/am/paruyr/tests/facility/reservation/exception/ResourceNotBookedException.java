package am.paruyr.tests.facility.reservation.exception;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotBookedException extends ResourceAccessException {
  private static final String MESSAGE = "Resource with id:%s is not booked";

  public ResourceNotBookedException(Long id) {
    super(format(MESSAGE, id));
  }
}
