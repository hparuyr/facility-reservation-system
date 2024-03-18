package am.paruyr.tests.facility.reservation.service;

import am.paruyr.tests.facility.reservation.dto.BaseResourceDto;
import am.paruyr.tests.facility.reservation.dto.BaseRevisionDto;

public interface BookableResourceService {
  BaseResourceDto[] getAllResources();

  BaseResourceDto getResourceById(Long id);

  void bookResource(Long id, String username);

  void returnResource(Long id);

  BaseRevisionDto[] getResourceChangeHistory(Long id);
}
