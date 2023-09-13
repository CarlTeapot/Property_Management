package com.example.propertymanagement.repository;

import com.example.propertymanagement.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findPurchaseById(Long id);

    Optional<Purchase> findPurchaseByPropertyAddress(String propertyAddress);

    List<Purchase> findPurchasesByBuyerEmail(String email);
    @Transactional
    void deletePurchaseById (Long id);

    Optional<Purchase> findPurchaseByBuyerEmailAndPropertyAddress(String buyerEmail,
                                                                  String propertyAddress);
    @Transactional
    void deletePurchaseByPropertyAddress(String address);
}
