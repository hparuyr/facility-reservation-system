package am.paruyr.tests.facility.reservation.service.impl;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.history.Revisions;

import java.time.LocalDateTime;
import java.util.List;
import am.paruyr.tests.facility.reservation.dto.PhoneDto;
import am.paruyr.tests.facility.reservation.entity.Phone;
import am.paruyr.tests.facility.reservation.exception.ResourceNotBookedException;
import am.paruyr.tests.facility.reservation.exception.ResourceNotFoundException;
import am.paruyr.tests.facility.reservation.exception.ResourceUnavailableException;
import am.paruyr.tests.facility.reservation.repository.PhoneRepository;

class PhoneServiceImplTest {
  private static final Long INCORRECT_ID = 11L;

  @Mock
  private PhoneRepository phoneRepository;
  @InjectMocks
  private PhoneServiceImpl underTestService;

  @BeforeEach
  void setUp() {
    openMocks(this);
    when(phoneRepository.findAll()).thenReturn(preparePhones());
    when(phoneRepository.findById(INCORRECT_ID)).thenReturn(empty());
  }

  @Test
  void getAllResources_correctCall_returnsListOfPhones() {
    assertEquals(preparePhones().size(), underTestService.getAllResources().length);
  }

  @Test
  void getResourceById_correctId_returnsBookPhoneDto() {
    var phone = preparePhones().get(0);
    when(phoneRepository.findById(1L)).thenReturn(of(phone));

    var bookPhoneDto = underTestService.getResourceById(1L);

    assertEquals(1L, bookPhoneDto.getId());
    assertEquals(phone.getBrand(), bookPhoneDto.getBrand());
    assertEquals(phone.getModel(), bookPhoneDto.getModel());
    assertEquals(phone.getBookedBy(), bookPhoneDto.getBookedBy());
    assertEquals(phone.getBookedSince(), bookPhoneDto.getBookedSince());
    assertFalse(bookPhoneDto.isAvailable());
  }

  @Test
  void getResourceById_incorrectId_throwsResourceNotFoundException() {
    assertThrows(ResourceNotFoundException.class, () -> underTestService.getResourceById(INCORRECT_ID));
  }

  @Test
  void bookResource_correctId_booksThePhone() {
    var phone = preparePhones().get(1);
    when(phoneRepository.findById(2L)).thenReturn(of(phone));

    var phoneCaptor = ArgumentCaptor.forClass(Phone.class);
    underTestService.bookResource(2L, "Tester2");
    verify(phoneRepository).save(phoneCaptor.capture());

    assertFalse(phoneCaptor.getValue().isAvailable());
    assertEquals("Tester2", phoneCaptor.getValue().getBookedBy());
  }

  @Test
  void bookResource_incorrectId_throwsResourceNotFoundException() {
    assertThrows(ResourceNotFoundException.class, () -> underTestService.bookResource(INCORRECT_ID, "Tester2"));
  }

  @Test
  void bookResource_tryAlreadyBookedResource_throwsResourceUnavailableException() {
    var phone = preparePhones().get(0);
    when(phoneRepository.findById(1L)).thenReturn(of(phone));

    assertThrows(ResourceUnavailableException.class, () -> underTestService.bookResource(1L, "Tester2"));
  }

  @Test
  void returnResource_correctId_returnsThePhone() {
    var phone = preparePhones().get(3);
    when(phoneRepository.findById(4L)).thenReturn(of(phone));

    var phoneCaptor = ArgumentCaptor.forClass(Phone.class);
    underTestService.returnResource(4L);
    verify(phoneRepository).save(phoneCaptor.capture());

    assertTrue(phoneCaptor.getValue().isAvailable());
    assertNull(phoneCaptor.getValue().getBookedBy());
  }

  @Test
  void returnResource_incorrectId_throwsResourceNotFoundException() {
    assertThrows(ResourceNotFoundException.class, () -> underTestService.returnResource(INCORRECT_ID));
  }

