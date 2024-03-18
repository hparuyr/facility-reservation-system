package am.paruyr.tests.facility.reservation.service.impl;

import static java.time.LocalDateTime.now;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import am.paruyr.tests.facility.reservation.dto.BookPhoneDto;
import am.paruyr.tests.facility.reservation.dto.PhoneDto;
import am.paruyr.tests.facility.reservation.dto.PhoneRevisionDto;
import am.paruyr.tests.facility.reservation.exception.ResourceNotBookedException;
import am.paruyr.tests.facility.reservation.exception.ResourceNotFoundException;
import am.paruyr.tests.facility.reservation.exception.ResourceUnavailableException;
import am.paruyr.tests.facility.reservation.repository.PhoneRepository;
import am.paruyr.tests.facility.reservation.service.BookableResourceService;
import am.paruyr.tests.facility.reservation.service.PhoneManagementService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PhoneServiceImpl implements BookableResourceService, PhoneManagementService {
  private final PhoneRepository phoneRepository;

  @Override
  @Transactional(readOnly = true)
  public BookPhoneDto[] getAllResources() {
    return phoneRepository.findAll().stream().map(BookPhoneDto::from).toArray(BookPhoneDto[]::new);
  }

  @Override
  @Transactional(readOnly = true)
  public BookPhoneDto getResourceById(Long id) {
    return phoneRepository.findById(id).map(BookPhoneDto::from)
        .orElseThrow(() -> new ResourceNotFoundException(id));
  }

  @Override
  @Transactional
  public void bookResource(Long id, String username) {
    var phone = phoneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    if (!phone.isAvailable()) {
      throw new ResourceUnavailableException(id);
    }

    phone.bookResource(username, now());
    phoneRepository.save(phone);
  }

  @Override
  @Transactional
  public void returnResource(Long id) {
    var phone = phoneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    if (phone.isAvailable()) {
      throw new ResourceNotBookedException(id);
    }
    phone.returnResource();
    phoneRepository.save(phone);
  }

  @Override
  @Transactional(readOnly = true)
  public PhoneRevisionDto[] getResourceChangeHistory(Long id) {
    var phone = phoneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    return phoneRepository.findRevisions(phone.getId()).get().map(PhoneRevisionDto::from)
        .toArray(PhoneRevisionDto[]::new);
  }

  @Override
  @Transactional
  public void createOrUpdatePhone(PhoneDto phoneDto) {
    var phone = PhoneDto.to(phoneDto);
    if (phone.getId() != null) {
      phoneRepository.findById(phone.getId()).orElseThrow(() -> new ResourceNotFoundException(phone.getId()));
    }
    phoneRepository.save(phone);
  }

  @Override
  @Transactional
  public void deletePhone(Long id) {
    var phone = phoneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    if (!phone.isAvailable()) {
      throw new ResourceUnavailableException(phone.getId());
    }

    phoneRepository.delete(phone);
  }
}