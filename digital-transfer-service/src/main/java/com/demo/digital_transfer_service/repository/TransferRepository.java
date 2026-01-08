package com.demo.digital_transfer_service.repository;

import com.demo.digital_transfer_service.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}