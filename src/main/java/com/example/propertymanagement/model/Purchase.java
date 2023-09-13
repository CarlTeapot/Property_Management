package com.example.propertymanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@Entity
@Component
@AllArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "property", nullable = false)
    private String propertyAddress;

    @Column(name = "email", nullable = false)
    private String buyerEmail;

    @Column(name = "offer", nullable = false)
    private Long offer;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "purchase-approvals",
            joinColumns = { @JoinColumn(name = "purchase_id") },
            inverseJoinColumns = { @JoinColumn(name = "users_id") })
    private Set<User> purchaseApprovers;
}
