package com.eat.logic.suggesters;

import com.eat.models.b2c.AppUser;


public interface Suggester {

    Advice makeSuggestion(AppUser user);

}
