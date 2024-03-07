package com.taskMangementService.job;

import com.taskMangementService.model.dto.EmailRequest;
import com.taskMangementService.model.entities.Task;
import com.taskMangementService.service.EmailService;
import com.taskMangementService.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyEmailSender {

    private final TaskService taskService;

    private final EmailService emailService;

    @Scheduled(cron = "0 0 0 * * *") // Schedule to run at midnight every day
    public void sendDailyEmail() {

        // Fetch upcoming task deadlines (within next 24 hours)
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime toDate = fromDate.plusDays(1);
        List<Task> upcomingTasks = taskService.getUpcomingTaskDeadlines(fromDate, toDate);

        // Construct email message

        if (!upcomingTasks.isEmpty()) {
            for (Task task : upcomingTasks) {
                String subject = "Daily Tasks and Upcoming Deadlines";
                StringBuilder emailText = new StringBuilder("Upcoming task deadlines:\n");
                emailText.append("- ").append(task.getTitle()).append(" (deadline: ").append(task.getDueDate()).append(")\n");

                emailService.sendSimpleMessage(new EmailRequest(task.getAssignedUser().getUserName(), subject, emailText.toString()));

            }
        }

    }
}
