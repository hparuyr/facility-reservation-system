package am.paruyr.tests.facility.reservation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.data.history.Revision;

import am.paruyr.tests.facility.reservation.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public class PhoneRevisionDto extends BaseRevisionDto {
  private BookPhoneDto phone;

  public static PhoneRevisionDto from(Revision<Integer, Phone> revision) {
    var revisionDto = new PhoneRevisionDto(BookPhoneDto.from(revision.getEntity()));
    revisionDto.setRevisionType(revision.getMetadata().getRevisionType());
    revisionDto.setRevisionNumber(revision.getMetadata().getRevisionNumber().orElseGet(null));
    return revisionDto;
  }
}
