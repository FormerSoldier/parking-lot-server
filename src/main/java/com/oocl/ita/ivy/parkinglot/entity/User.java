package com.oocl.ita.ivy.parkinglot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "UPDATE USER SET DELETE_FLAG = TRUE WHERE ID = ?")
@Where(clause = "DELETE_FLAG = " + false)
public class User extends com.itmuch.lightsecurity.jwt.User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String name;
    private String password;
    private String roles;
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

    public String getPassword() {
        return password;
    }

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
}
