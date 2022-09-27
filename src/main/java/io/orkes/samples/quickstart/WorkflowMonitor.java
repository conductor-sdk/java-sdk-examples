package io.orkes.samples.quickstart;

import com.netflix.conductor.common.run.Workflow;
import io.orkes.conductor.client.WorkflowClient;

import java.util.Map;
import java.util.concurrent.*;

public class WorkflowMonitor {
    private Map<String, CompletableFuture<Workflow>> runningWorkflowFutures =
            new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduledWorkflowMonitor =
            Executors.newSingleThreadScheduledExecutor();


    public WorkflowMonitor(WorkflowClient workflowClient) {
        scheduledWorkflowMonitor.scheduleAtFixedRate(
                () -> {
                    for (Map.Entry<String, CompletableFuture<Workflow>> entry :
                            runningWorkflowFutures.entrySet()) {
                        String workflowId = entry.getKey();
                        CompletableFuture<Workflow> future = entry.getValue();
                        Workflow workflow = workflowClient.getWorkflow(workflowId, true);
                        if (workflow.getStatus().isTerminal()) {
                            future.complete(workflow);
                        }
                    }
                },
                100,
                100,
                TimeUnit.MILLISECONDS);
    }

    public CompletableFuture<Workflow> monitorForCompletion(String workflowId){
        CompletableFuture<Workflow> future = new CompletableFuture<>();
        runningWorkflowFutures.put(workflowId, future);
        return future;
    }


    public void shutdown() {
        scheduledWorkflowMonitor.shutdownNow();
    }
}
