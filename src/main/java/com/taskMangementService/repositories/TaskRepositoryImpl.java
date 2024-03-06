package com.taskMangementService.repositories;

import com.taskMangementService.model.dto.SearchRequest;
import com.taskMangementService.model.entities.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Task> searchTasks(SearchRequest searchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = criteriaBuilder.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);

        prepareFilters(criteriaBuilder, query, root, searchRequest);

        return entityManager.createQuery(query).getResultList();
    }

    private void prepareFilters(CriteriaBuilder criteriaBuilder, CriteriaQuery<Task> query, Root<Task> root
            , SearchRequest searchRequest) {
        List<Predicate> predicates = new ArrayList<>();


        if (searchRequest.getTitleFilter() != null && !searchRequest.getTitleFilter().isEmpty()) {
            String titleKeyLower = searchRequest.getTitleFilter().toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                    "%" + titleKeyLower + "%"));
        }

        if (searchRequest.getDescriptionFilter() != null && !searchRequest.getDescriptionFilter().isEmpty()) {
            String descKeyLower = searchRequest.getDescriptionFilter().toLowerCase();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    "%" + descKeyLower + "%"));
        }

        if (searchRequest.getDueDateFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), searchRequest.getDueDateFrom()));
        }

        if (searchRequest.getDueDateTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), searchRequest.getDueDateTo()));
        }

        if (searchRequest.getStatus() != null && !searchRequest.getStatus().isEmpty()) {
            predicates.add(root.get("status").in(searchRequest.getStatus()));
        }

        query.where(predicates.toArray(new Predicate[0]));
    }
}
