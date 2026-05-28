package com.example.quanlytom.service;

import com.example.quanlytom.dto.response.InventoryNotification;
import com.example.quanlytom.entity.Inventory;
import com.example.quanlytom.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventorySchedulerService {

    private final InventoryRepository inventoryRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Scan active inventory items automatically every minute.
     * Checks if the time since import is larger than 36 hours or between 24 and 36 hours.
     * Sends a notification to the frontend via WebSocket if conditions match.
     */
    @Scheduled(fixedRate = 60000) // 1 minute
    @Transactional
    public void checkInventoryAges() {
        log.info("Starting automatic inventory age check...");
        List<Inventory> activeInventories = inventoryRepository.findByStockQuantityGreaterThan(0.0);
        LocalDateTime now = LocalDateTime.now();

        for (Inventory inventory : activeInventories) {
            LocalDateTime storedAt = inventory.getStoredAt();
            if (storedAt == null) {
                // Fallback to updatedAt if storedAt is not populated
                storedAt = inventory.getUpdatedAt();
                if (storedAt == null) {
                    storedAt = now;
                }
                inventory.setStoredAt(storedAt);
            }

            Duration duration = Duration.between(storedAt, now);
            long hours = duration.toHours();

            boolean matchesWarning = (hours >= 24 && hours <= 36);
            boolean matchesCritical = (hours > 36);

            if (matchesWarning || matchesCritical) {
                String alertType = matchesCritical ? "CRITICAL" : "WARNING";
                String message = String.format(
                        "Shrimp '%s' (Batch: %s) has been stored for %d hours. Status: %s",
                        inventory.getShrimpAttribute() != null && inventory.getShrimpAttribute().getShrimp() != null
                                ? inventory.getShrimpAttribute().getShrimp().getName() : "Unknown",
                        inventory.getBatch() != null ? inventory.getBatch().getBatchName() : "Unknown",
                        hours,
                        alertType
                );

                InventoryNotification notification = InventoryNotification.builder()
                        .inventoryId(inventory.getId())
                        .shrimpName(inventory.getShrimpAttribute() != null && inventory.getShrimpAttribute().getShrimp() != null
                                ? inventory.getShrimpAttribute().getShrimp().getName() : "Unknown")
                        .attributeName(inventory.getShrimpAttribute() != null && inventory.getShrimpAttribute().getAttribute() != null
                                ? inventory.getShrimpAttribute().getAttribute().getName() : "Unknown")
                        .batchName(inventory.getBatch() != null ? inventory.getBatch().getBatchName() : "Unknown")
                        .stockQuantity(inventory.getStockQuantity())
                        .storedAt(storedAt)
                        .hoursInInventory(hours)
                        .alertType(alertType)
                        .message(message)
                        .build();

                // Send message to WebSocket topic
                messagingTemplate.convertAndSend("/topic/inventory-alerts", notification);
                log.info("Sent alert via WebSocket: {}", message);
            }

            // Update lastCheckedAt to represent the latest time the system scanned this item
            inventory.setLastCheckedAt(now);
            inventoryRepository.save(inventory);
        }
        log.info("Completed automatic inventory age check.");
    }
}
