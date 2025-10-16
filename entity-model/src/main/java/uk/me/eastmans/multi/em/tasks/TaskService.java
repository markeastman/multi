package uk.me.eastmans.multi.em.tasks;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.eastmans.multi.em.kafka.PostKafkaEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@PreAuthorize("isAuthenticated()")
public class TaskService {

    private final TaskRepository taskRepository;
    private final PostKafkaEvent postKafkaEvent;

    TaskService(PostKafkaEvent postKafkaEvent, TaskRepository taskRepository) {
        this.postKafkaEvent = postKafkaEvent;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void createTask(String description, LocalDate dueDate) {
        if ("fail".equals(description)) {
            throw new RuntimeException("This is for testing the error handler");
        }
        Task task = new Task(description, Instant.now());
        task.setDueDate(dueDate);
        taskRepository.saveAndFlush(task);
        postKafkaEvent.postCreate("Task", task.getId() );
    }

    @Transactional(readOnly = true)
    public List<Task> list(Pageable pageable) {
        return taskRepository.findAllBy(pageable).toList();
    }

}