package com.devsuperior.bds04.repositories;

import com.devsuperior.bds04.entities.Role;
import com.devsuperior.bds04.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
