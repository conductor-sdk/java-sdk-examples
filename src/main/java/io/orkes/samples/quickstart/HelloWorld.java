package io.orkes.samples.quickstart;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.stereotype.Component;

@Component
public class HelloWorld implements Worker {
    @Override
    public String getTaskDefName() {
        return "hello_world_task";
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult result = new TaskResult(task);

        String name = (String)task.getInputData().get("name");
        result.addOutputData("hw_response", "Hello, " + name);

        result.setStatus(TaskResult.Status.COMPLETED);
        return result;
    }
}
