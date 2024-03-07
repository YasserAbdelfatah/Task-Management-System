package com.taskMangementService.error.exceptions;

public class UserNotFoundException extends NotFoundException{
    public UserNotFoundException(Long id) {
        super("user", id);
    }
    public UserNotFoundException(String userName) {
        super("user", userName);
    }
}
