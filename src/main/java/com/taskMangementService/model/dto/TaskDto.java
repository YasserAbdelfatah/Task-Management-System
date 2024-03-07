package com.taskMangementService.model.dto;

import com.taskMangementService.model.entities.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Task.TaskStatus status;
    private Task.Priority priority;
    private LocalDateTime createDate;
    private String assignedUserName;

    @JsonIgnore
    public boolean isInValidData() {
        return StringUtils.isEmpty(title) || status == null || priority == null;
    }

    public static List<TaskDto> mapperTaskToTaskDto(List<Task> tasks) {
        if (tasks == null) {
            return new ArrayList<>();
        }

        return tasks.stream().map(TaskDto::mapperTaskToTaskDto).toList();
    }

    public static TaskDto mapperTaskToTaskDto(Task task) {
        if (task == null) {
            return null;
        }

        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assignedUserName(task.getAssignedUser() != null ? task.getAssignedUser().getUserName() : null)
                .createDate(task.getCreateDate())
                .build();
    }

    public static Task mapperTaskDtoToTask(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }

        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .status(taskDto.getStatus())
                .priority(taskDto.getPriority())
                .build();
    }

    public static void updateTaskFromTaskDto(Task task, TaskDto taskDto) {

        BeanUtils.copyProperties(taskDto, task, "id");  // Exclude "id" from copying

    }
}