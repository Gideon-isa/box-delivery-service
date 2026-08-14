package com.polarisdigitech.boxdeliveryservice.infrastructure.security.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, String> {
    List<RoleJpaEntity> findByNameIn(List<String> names);
}
