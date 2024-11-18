package com.github.fabiitch.nzbox.contact.listener;

import com.github.fabiitch.nzbox.contact.data.ContactFixture;

public interface ContactListener {

    void beginContact(ContactFixture contactFixture);

    void endContact(ContactFixture contactFixture);

}
