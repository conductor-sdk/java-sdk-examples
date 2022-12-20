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
package io.orkes.samples.quickstart.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.config.ObjectMapperProvider;
import com.netflix.conductor.sdk.workflow.executor.WorkflowExecutor;

import io.orkes.conductor.client.*;
import io.orkes.conductor.client.automator.TaskRunnerConfigurer;
import io.orkes.samples.quickstart.workers.UserTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SDKUtils {

    private static final String CONDUCTOR_SERVER_URL = "http://localhost:8080/api";

    private static ObjectMapper objectMapper = new ObjectMapperProvider().getObjectMapper();

    private final WorkflowExecutor workflowExecutor;
    private final MetadataClient metadataClient;
    private final WorkflowClient workflowClient;
    private final TaskClient taskClient;
    private ApiClient apiClient;
    private TaskRunnerConfigurer taskRunner;

    public SDKUtils() {

        String key = System.getenv("KEY");
        String secret = System.getenv("SECRET");
        String conductorServer = System.getenv("CONDUCTOR_SERVER_URL");
        if (conductorServer == null) {
            conductorServer = CONDUCTOR_SERVER_URL;
        }
        if (StringUtils.isNotBlank(key)) {
            apiClient = new ApiClient(conductorServer, key, secret);
        } else {
            apiClient = new ApiClient(conductorServer);
        }
        apiClient.setReadTimeout(30_000);

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
        this.workflowExecutor =
                new WorkflowExecutor(this.taskClient, this.workflowClient, this.metadataClient, 10);
        this.workflowExecutor.initWorkers("io.orkes.samples.quickstart.workers");
        initWorkers(Arrays.asList(new UserTransactions()));
    }

    private void initWorkers(List<Worker> workers) {

        TaskRunnerConfigurer.Builder builder =
                new TaskRunnerConfigurer.Builder(taskClient, workers);

        taskRunner = builder.withThreadCount(1).withTaskPollTimeout(5).build();

        // Start Polling for tasks and execute them
        taskRunner.init();
    }

    public WorkflowExecutor getWorkflowExecutor() {
        return workflowExecutor;
    }

    public WorkflowClient getWorkflowClient() {
        return workflowClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public String getUIPath() {
        return apiClient.getBasePath().replaceAll("api", "").replaceAll("8080", "5000")
                + "execution/";
    }

    // Clean up resources
    public void shutdown() {
        this.apiClient.shutdown();
        this.workflowClient.shutdown();
        this.taskRunner.shutdown();
    }
}
