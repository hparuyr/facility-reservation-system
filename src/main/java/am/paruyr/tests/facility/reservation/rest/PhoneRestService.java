package am.paruyr.tests.facility.reservation.rest;

import static am.paruyr.tests.facility.reservation.dto.PhoneDto.validate;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import am.paruyr.tests.facility.reservation.dto.PhoneDto;
import am.paruyr.tests.facility.reservation.service.PhoneManagementService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/private/phone")
@Tag(name = "Phone Services", description = "Phone Management Api")
public class PhoneRestService {
  private final PhoneManagementService managementService;

  @Operation(
      summary = "Create new phone",
      description = "Create phone with new brand and model")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation"),
      @ApiResponse(responseCode = "400", description = "brand or model are empty"),
      @ApiResponse(responseCode = "401", description = "request is not authorized"),
  })
  @PostMapping
  public ResponseEntity<List<String>> createPhone(@RequestBody PhoneDto phoneDto) {
    var errors = validate(phoneDto);
    if (!errors.isEmpty()) {
      return badRequest().body(errors);
    }

    managementService.createOrUpdatePhone(phoneDto);
    return ok().build();
  }

  @Operation(
      summary = "Update phone data",
      description = "Update given phone brand/model by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation"),
      @ApiResponse(responseCode = "400", description = "brand or model are empty"),
      @ApiResponse(responseCode = "401", description = "request is not authorized"),
      @ApiResponse(responseCode = "404", description = "phone not found")
  })
  @PutMapping("/{id}")
  public ResponseEntity<List<String>> updatePhone(@PathVariable Long id, @RequestBody PhoneDto phone) {
    var errors = validate(phone);
    if (!errors.isEmpty()) {
      return badRequest().body(errors);
    }
    phone.setId(id);
    managementService.createOrUpdatePhone(phone);
    return ok().build();
  }

  @Operation(
      summary = "Permanently delete the phone",
      description = "Deletes given phone by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation"),
      @ApiResponse(responseCode = "400", description = "phone is in use"),
      @ApiResponse(responseCode = "401", description = "request is not authorized"),
      @ApiResponse(responseCode = "404", description = "phone not found")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePhone(@PathVariable Long id) {
    managementService.deletePhone(id);
    return ok().build();
  }
}
