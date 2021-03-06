package com.yoogurt.taxi.pay.service;

import com.yoogurt.taxi.dal.bo.BaseNotify;
import com.yoogurt.taxi.pay.doc.Event;

public interface EventService<T extends BaseNotify> extends EventTaskService {

    Event<T> getEvent(String eventId);

    Event<T> addEvent(Event<T> event);

    Event<T> saveEvent(Event<T> event);

    boolean deleteEvent(String eventId);
}
