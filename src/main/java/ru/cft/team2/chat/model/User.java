package ru.cft.team2.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.*;

@ApiModel(description = "Пользователь")
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(
            value = "Идентификатор пользователя",
            example = "1"
    )
    private Integer id;

    @Column(nullable = false)
    @ApiModelProperty(
            value = "Имя пользователя",
            required = true,
            example = "someFirstName"
    )
    private String firstName;

    @Column(nullable = false)
    @ApiModelProperty(
            value = "Фамилия пользователя",
            required = true,
            example = "someLastName"
    )
    private String lastName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "chatMembers")
    @ApiModelProperty(
            value = "Список доступных чатов для пользователя"
    )
    private Set<Chat> availableChats = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "usersWhoDidNotRead")
    @ApiModelProperty(
            value = "Список сообщений, которые не прочитал данный пользователь"
    )
    private List<Message> unreadMessages = new ArrayList<>();

    public User() {
    }

    public User(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Chat> getAvailableChats() {
        return availableChats;
    }

    public void setAvailableChats(Set<Chat> availableChats) {
        this.availableChats = availableChats;
    }

    public List<Message> getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(List<Message> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}
