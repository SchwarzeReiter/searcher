package com.spd.test.google_clone.errors;

import lombok.Getter;

@Getter
public class RepositoryError  extends Exception{

     private static final String REPO_EMPTY = "The repository base is empty, try indexing the site first";
     private String message = "Some error in repository";
     public RepositoryError(RepositoryErrorsEnum error)
     {
         if(error == RepositoryErrorsEnum.REPOSITORY_IS_EMPTY)
         {
             message = REPO_EMPTY;
         }
     }

}
