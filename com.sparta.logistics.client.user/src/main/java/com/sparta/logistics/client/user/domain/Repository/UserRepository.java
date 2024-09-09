package com.sparta.logistics.client.user.domain.Repository;

import com.sparta.logistics.client.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface UserRepository extends JpaRepository<User, BigInteger> {
}
