package am.paruyr.tests.facility.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import am.paruyr.tests.facility.reservation.entity.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long>, RevisionRepository<Phone, Long, Integer> {
}