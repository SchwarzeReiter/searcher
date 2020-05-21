package com.spd.test.google_clone.errors;
import lombok.Getter;

@Getter
public class HttpNotFountException extends Exception {

    private static final  String ERRORMESSAGE = "The site does not exist or does not work." +
            "Please check the url and try again";
   private final String message;

   public HttpNotFountException()
   {
       this.message = ERRORMESSAGE;
   }
}
