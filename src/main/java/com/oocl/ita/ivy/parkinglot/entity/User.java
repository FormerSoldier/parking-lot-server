package com.oocl.ita.ivy.parkinglot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
//@SQLDelete(sql = "UPDATE USER SET DELETE_FLAG = TRUE WHERE ID = ?")
//@Where(clause = "DELETE_FLAG = " + false)
@Table(name = "USER_MASTER")
public class User extends com.itmuch.lightsecurity.jwt.User {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String password;

    @Column
    private String roles;

    @Column
    private boolean deleteFlag;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return Arrays.asList(roles.split(","));
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = StringUtils.join(roles, ",");
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}
