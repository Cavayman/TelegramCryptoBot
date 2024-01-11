package com.example.crypto.entity;

import com.example.crypto.model.Cryptocurrency;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    @Type(JsonBinaryType.class)
    private List<Cryptocurrency> cryptocurrency;
}
