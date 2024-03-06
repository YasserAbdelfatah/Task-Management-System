package com.taskMangementService.service;

import com.taskMangementService.error.exceptions.InvalidDataException;
import com.taskMangementService.error.exceptions.TaskNotFoundException;
import com.taskMangementService.error.exceptions.UnauthorizedException;
import com.taskMangementService.error.exceptions.UserNotFoundException;
import com.taskMangementService.model.dto.SearchRequest;
import com.taskMangementService.model.dto.TaskDto;
import com.taskMangementService.model.entities.Task;
import com.taskMangementService.model.entities.User;
import com.taskMangementService.repositories.TaskRepository;
import com.taskMangementService.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public Page<TaskDto> findAll(Pageable pageable) {
        final Page<Task> pagedResult = taskRepository.findAll(pageable);
        return pagedResult.map(TaskDto::mapperTaskToTaskDto);
    }

    public Page<TaskDto> findAllByUser(User user, Pageable pageable) {
        Page<Task> pagedResult = taskRepository.findAllByAssignedUserOrAuthorUser(user, user, pageable);
        return pagedResult.map(TaskDto::mapperTaskToTaskDto);
    }

    public TaskDto findById(User user, long taskId) {
        if (taskId <= 0) {
            throw new InvalidDataException("Invalid taskId!");
        }

        return taskRepository
                .findById(taskId)
                .map(TaskDto::mapperTaskToTaskDto)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Transactional
    public void save(User user, TaskDto taskDto) {
        if (taskDto.isInValidData()) {
            throw new InvalidDataException("Invalid task data!");
        }

        Task task = TaskDto.mapperTaskDtoToTask(taskDto);
        if (taskDto.getAssignedUserName() != null) {
            User assingedUser = userRepository.findByUserName(taskDto.getAssignedUserName())
                    .orElseThrow(() -> new UserNotFoundException(taskDto.getAssignedUserName()));
            task.setAssignedUser(assingedUser);
        }
        task.setAuthorUser(user);
        taskRepository.save(task);
    }

    @Transactional
    public void update(Long id, TaskDto taskDto, User user) {
        taskRepository.findByIdAndAssignedUserOrAuthorUser(id, user.getId(), user.getId())
                .map(task -> {
                    TaskDto.updateTaskFromTaskDto(task, taskDto);
                    if (taskDto.getAssignedUserName() != null) {
                        User assingedUser = userRepository.findByUserName(taskDto.getAssignedUserName())
                                .orElseThrow(() -> new UserNotFoundException(taskDto.getAssignedUserName()));
                        task.setAssignedUser(assingedUser);
                    }
                    Task savedTask = taskRepository.save(task);
                    return TaskDto.mapperTaskToTaskDto(savedTask);
                }).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public void deleteByTaskId(User user, long taskId) {
        if (taskId <= 0) {
            throw new InvalidDataException("Invalid taskId!");
        }
        if (!taskRepository.existsByIdAndAuthorUserId(taskId, user.getId()))
            throw new UnauthorizedException("Unauthorized action");

        taskRepository.deleteByIdAndAuthorUserId(taskId, user.getId());
    }

    @Transactional
    public void deleteAllByUserId(User user) {
        taskRepository.deleteAllByAuthorUserId(user.getId());
    }

    public List<Task> getUpcomingTaskDeadlines(LocalDateTime fromDate, LocalDateTime toDate) {
        return taskRepository.findByDueDateBetweenAndStatusIn(fromDate, toDate, List.of(Task.TaskStatus.TODO, Task.TaskStatus.IN_PROGRESS));
    }

    public List<TaskDto> searchTasks(SearchRequest searchRequest) {
        List<Task> tasks = taskRepository.searchTasks(searchRequest);
        return tasks.isEmpty() ? Collections.emptyList() : TaskDto.mapperTaskToTaskDto(tasks);

    }
}