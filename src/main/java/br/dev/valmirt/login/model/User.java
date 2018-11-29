package br.dev.valmirt.login.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
@JsonIgnoreProperties(
        value = "password",
        allowSetters = true
)
public class User extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty()
    @Column(name = "nickname", nullable = false)
    private String nickname;

    private String name;

    @NotEmpty()
    @Column(unique = true, nullable = false)
    private String cpf;

    @NotEmpty()
    @Column(unique = true)
    @Email()
    private String email;

    private String password;

    @Column(name = "adm_auth", nullable = false)
    private Boolean admAuth = false;

    @JsonIgnore
    private String token;

    public User() {}

    public User(@NotEmpty() String nickname, String name, @NotEmpty() String cpf, @NotEmpty() String email, String password, String token) {
        this.nickname = nickname;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmAuth() {
        return admAuth;
    }

    public void setAdmAuth(Boolean admAuth) {
        this.admAuth = admAuth;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
