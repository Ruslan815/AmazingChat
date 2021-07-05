package ru.cft.team2.chat.model;

import javax.persistence.Id;

public class User {
    @Id
    Integer id;

    String firstName;
    String lastName;

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

    @Override
    public String toString() {
        return "User {\n" +
                "\tid:\"" + id + "\",\n" +
                "\t\"firstName\":\"" + firstName + "\",\n" +
                "\t\"lastName\":\"" + lastName + "\"\n" +
                "}";
    }
}
