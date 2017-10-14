package com.yoogurt.taxi.dal.beans;

import com.yoogurt.taxi.dal.annotation.Domain;
import com.yoogurt.taxi.dal.common.SuperModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Domain
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_tag_statistic")
public class CommentTagStatistic extends SuperModel {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag_name")
    private String tagName;

    private Integer counter;

    public CommentTagStatistic(Long userId) {
        this.userId = userId;
    }
}