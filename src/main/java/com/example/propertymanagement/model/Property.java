package com.example.propertymanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "property")
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(mappedBy = "properties")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @Column(nullable = false)
    @JsonProperty("size")
    private Double size;

    @Column(nullable = false, length = 100)
    @JsonProperty("address")
    private String address;

    @Column(nullable = false, length = 100)
    @JsonProperty("price")
    private Long price;

    @OneToMany(mappedBy = "property")
    private Set<ImageData> images = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "delete-approvals",
            joinColumns = { @JoinColumn(name = "property_id") },
            inverseJoinColumns = { @JoinColumn(name = "users_id") })
    @JsonIgnore
    private Set<User> deleteRequests = new HashSet<>();


}