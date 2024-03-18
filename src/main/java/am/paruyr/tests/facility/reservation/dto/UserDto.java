package am.paruyr.tests.facility.reservation.dto;

import lombok.Data;

@Data
public class UserDto {
  public static final String ERROR_MESSAGE_USERNAME_MUST_NOT_BE_EMPTY = "Username field must no be empty!";

  private String username;
}
