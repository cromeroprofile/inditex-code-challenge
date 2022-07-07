package com.example.demoinditex.repository.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="BRANCHES")
@Data
public class BranchEntity {

    @Id
    private Long id;
    private String name;

}

