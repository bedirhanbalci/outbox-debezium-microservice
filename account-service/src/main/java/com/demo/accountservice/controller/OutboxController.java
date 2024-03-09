package com.demo.accountservice.controller;

import com.demo.accountservice.model.Outbox;
import com.demo.accountservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/outboxes")
@RequiredArgsConstructor
public class OutboxController {

    private final OutboxService outboxService;

    @GetMapping
    public List<Outbox> getOutboxed() {
        return outboxService.findAll();
    }

}
