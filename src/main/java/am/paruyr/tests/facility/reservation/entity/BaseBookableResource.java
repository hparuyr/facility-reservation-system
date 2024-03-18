package am.paruyr.tests.facility.reservation.entity;

import static java.time.LocalDateTime.now;

import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@MappedSuperclass
@Data
@Audited
public class BaseBookableResource {
  private String bookedBy;
  private LocalDateTime bookedSince;

  @Column(nullable = false)
  private LocalDateTime updatedOn;

  public boolean isAvailable() {
    return bookedSince == null;
  }

  public void bookResource(String username, LocalDateTime bookedSince) {
    this.bookedSince = bookedSince;
    this.bookedBy = username;
  }

  public void returnResource() {
    this.bookedSince = null;
    this.bookedBy = null;
  }

  @PrePersist
  @PreUpdate
  public void onUpdate() {
    updatedOn = now();
  }

}
