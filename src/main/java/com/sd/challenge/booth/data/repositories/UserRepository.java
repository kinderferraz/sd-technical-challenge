package com.sd.challenge.booth.data.repositories;

import com.sd.challenge.booth.data.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select u from User u join fetch u.userPolls where u.id = (:userId)")
    User findUserByIdWithPolls(Long userId);

    User findUserById(Long userId);

    User findUserByCpf(String idtCpfInput);

}
