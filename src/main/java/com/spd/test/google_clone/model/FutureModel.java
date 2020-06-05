package com.spd.test.google_clone.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FutureModel {
   private long id;
    private final String futureModel =" new future model";
    private String name ;
    private String location;
    private String local;
    private LocalDate localDate;
}
