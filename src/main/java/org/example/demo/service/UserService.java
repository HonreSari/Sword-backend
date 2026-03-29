package org.example.demo.service;

import org.example.demo.entity.User;
import org.example.demo.entity.VipTier;
import org.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Transactional
    public void updateCreditBalance(Long userId, BigDecimal amount, String description) {
        User user = getUserById(userId);

        if (amount.compareTo(BigDecimal.ZERO) < 0 && user.getCreditBalance().compareTo(amount.abs()) < 0) {
            throw new RuntimeException("Insufficient credits");
        }

        user.setCreditBalance(user.getCreditBalance().add(amount));
        userRepository.save(user);
    }

    @Transactional
    public void updateVipTier(Long userId, Long vipTierId) {
        User user = getUserById(userId);
        // TODO: Will properly link VipTier in next bricks
        user.setActiveVipTier(null);
        userRepository.save(user);
    }
}
