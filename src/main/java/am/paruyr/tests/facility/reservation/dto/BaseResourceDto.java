package am.paruyr.tests.facility.reservation.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BaseResourceDto {
  private String bookedBy;
  private LocalDateTime bookedSince;
  private boolean available;
  private LocalDateTime updatedOn;
}
