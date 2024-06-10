package com.example.demo.repository;

import com.example.demo.mysql.model.UserCredentials;
import org.springframework.data.repository.CrudRepository;

public interface UserCredentialsRepository extends CrudRepository <UserCredentials, Integer>{
}
