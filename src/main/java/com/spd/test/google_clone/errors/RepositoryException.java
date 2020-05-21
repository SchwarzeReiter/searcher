package com.spd.test.google_clone.errors;

import lombok.Getter;

@Getter
public class RepositoryException extends Exception{
     private final String message;
     public RepositoryException(String message)
     {
         this.message = message;
     }
}
