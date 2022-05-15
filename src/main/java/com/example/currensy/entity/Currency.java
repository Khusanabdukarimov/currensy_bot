package com.example.currensy.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class Currency {
    private String name;
    private BigDecimal rate;
    private BigDecimal diff;
    private String name_ru;
    private String name_uz;
    private String name_uzs;
}
