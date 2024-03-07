package com.taskMangementService.error.exceptions;

public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String name, Long id) {
        super(String.format("Could not find %s by id=%d", name, id));
    }

    public NotFoundException(String name, String userNotFound) {
        super(String.format("Could not find %s by userName=%s", name, userNotFound));
    }
}
