package com.sd.challenge.booth.data.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USER_VOTE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVote {

    @Id
    @Column(name = "IDT_USER_VOTE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "FLG_ACCEPTED", length = 1, nullable = false)
    boolean name;

    @ManyToOne
    @JoinColumn(name = "IDT_USER")
    User voter;

    @ManyToOne
    @JoinColumn(name = "IDT_POLL")
    Poll poll;
}
