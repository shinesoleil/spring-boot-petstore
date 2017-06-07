package com.thoughtworks.petstore.core.pet;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
@Repository
public interface PetRepository extends PagingAndSortingRepository<Pet, Long> {
}
