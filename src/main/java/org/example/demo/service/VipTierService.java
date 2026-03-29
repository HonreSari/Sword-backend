package org.example.demo.service;

import org.example.demo.entity.VipTier;
import org.example.demo.repository.VipTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VipTierService {

    private final VipTierRepository vipTierRepository;

    public List<VipTier> getAllTiers() {
        return vipTierRepository.findAllByOrderByPriceAsc();
    }

    public VipTier getTierById(Long id) {
        return vipTierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VIP Tier not found with id: " + id));
    }

    public VipTier getTierByName(String name) {
        return vipTierRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("VIP Tier not found with name: " + name));
    }

    @Transactional
    public VipTier saveTier(VipTier vipTier) {
        return vipTierRepository.save(vipTier);
    }
}