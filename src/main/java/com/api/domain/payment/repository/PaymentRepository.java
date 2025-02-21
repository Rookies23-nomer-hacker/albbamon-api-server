package com.api.domain.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.domain.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
    @Query("SELECT r.id "
            + "FROM Recruitment r JOIN Payment p ON r.userId = p.userId")
       List<Long> findByUserId();
}
