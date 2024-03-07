package com.taskMangementService.model.response;

import com.taskMangementService.model.dto.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private List<TaskDto> tasks;
}