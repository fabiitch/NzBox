package com.github.fabiitch.nzbox.contact.listener;

import com.badlogic.gdx.utils.Array;
import com.github.fabiitch.nzbox.contact.data.ContactFixture;

public class ContactListenerMultiplexer implements ContactListener {

    private final Array<ContactListener> listeners = new Array<>();

    @Override
    public void beginContact(ContactFixture contactFixture) {
        for (ContactListener listener : listeners) {
            listener.beginContact(contactFixture);
        }
    }

    @Override
    public void endContact(ContactFixture contactFixture) {
        for (ContactListener listener : listeners) {
            listener.endContact(contactFixture);
        }
    }

    public void addContactListener(ContactListener listener) {
        listeners.add(listener);
    }

    public void removeContactListener(ContactListener listener) {
        listeners.removeValue(listener, true);
    }
}
