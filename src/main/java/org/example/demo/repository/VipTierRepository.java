package org.example.demo.repository;

import org.example.demo.entity.VipTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VipTierRepository extends JpaRepository<VipTier, Long> {

    Optional<VipTier> findByName(String name);

    // Useful for membership dashboard
    List<VipTier> findAllByOrderByPriceAsc();
}
