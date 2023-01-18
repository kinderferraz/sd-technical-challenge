package com.sd.challenge.booth.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "POLL")
@Getter
@Setter
@ToString
public class Poll {

    @Id
    @Column(name = "IDT_POOL", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "DSC_TITLE", nullable = false)
    String title;

    @Column(name = "DSC_PROPOSAL", nullable = false, length = 1000)
    String proposal;

    @Column(name = "DAT_CREATED", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "DAT_ENDED", nullable = false)
    LocalDateTime endsAt;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDT_OWNER", referencedColumnName = "IDT_USER", nullable = false)
    User owner;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "id")
    List<UserVote> pollVotes;

}
