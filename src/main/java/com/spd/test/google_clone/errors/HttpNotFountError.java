package com.spd.test.google_clone.errors;

import lombok.Getter;



@Getter
public class HttpNotFountError extends Exception {

    private final String WRONG_URL = "The site does not exist or does not work." +
            "Please check the url and try again";
   private String message;

   public HttpNotFountError(int code)
   {
       if(code == 404){
           this.message = WRONG_URL;
       }
   }
}
