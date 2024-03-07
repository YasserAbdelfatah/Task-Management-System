package com.taskMangementService.model.dto;

import com.taskMangementService.model.entities.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
public class SearchRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titleFilter;
    private String descriptionFilter;
    private LocalDateTime dueDateFrom;
    private LocalDateTime dueDateTo;
    private Set<Task.TaskStatus> status;
}
