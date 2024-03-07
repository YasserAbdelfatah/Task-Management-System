package com.taskMangementService.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class EmailRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String to;
    private String subject;
    private String text;
}
