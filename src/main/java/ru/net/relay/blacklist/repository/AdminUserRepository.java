package ru.net.relay.blacklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.net.relay.blacklist.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    AdminUser findByUsernameIgnoreCase(String username);
}