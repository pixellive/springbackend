package com.thangthai.training.backend.repository;

import com.thangthai.training.backend.entity.Address;
import com.thangthai.training.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, String> {

    List<Address> findByUser(User user);
}
