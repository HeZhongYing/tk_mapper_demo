package com.hezy.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "i_users")
@Data
public class User implements Serializable {

    @Id
    private Integer id;

    @Column(name = "username")
    private String username;

    private String password;
}
