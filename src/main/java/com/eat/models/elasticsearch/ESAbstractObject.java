package com.eat.models.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * Created by arthur.kalmykov@gmail.com
 * http://smetner.com/
 * 08.02.17
 */

@Getter
@Setter
@AllArgsConstructor
public abstract class ESAbstractObject {
    private long id;
    @NonNull
    private List<String> tags;
}
