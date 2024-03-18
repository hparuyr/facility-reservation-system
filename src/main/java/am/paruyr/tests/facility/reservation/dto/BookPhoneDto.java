package am.paruyr.tests.facility.reservation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import am.paruyr.tests.facility.reservation.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public class BookPhoneDto extends BaseResourceDto {
  private Long id;
  private String brand;
  private String model;

  public static BookPhoneDto from(Phone phone) {
    var bookPhoneDto = new BookPhoneDto(phone.getId(), phone.getBrand(), phone.getModel());
    bookPhoneDto.setBookedBy(phone.getBookedBy());
    bookPhoneDto.setBookedSince(phone.getBookedSince());
    bookPhoneDto.setAvailable(phone.isAvailable());
    bookPhoneDto.setUpdatedOn(phone.getUpdatedOn());
    return bookPhoneDto;
  }
}
