package com.boilerplate.common.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, updatable = false)
	private Long id;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) {
			return false;
		}
		BaseEntity that = (BaseEntity) other;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
