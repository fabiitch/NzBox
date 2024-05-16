package com.github.fabiitch.nzbox.contact;

public interface ContactListener {

    void beginContact(ContactFixture contactFixture);

    void endContact(ContactFixture contactFixture);

    void continueContact(ContactFixture contactFixture);
}
