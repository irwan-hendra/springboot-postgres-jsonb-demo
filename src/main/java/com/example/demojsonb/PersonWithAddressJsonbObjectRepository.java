package com.example.demojsonb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonWithAddressJsonbObjectRepository extends JpaRepository<PersonWithAddressJsonbObject, Integer> {
}
