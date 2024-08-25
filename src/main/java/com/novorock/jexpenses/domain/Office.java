package com.novorock.jexpenses.domain;

import java.util.List;

public class Office {
    public Long id;
    public String name;
    public List<Contact> contacts;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
