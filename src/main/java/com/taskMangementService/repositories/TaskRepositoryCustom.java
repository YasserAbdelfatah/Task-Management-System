package com.taskMangementService.repositories;

import com.taskMangementService.model.dto.SearchRequest;
import com.taskMangementService.model.entities.Task;

import java.util.List;

public interface TaskRepositoryCustom {
    List<Task> searchTasks(SearchRequest searchRequest);
}
