/*
 * Copyright 2022 Orkes, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.orkes.samples.quickstart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.config.ObjectMapperProvider;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import io.orkes.conductor.client.*;
import io.orkes.conductor.client.automator.TaskRunnerConfigurer;
import io.orkes.conductor.client.model.WorkflowRun;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ExecuteWorkflow {

    private static final String CONDUCTOR_SERVER_URL = "https://play.orkes.io/api";

    private static ObjectMapper objectMapper = new ObjectMapperProvider().getObjectMapper();

    private final MetadataClient metadataClient;
    private final WorkflowClient workflowClient;

    private final TaskClient taskClient;

    private TaskRunnerConfigurer taskRunner;

    public ExecuteWorkflow() {

        String key = System.getenv("KEY");
        String secret = System.getenv("SECRET");
        String conductorServer = System.getenv("CONDUCTOR_SERVER_URL");
        if (conductorServer == null) {
            conductorServer = CONDUCTOR_SERVER_URL;
        }

        ApiClient apiClient = new ApiClient(conductorServer, key, secret);
        if (StringUtils.isBlank(key) || StringUtils.isBlank(secret)) {
            System.out.println(
                    "\n\nMissing KEY and|or SECRET.  Attemping to connect to "
                            + conductorServer
                            + " without authentication\n\n");
            apiClient = new ApiClient(conductorServer);
        }

        OrkesClients orkesClients = new OrkesClients(apiClient);
        this.metadataClient = orkesClients.getMetadataClient();
        this.workflowClient = orkesClients.getWorkflowClient();
        this.taskClient = orkesClients.getTaskClient();
    }

    private WorkflowDef registerWorkflowDef() throws IOException {
        InputStream is = ExecuteWorkflow.class.getResourceAsStream("/workflow.json");
        WorkflowDef workflowDef = objectMapper.readValue(is, WorkflowDef.class);
        metadataClient.registerWorkflowDef(workflowDef, true);
        return workflowDef;
    }

    /**
     * Similar to start workflow, but returns a future that completes when the workflow reaches the terminal state.
     * @param name
     * @param version
     * @param input
     * @return
     */
    private CompletableFuture<WorkflowRun> executeWorkflow(
            String name, Integer version, Map<String, Object> input) {

        StartWorkflowRequest request = new StartWorkflowRequest();
        request.setName(name);
        request.setVersion(version);
        request.setCorrelationId(UUID.randomUUID().toString());
        request.setInput(input);

        return workflowClient.executeWorkflow(request, null);
    }

    private void startWorkers() {

        TaskRunnerConfigurer.Builder builder =
                new TaskRunnerConfigurer.Builder(taskClient, List.of(new HelloWorld()));

        taskRunner = builder.withThreadCount(100).withTaskPollTimeout(5).build();

        // Start Polling for tasks and execute them
        taskRunner.init();
    }

    public static void main(String[] args) throws IOException {

        ExecuteWorkflow workflowManagement = new ExecuteWorkflow();

        // Start polling for task workers
        workflowManagement.startWorkers();

        WorkflowDef workflowDef = workflowManagement.registerWorkflowDef();
        Map<String, Object> input = new HashMap<>();
        input.put("name", System.getProperty("user.name"));

        CompletableFuture<WorkflowRun> future =
                workflowManagement.executeWorkflow(workflowDef.getName(), workflowDef.getVersion(), input);
        future.thenAccept(
                workflow -> {
                    try {
                        String formattedOutput =
                                objectMapper
                                        .writerWithDefaultPrettyPrinter()
                                        .writeValueAsString(workflow.getOutput());
                        System.out.printf(
                                "\n\n\n\nCompleted execution.  Workflow Id %s \n with output \n%s \n\n",
                                workflow.getWorkflowId(),
                                formattedOutput);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    } finally {
                        System.exit(0);
                    }
                });
    }
}