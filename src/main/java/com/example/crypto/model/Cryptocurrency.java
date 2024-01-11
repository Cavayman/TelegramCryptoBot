package com.example.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cryptocurrency {
    private String symbol;
    private BigDecimal price;
    private boolean notified;
}
