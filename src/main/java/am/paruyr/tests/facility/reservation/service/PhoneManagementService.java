package am.paruyr.tests.facility.reservation.service;

import am.paruyr.tests.facility.reservation.dto.PhoneDto;

public interface PhoneManagementService {
  void createOrUpdatePhone(PhoneDto phoneDto);

  void deletePhone(Long id);

}
