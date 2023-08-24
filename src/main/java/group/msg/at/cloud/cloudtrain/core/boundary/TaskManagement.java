package group.msg.at.cloud.cloudtrain.core.boundary;

import group.msg.at.cloud.cloudtrain.core.control.TaskRepository;
import group.msg.at.cloud.cloudtrain.core.control.UserPermissionVerifier;
import group.msg.at.cloud.cloudtrain.core.entity.Task;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Simple {@code Boundary} that manages {@code Task} entities.
 */
@Service
@Secured("CLOUDTRAIN_USER")
public class TaskManagement {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private UserPermissionVerifier verifier;

    @NotNull
    public UUID addTask(@NotNull @Valid Task newTask) {
        verifier.requirePermission("TASK_CREATE");
        return this.repository.addTask(newTask);
    }

    public void modifyTask(@NotNull @Valid Task modifiedTask) {
        verifier.requirePermission("TASK_UPDATE");
        this.repository.setTask(modifiedTask);
    }

    public Optional<Task> getTaskById(@NotNull UUID taskId) {
        Optional<Task> result = this.repository.getTaskById(taskId);
        if (result.isPresent()) {
            verifier.requirePermission("TASK_READ");
        }
        return result;
    }

    public void removeTask(@NotNull UUID taskId) {
        Optional<Task> found = this.repository.getTaskById(taskId);
        if (found.isPresent()) {
            verifier.requirePermission("TASK_DELETE");
            repository.removeTaskById(found.get().getId());
        }
    }

    public List<Task> getAllTasks() {
        verifier.requirePermission("TASK_READ");
        return this.repository.getAllTasks();
    }
}
