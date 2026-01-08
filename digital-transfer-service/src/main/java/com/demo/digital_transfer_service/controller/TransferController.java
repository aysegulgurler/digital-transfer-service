package com.demo.digital_transfer_service.controller;

import com.demo.digital_transfer_service.entity.Transfer;
import com.demo.digital_transfer_service.repository.TransferRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferRepository transferRepository;

    public TransferController(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    // POST - transfer oluşturur
    @PostMapping
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        return transferRepository.save(transfer);
    }

    // GET - tüm transferleri getirir
    @GetMapping
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @GetMapping("/{id}")
    public Transfer getTransferById(@PathVariable Long id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        if (!transferRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        transferRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Transfer> updateTransfer(@PathVariable Long id, @RequestBody Transfer updatedTransfer) {
        return transferRepository.findById(id)
                .map(existingTransfer -> {
                    existingTransfer.setSenderIban(updatedTransfer.getSenderIban());
                    existingTransfer.setReceiverIban(updatedTransfer.getReceiverIban());
                    existingTransfer.setAmount(updatedTransfer.getAmount());
                    // transferDate güncellemek istemiyorsan bu satırı atla
                    // existingTransfer.setTransferDate(updatedTransfer.getTransferDate());
                    Transfer saved = transferRepository.save(existingTransfer);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Transfer> patchTransfer(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));

        if (updates.containsKey("amount")) {
            transfer.setAmount(new BigDecimal(updates.get("amount").toString()));
        }
        // Diğer alanlar için de benzer kontroller

        transferRepository.save(transfer);
        return ResponseEntity.ok(transfer);
    }
}
