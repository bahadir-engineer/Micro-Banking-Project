package com.example.microbankingproject.entity;

import com.example.microbankingproject.entity.enums.Gender;
import com.example.microbankingproject.entity.enums.Roles;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private Integer CIF;
    private String finCode;
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phone;
    private String email;
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(value = EnumType.STRING)
    private Set<Roles> roles;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference
    private List<AccountEntity> accountEntities;
}
