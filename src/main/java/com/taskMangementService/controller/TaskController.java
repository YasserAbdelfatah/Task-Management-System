package com.taskMangementService.controller;

import com.taskMangementService.model.dto.SearchRequest;
import com.taskMangementService.model.dto.TaskDto;
import com.taskMangementService.model.entities.User;
import com.taskMangementService.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/all")
    public Page<TaskDto> findAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return taskService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/findAllByUser")
    public Page<TaskDto> findAllByUser(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return taskService.findAllByUser(getCurrentUser(), PageRequest.of(page, size));
    }

    @GetMapping("/{taskId}")
    private TaskDto findById(@PathVariable Long taskId) {
        return taskService.findById(getCurrentUser(), taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<String> save(@RequestBody TaskDto task) {
        taskService.save(getCurrentUser(), task);
        return ResponseEntity.ok("Task added successfully");
    }

    @PatchMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable Long taskId,
                       @RequestBody
                       @Validated
                       TaskDto taskDto) {
        taskService.update(taskId, taskDto, getCurrentUser());
    }

    @DeleteMapping("/{taskId}")
    private void deleteByTaskId(@PathVariable Long taskId) {
        taskService.deleteByTaskId(getCurrentUser(), taskId);
    }

    @DeleteMapping("/deleteAll")
    private void deleteAllByUserId() {
        taskService.deleteAllByUserId(getCurrentUser());
    }

    @PostMapping("search")
    private List<TaskDto> searchTasks(@RequestBody SearchRequest searchRequest) {
        return taskService.searchTasks(searchRequest);
    }

    private User getCurrentUser() {
        long x = Instant.now().getLong(ChronoField.MILLI_OF_SECOND);

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }
}
