package am.paruyr.tests.facility.reservation.rest;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import am.paruyr.tests.facility.reservation.FacilityReservationApplication;
import am.paruyr.tests.facility.reservation.config.SecurityConfig;
import am.paruyr.tests.facility.reservation.exception.ResourceNotFoundException;
import am.paruyr.tests.facility.reservation.service.impl.PhoneServiceImpl;
import lombok.SneakyThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PhoneRestService.class)
@ContextConfiguration(classes = {FacilityReservationApplication.class, SecurityConfig.class})
class PhoneRestServiceTest {
  private static final String CREATE_PHONE_REQUEST_BODY = "{\"brand\": \"%s\", \"model\": \"%s\"}";
  private static final String AUTHORIZATION = "Basic " + new String(
      Base64.getEncoder().encode(("admin:password").getBytes()));
  private static final String PATH_PHONE_PRIVATE = "/private/phone";
  private static final String PATH_PHONE_ID_PRIVATE = PATH_PHONE_PRIVATE + "/%s";

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private PhoneServiceImpl phoneService;

  @SneakyThrows
  @Test
  void createPhone_correctRequest_return200() {
    mockMvc.perform(post(PATH_PHONE_PRIVATE)
            .header("Authorization", AUTHORIZATION)
            .content(format(CREATE_PHONE_REQUEST_BODY, "Apple", "iPhone 12"))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(phoneService).createOrUpdatePhone(any());
  }

  @SneakyThrows
  @Test
  void createPhone_unauthorizedRequest_return401() {
    mockMvc.perform(post(PATH_PHONE_PRIVATE)
            .content(format(CREATE_PHONE_REQUEST_BODY, "Apple", "iPhone 12"))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @SneakyThrows
  @Test
  void createPhone_emptyBrand_return400() {
    mockMvc.perform(post(PATH_PHONE_PRIVATE)
            .header("Authorization", AUTHORIZATION)
            .content(format(CREATE_PHONE_REQUEST_BODY, "", "iPhone 12"))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  void createPhone_emptyModel_return400() {
    mockMvc.perform(post(PATH_PHONE_PRIVATE)
            .header("Authorization", AUTHORIZATION)
            .content(format(CREATE_PHONE_REQUEST_BODY, "Apple", ""))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  void updatePhone_correctRequest_return200() {
    mockMvc.perform(put(format(PATH_PHONE_ID_PRIVATE, 1L))
            .header("Authorization", AUTHORIZATION)
            .content(format(CREATE_PHONE_REQUEST_BODY, "Apple", "iPhone 12"))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(phoneService).createOrUpdatePhone(any());
  }

  @SneakyThrows
  @Test
  void updatePhone_emptyBrandAndModel_return400() {
    mockMvc.perform(put(format(PATH_PHONE_ID_PRIVATE, 1L))
            .header("Authorization", AUTHORIZATION)
            .content(format(CREATE_PHONE_REQUEST_BODY, "", ""))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @SneakyThrows
  @Test
  void updatePhone_unauthorizedRequest_return401() {
    mockMvc.perform(put(format(PATH_PHONE_ID_PRIVATE, 1L))
            .content(format(CREATE_PHONE_REQUEST_BODY, "Apple", "iPhone 12"))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @SneakyThrows
  @Test
  void updatePhone_wrongId_return404() {
    doThrow(ResourceNotFoundException.class).when(phoneService).createOrUpdatePhone(any());

    mockMvc.perform(put(format(PATH_PHONE_ID_PRIVATE, 1L))
            .header("Authorization", AUTHORIZATION)
            .content(format(CREATE_PHONE_REQUEST_BODY, "Apple", "iPhone 12"))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @SneakyThrows
  @Test
  void deletePhone_correctRequest_return200() {
    mockMvc.perform(delete(format(PATH_PHONE_ID_PRIVATE, 1L))
            .header("Authorization", AUTHORIZATION))
        .andExpect(status().isOk());

    verify(phoneService).deletePhone(1L);
  }

  @SneakyThrows
  @Test
  void deletePhone_unauthorizedRequest_return401() {
    mockMvc.perform(delete(format(PATH_PHONE_ID_PRIVATE, 1L)))
        .andExpect(status().isUnauthorized());
  }

  @SneakyThrows
  @Test
  void deletePhone_unauthorizedRequest_return404() {
    doThrow(ResourceNotFoundException.class).when(phoneService).deletePhone(1L);

    mockMvc.perform(delete(format(PATH_PHONE_ID_PRIVATE, 1L))
            .header("Authorization", AUTHORIZATION))
        .andExpect(status().isNotFound());
  }
}