  @Test
  void returnResource_tryReturnNotBookedResource_throwsResourceUnavailableException() {
    var phone = preparePhones().get(1);
    when(phoneRepository.findById(2L)).thenReturn(of(phone));

    assertThrows(ResourceNotBookedException.class, () -> underTestService.returnResource(2L));
  }

  @Test
  void createOrUpdatePhone_correctPhoneDto_createsThePhone() {
    var phoneDto = new PhoneDto();
    phoneDto.setBrand("New_Brand");
    phoneDto.setModel("New_Model");

    var phoneCaptor = ArgumentCaptor.forClass(Phone.class);
    underTestService.createOrUpdatePhone(phoneDto);
    verify(phoneRepository).save(phoneCaptor.capture());

    assertEquals("New_Brand", phoneCaptor.getValue().getBrand());
    assertEquals("New_Model", phoneCaptor.getValue().getModel());
  }

  @Test
  void createOrUpdatePhone_phoneDtoWithId_updatesThePhone() {
    var phone = preparePhones().get(4);
    when(phoneRepository.findById(5L)).thenReturn(of(phone));

    var phoneDto = new PhoneDto(5L, "New_Brand", "New_Model");

    var phoneCaptor = ArgumentCaptor.forClass(Phone.class);
    underTestService.createOrUpdatePhone(phoneDto);
    verify(phoneRepository).save(phoneCaptor.capture());

    assertEquals(5L, phoneCaptor.getValue().getId());
    assertEquals("New_Brand", phoneCaptor.getValue().getBrand());
    assertEquals("New_Model", phoneCaptor.getValue().getModel());
  }

  @Test
  void createOrUpdatePhone_phoneDtoWithIncorrectId_throwsResourceNotFoundException() {
    var phoneDto = new PhoneDto(INCORRECT_ID, "New_Brand", "New_Model");

    assertThrows(ResourceNotFoundException.class, () -> underTestService.createOrUpdatePhone(phoneDto));
  }

  @Test
  void deletePhone_correctId_deletesThePhone() {
    var phone = preparePhones().get(5);
    when(phoneRepository.findById(6L)).thenReturn(of(phone));

    underTestService.deletePhone(6L);
    verify(phoneRepository).delete(phone);
  }

  @Test
  void deletePhone_incorrectId_throwsResourceNotFoundException() {
    assertThrows(ResourceNotFoundException.class, () -> underTestService.deletePhone(INCORRECT_ID));
  }

  @Test
  void getPhoneChangeHistory_correctId_emptyList() {
    var phone = preparePhones().get(6);
    when(phoneRepository.findById(7L)).thenReturn(of(phone));
    when(phoneRepository.findRevisions(7L)).thenReturn(Revisions.none());

    var phoneChangeHistory = underTestService.getResourceChangeHistory(7L);
    assertEquals(0, phoneChangeHistory.length);
  }

  @Test
  void getPhoneChangeHistory_incorrectId_throwsResourceNotFoundException() {
    assertThrows(ResourceNotFoundException.class, () -> underTestService.getResourceChangeHistory(INCORRECT_ID));
  }

  private List<Phone> preparePhones() {
    var phone1 = new Phone(1L, "Brand1", "Model1");
    phone1.setBookedBy("Tester");
    phone1.setBookedSince(LocalDateTime.now());

    var phone2 = new Phone(2L, "Brand2", "Model2");
    var phone3 = new Phone(3L, "Brand2", "Model3");
    var phone4 = new Phone(4L, "Brand2", "Model4");
    phone4.setBookedBy("Tester4");
    phone4.setBookedSince(LocalDateTime.now());

    var phone5 = new Phone(5L, "Brand3", "Model5");
    var phone6 = new Phone(6L, "Brand3", "Model6");
    var phone7 = new Phone(7L, "Brand4", "Model7");

    return List.of(phone1, phone2, phone3, phone4, phone5, phone6, phone7);
  }

}