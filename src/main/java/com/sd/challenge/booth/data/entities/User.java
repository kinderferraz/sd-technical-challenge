package com.sd.challenge.booth.data.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class User {

    @Id
    @Column(name = "IDT_USER", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "DSC_CPF", length = 16, nullable = false)
    String cpf;

    @Column(name = "DSC_NAME", nullable = false)
    String name;

    @OneToMany(mappedBy = "id")
    List<Poll> userPolls;

    @OneToMany(mappedBy = "id")
    List<UserVote> userVotes;
}
