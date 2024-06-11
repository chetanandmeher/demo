package com.example.demo.repository;

import com.example.demo.mysql.model.UserCredentials;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserCredentialsRepository extends CrudRepository <UserCredentials, Integer>{

    Optional<UserCredentials> findByUserId(Integer id);

    void deleteByUserId(Integer id);
}
