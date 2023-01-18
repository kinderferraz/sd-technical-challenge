package com.sd.challenge.booth.data.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "POOL")
@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class Poll {

    @Id
    @Column(name = "IDT_POOL", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "DSC_TITLE", nullable = false)
    String title;

    @Column(name = "DSC_PROPSAL", nullable = false)
    String proposal;

    @Column(name = "DAT_CREATED", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "DAT_ENDED", nullable = false)
    LocalDateTime endsAt;

    @ManyToOne
    @JoinColumn(name = "IDT_OWNER")
    User owner;

    @OneToMany(mappedBy = "id")
    List<UserVote> pollVotes;

}
