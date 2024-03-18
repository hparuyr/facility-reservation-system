package am.paruyr.tests.facility.reservation.rest;

import static java.lang.String.format;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import am.paruyr.tests.facility.reservation.FacilityReservationApplication;
import am.paruyr.tests.facility.reservation.config.SecurityConfig;
import am.paruyr.tests.facility.reservation.exception.ResourceNotBookedException;
import am.paruyr.tests.facility.reservation.exception.ResourceNotFoundException;
import am.paruyr.tests.facility.reservation.exception.ResourceUnavailableException;
import am.paruyr.tests.facility.reservation.service.impl.PhoneServiceImpl;
import lombok.SneakyThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PhoneBookingRestService.class)
@ContextConfiguration(classes = {FacilityReservationApplication.class, SecurityConfig.class})
class PhoneBookingRestServiceTest {
  private static final String BOOKING_PHONE_REQUEST_BODY = "{\"username\": \"%s\"}";
  private static final String PATH_PHONES = "/phones";
  private static final String PATH_PHONE_ID = PATH_PHONES + "/%s";
  private static final String PATH_BOOK_PHONE = PATH_PHONE_ID + "/book";
  private static final String PATH_PHONE_RETURN = PATH_PHONE_ID + "/return";
  private static final String PATH_PHONE_HISTORY = PATH_PHONE_ID + "/history";

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private PhoneServiceImpl phoneService;

  @SneakyThrows
  @Test
  void getAllPhones_correctRequest_return200() {
    mockMvc.perform(get(PATH_PHONES)).andExpect(status().isOk());
    verify(phoneService).getAllResources();
  }

  @SneakyThrows
  @Test
  void getPhoneById_correctRequest_return200() {
    mockMvc.perform(get(format(PATH_PHONE_ID, 1))).andExpect(status().isOk());
    verify(phoneService).getResourceById(1L);
  }

  @SneakyThrows
  @Test
  void getPhoneById_wrongId_return404() {
    when(phoneService.getResourceById(2L)).thenThrow(ResourceNotFoundException.class);

    mockMvc.perform(get(format(PATH_PHONE_ID, 2))).andExpect(status().isNotFound());
  }

  @SneakyThrows
  @Test
  void bookPhone_correctRequest_return200() {
    mockMvc.perform(
            post(format(PATH_BOOK_PHONE, 1)).content(format(BOOKING_PHONE_REQUEST_BODY, "Tester"))
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(phoneService).bookResource(1L, "Tester");
  }

  @SneakyThrows
  @Test
  void bookPhone_emptyUsername_return400() {
    mockMvc.perform(
            post(format(PATH_BOOK_PHONE, 1)).content(format(BOOKING_PHONE_REQUEST_BODY, "")).contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  void bookPhone_wrongId_return404() {
    doThrow(ResourceNotFoundException.class).when(phoneService).bookResource(2L, "Tester");

    mockMvc.perform(
            post(format(PATH_BOOK_PHONE, 2)).content(format(BOOKING_PHONE_REQUEST_BODY, "Tester"))
                .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @SneakyThrows
  @Test
  void bookPhone_alreadyBookedPhone_return409() {
    doThrow(ResourceUnavailableException.class).when(phoneService).bookResource(3L, "Tester");

    mockMvc.perform(
            post(format(PATH_BOOK_PHONE, 3)).content(format(BOOKING_PHONE_REQUEST_BODY, "Tester"))
                .contentType(APPLICATION_JSON))
        .andExpect(status().isConflict());
  }

  @SneakyThrows
  @Test
  void returnPhone_correctRequest_return200() {
    mockMvc.perform(post(format(PATH_PHONE_RETURN, 1))).andExpect(status().isOk());

    verify(phoneService).returnResource(1L);
  }

  @SneakyThrows
  @Test
  void returnPhone_notBookedPhone_return400() {
    doThrow(ResourceNotBookedException.class).when(phoneService).returnResource(4L);

    mockMvc.perform(post(format(PATH_PHONE_RETURN, 4))).andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  void returnPhone_wrongId_return404() {
    doThrow(ResourceNotFoundException.class).when(phoneService).returnResource(2L);

    mockMvc.perform(post(format(PATH_PHONE_RETURN, 2))).andExpect(status().isNotFound());
  }

  @SneakyThrows
  @Test
  void getPhoneChangeHistory_correctRequest_return200() {
    mockMvc.perform(get(format(PATH_PHONE_HISTORY, 1))).andExpect(status().isOk());

    verify(phoneService).getResourceChangeHistory(1L);
  }

  @SneakyThrows
  @Test
  void getPhoneChangeHistory_wrongId_return404() {
    when(phoneService.getResourceChangeHistory(2L)).thenThrow(ResourceNotFoundException.class);

    mockMvc.perform(get(format(PATH_PHONE_HISTORY, 2))).andExpect(status().isNotFound());
  }
}