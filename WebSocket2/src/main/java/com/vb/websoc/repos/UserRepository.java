package com.vb.websoc.repos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vb.websoc.modals.Status;
import com.vb.websoc.modals.User;

public interface UserRepository extends MongoRepository<User, String> {

	List<User> findAllByStatus(Status online);

}
