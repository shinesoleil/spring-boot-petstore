package com.thoughtworks.petstore.core.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends PagingAndSortingRepository<Order, String> {
    Page<Order> findByUsername(@Param("username") String username, Pageable pageable);
}
