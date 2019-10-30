package com.chrisreal.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;

import com.chrisreal.ws.io.entity.PasswordResetTokenEntity;

public interface PasswordResetRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

}
