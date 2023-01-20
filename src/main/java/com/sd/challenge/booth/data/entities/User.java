package com.sd.challenge.booth.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "IDT_USER", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "DSC_CPF", length = 16, nullable = false, unique = true)
    String cpf;

    @Column(name = "DSC_NAME", nullable = false)
    String name;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "id")
    Set<Poll> userPolls;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    Set<UserVote> userVotes;

}
