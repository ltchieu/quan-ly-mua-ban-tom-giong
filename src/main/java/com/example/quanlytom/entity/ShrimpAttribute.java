package com.example.quanlytom.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Tinhchat_Tom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShrimpAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ma tinh chat")
    private Attribute attribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ma tom")
    private Shrimp shrimp;
}
