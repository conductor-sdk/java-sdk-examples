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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.util.concurrent.Uninterruptibles;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.run.Workflow;
import com.netflix.conductor.sdk.workflow.def.ConductorWorkflow;

import io.orkes.samples.quickstart.utils.SDKUtils;
import io.orkes.samples.quickstart.workflow.WorkflowCreator;
import io.orkes.samples.quickstart.workflow.WorkflowInput;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        SDKUtils utils = new SDKUtils();
        WorkflowCreator workflowCreator = new WorkflowCreator(utils.getWorkflowExecutor());
        ConductorWorkflow<WorkflowInput> simpleWorkflow = workflowCreator.createSimpleWorkflow();
        simpleWorkflow.setVariables(new HashMap<>());

        CompletableFuture<Workflow> workflowExecution = simpleWorkflow.executeDynamic(new WorkflowInput("userA"));
        Workflow workflowRun = workflowExecution.get(10, TimeUnit.SECONDS);

        System.out.println();
        System.out.println("=======================================================================================");
        System.out.println("Workflow Execution Completed");
        System.out.println("Workflow Id: " + workflowRun.getWorkflowId());
        System.out.println("Workflow Status: " + workflowRun.getStatus());
        System.out.println("Workflow Output: " + workflowRun.getOutput());
        String url = utils.getUIPath() + workflowRun.getWorkflowId();
        System.out.println("Workflow Execution Flow UI: " + url);
        System.out.println("=======================================================================================");

        startAsync(utils);
        //Wait for a few seconds for workers to complete executing
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);

        // Shutdown any background threads
        utils.shutdown();
        System.exit(0);
    }

    public static void startAsync(SDKUtils utils)  {
        WorkflowCreator workflowCreator = new WorkflowCreator(utils.getWorkflowExecutor());
        WorkflowDef workflowDef = workflowCreator.createSimpleWorkflow().toWorkflowDef();
        StartWorkflowRequest startWorkflowReq = new StartWorkflowRequest().withWorkflowDef(workflowDef).withInput(Map.of("userId", "userA"));
        startWorkflowReq.setName(workflowDef.getName());

        String workflowId = utils.getWorkflowClient().startWorkflow(startWorkflowReq);
        String url = utils.getUIPath() + workflowId;

        System.out.println();
        System.out.println("=======================================================================================");
        System.out.println("Workflow Execution Completed");
        System.out.println("Workflow Id: " + workflowId);
        System.out.println("Workflow Execution Flow UI: " + url);
        System.out.println("=======================================================================================");

    }
}
