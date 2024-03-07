package com.taskMangementService.repositories;

import com.taskMangementService.model.entities.Task;
import com.taskMangementService.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {


    Page<Task> findAllByAssignedUserOrAuthorUser(User assignedUser, User authorUser, Pageable pageable);

    void deleteAllByAuthorUserId(long userId);

    void deleteByIdAndAuthorUserId(long id, long userId);

    boolean existsByIdAndAuthorUserId(long taskId, Long id);

    @Query("SELECT t FROM Task t WHERE t.id = :id AND (t.assignedUser.id = :assignedUserId OR t.authorUser.id = :authorUserId)")
    Optional<Task> findByIdAndAssignedUserOrAuthorUser(long id, long assignedUserId, long authorUserId);

    List<Task> findByDueDateBetweenAndStatusIn(LocalDateTime fromDate, LocalDateTime toDate, List<Task.TaskStatus> taskStatuses);

}
