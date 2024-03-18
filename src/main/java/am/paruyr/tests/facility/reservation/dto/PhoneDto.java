package am.paruyr.tests.facility.reservation.dto;

import static org.springframework.util.StringUtils.hasLength;

import java.util.ArrayList;
import java.util.List;
import am.paruyr.tests.facility.reservation.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {
  private Long id;
  private String brand;
  private String model;

  public static Phone to(PhoneDto phoneDto) {
    return new Phone(phoneDto.getId(), phoneDto.getBrand(), phoneDto.getModel());
  }

  public static List<String> validate(PhoneDto phoneDto) {
    var errors = new ArrayList<String>();
    if (!hasLength(phoneDto.getBrand())) {
      errors.add("Brand field must no be empty!");
    }
    if (!hasLength(phoneDto.getModel())) {
      errors.add("Model field must no be empty!");
    }

    return errors;
  }
}
