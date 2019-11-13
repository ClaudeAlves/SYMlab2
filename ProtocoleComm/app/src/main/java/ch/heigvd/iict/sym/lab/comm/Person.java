package ch.heigvd.iict.sym.lab.comm;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Person {


    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private String phone;

    public Person(String firstName, String lastName, String middleName, String gender, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.gender = gender;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null && this != null)|| getClass() != o.getClass()) return false;
        Person person = (Person) o;

        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
        if(lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;
        if(middleName != null ? !middleName.equals(person.middleName) : person.middleName != null) return false;
        if(gender != null ? !gender.equals(person.gender) : person.gender != null) return false;
        return phone != null ? phone.equals(person.phone) : person.phone == null;

    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}