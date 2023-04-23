package com.shinigami.api.repositories;

import com.shinigami.api.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByUserId(String userId);

    Optional<UserModel> findByEmail(String email);
}
