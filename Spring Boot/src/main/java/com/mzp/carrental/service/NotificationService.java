package com.mzp.carrental.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {
    private final Map<Integer, SseEmitter> agencyEmitters = new ConcurrentHashMap<>();
    private final Map<Integer, SseEmitter> customerEmitters = new ConcurrentHashMap<>();

    // ✅ Agency Emitter
    public SseEmitter createEmitter(Integer agencyId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        agencyEmitters.put(agencyId, emitter);

        emitter.onCompletion(() -> agencyEmitters.remove(agencyId));
        emitter.onTimeout(() -> agencyEmitters.remove(agencyId));

        return emitter;
    }

    public void sendMessageToAgency(Integer agencyId, String message) {
        SseEmitter emitter = agencyEmitters.get(agencyId);
        if (emitter != null) {
            try {
                emitter.send(message);
            } catch (IOException e) {
                emitter.complete();
                agencyEmitters.remove(agencyId);
            }
        }
    }

    // ✅ Customer Emitter
    public SseEmitter createCustomerEmitter(Integer customerId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        customerEmitters.put(customerId, emitter);

        emitter.onCompletion(() -> customerEmitters.remove(customerId));
        emitter.onTimeout(() -> customerEmitters.remove(customerId));

        return emitter;
    }

    public void sendMessageToCustomer(Integer customerId, String message) {
        SseEmitter emitter = customerEmitters.get(customerId);
        if (emitter != null) {
            try {
                emitter.send(message);
            } catch (IOException e) {
                emitter.complete();
                customerEmitters.remove(customerId);
            }
        }
    }
}
