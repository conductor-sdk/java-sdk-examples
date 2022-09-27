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

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.lang3.StringUtils;

import com.netflix.conductor.common.config.ObjectMapperProvider;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.run.Workflow;

import io.orkes.conductor.client.*;
import io.orkes.conductor.client.automator.TaskRunnerConfigurer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkflowManagement {

    private static final String CONDUCTOR_SERVER_URL = "https://play.orkes.io/api";

    private static ObjectMapper objectMapper = new ObjectMapperProvider().getObjectMapper();

    private final MetadataClient metadataClient;
    private final WorkflowClient workflowClient;

    private final TaskClient taskClient;

    private final WorkflowMonitor workflowMonitor;

    private TaskRunnerConfigurer taskRunner;

    public WorkflowManagement(ApiClient apiClient) {
        OrkesClients orkesClients = new OrkesClients(apiClient);
        this.metadataClient = orkesClients.getMetadataClient();
        this.workflowClient = orkesClients.getWorkflowClient();
        this.taskClient = orkesClients.getTaskClient();
        this.workflowMonitor = new WorkflowMonitor(this.workflowClient);
    }

    private WorkflowDef registerWorkflowDef() throws IOException {
        InputStream is = WorkflowManagement.class.getResourceAsStream("/workflow.json");
        WorkflowDef workflowDef = objectMapper.readValue(is, WorkflowDef.class);
        metadataClient.registerWorkflowDef(workflowDef, true);
        return workflowDef;
    }

    private String startWorkflow(String name, Integer version, Map<String, Object> input) {

        StartWorkflowRequest request = new StartWorkflowRequest();
        request.setName(name);
        request.setVersion(version);
        request.setCorrelationId(UUID.randomUUID().toString());
        request.setInput(input);
        String workflowId = workflowClient.startWorkflow(request);

        return workflowId;
    }

    private CompletableFuture<Workflow> executeWorkflow(
            String name, Integer version, Map<String, Object> input) {
        String workflowId = startWorkflow(name, version, input);
        return workflowMonitor.monitorForCompletion(workflowId);
    }

    private void startWorkers() {

        TaskRunnerConfigurer.Builder builder =
                new TaskRunnerConfigurer.Builder(taskClient, List.of(new HelloWorld()));

        taskRunner = builder.withThreadCount(1).withTaskPollTimeout(100).build();

        // Start Polling for tasks and execute them
        taskRunner.init();
    }

    public static void main(String[] args) throws IOException {

        String key = System.getenv("KEY");
        String secret = System.getenv("SECRET");
        String conductorServer = System.getenv("CONDUCTOR_SERVER_URL");
        if (conductorServer == null) {
            conductorServer = CONDUCTOR_SERVER_URL;
        }

        ApiClient apiClient = new ApiClient(conductorServer, key, secret);
        if (StringUtils.isBlank(key) || StringUtils.isBlank(secret)) {
            log.warn(
                    "\n\nMissing KEY and|or SECRET.  Attemping to connect to "
                            + conductorServer
                            + " without authentication\n\n");
            apiClient = new ApiClient(conductorServer);
        }

        WorkflowManagement workflowManagement = new WorkflowManagement(apiClient);
        // Start polling for task workers
        workflowManagement.startWorkers();

        WorkflowDef workflowDef = workflowManagement.registerWorkflowDef();
        Map<String, Object> input = new HashMap<>();
        input.put("name", System.getProperty("user.name"));
        CompletableFuture<Workflow> future =
                workflowManagement.executeWorkflow(
                        workflowDef.getName(), workflowDef.getVersion(), input);
        future.thenAccept(
                workflow -> {
                    try {
                        String formattedOutput =
                                objectMapper
                                        .writerWithDefaultPrettyPrinter()
                                        .writeValueAsString(workflow.getOutput());
                        log.info(
                                "\n\n\n\nCompleted execution.  Workflow Id {} \n with output \n{} \n\n",
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
