package com.mindhub.homeBanking.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFilterDTO {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String accountNumber;
}
