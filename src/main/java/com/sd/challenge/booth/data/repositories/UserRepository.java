package com.sd.challenge.booth.data.repositories;

import com.sd.challenge.booth.data.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> { }
