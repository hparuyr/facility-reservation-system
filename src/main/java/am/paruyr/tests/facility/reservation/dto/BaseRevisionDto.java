package am.paruyr.tests.facility.reservation.dto;

import org.springframework.data.history.RevisionMetadata.RevisionType;

import lombok.Data;

@Data
public class BaseRevisionDto {
  private Integer revisionNumber;
  private RevisionType revisionType;
}
