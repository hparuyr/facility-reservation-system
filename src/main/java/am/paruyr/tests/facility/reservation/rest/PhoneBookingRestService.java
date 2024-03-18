package am.paruyr.tests.facility.reservation.rest;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.StringUtils.hasLength;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import am.paruyr.tests.facility.reservation.dto.BaseResourceDto;
import am.paruyr.tests.facility.reservation.dto.BaseRevisionDto;
import am.paruyr.tests.facility.reservation.dto.UserDto;
import am.paruyr.tests.facility.reservation.service.BookableResourceService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/phones")
@Tag(name = "Phone Booking Services", description = "Phone Booking and Return Management Api")
public class PhoneBookingRestService {
  private final BookableResourceService bookableResourceService;

  @Operation(
      summary = "Fetch all phones",
      description = "Fetches all phone entities and their booking information")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation")
  })
  @GetMapping
  public BaseResourceDto[] getAllPhones() {
    return bookableResourceService.getAllResources();
  }

  @Operation(
      summary = "Fetch the phone",
      description = "Fetches the given phone and booking information")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation"),
      @ApiResponse(responseCode = "404", description = "phone not found")
  })
  @GetMapping("/{id}")
  public BaseResourceDto getPhoneById(@PathVariable Long id) {
    return bookableResourceService.getResourceById(id);
  }

  @Operation(
      summary = "Book the phone",
      description = "Book the given phone by id and username")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation"),
      @ApiResponse(responseCode = "400", description = "username is empty"),
      @ApiResponse(responseCode = "404", description = "phone not found"),
      @ApiResponse(responseCode = "409", description = "phone is already booked")
  })
  @PostMapping("/{id}/book")
  public ResponseEntity<String> bookPhone(@PathVariable Long id, @RequestBody UserDto user) {
    if (hasLength(user.getUsername())) {
      bookableResourceService.bookResource(id, user.getUsername());
      return ok().build();
    }

    return badRequest().body(UserDto.ERROR_MESSAGE_USERNAME_MUST_NOT_BE_EMPTY);
  }

  @Operation(
      summary = "Return the phone",
      description = "Returns the given phone by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation"),
      @ApiResponse(responseCode = "400", description = "phone is not booked"),
      @ApiResponse(responseCode = "404", description = "phone not found")
  })
  @PostMapping("/{id}/return")
  public ResponseEntity<Void> returnPhone(@PathVariable Long id) {
    bookableResourceService.returnResource(id);
    return ok().build();
  }

  @Operation(
      summary = "Show history of operations",
      description = "Shows phone and it's booking information historical data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation"),
      @ApiResponse(responseCode = "404", description = "phone not found")
  })
  @GetMapping("/{id}/history")
  public ResponseEntity<BaseRevisionDto[]> getPhoneChangeHistory(@PathVariable Long id) {
    return ok(bookableResourceService.getResourceChangeHistory(id));
  }
}
