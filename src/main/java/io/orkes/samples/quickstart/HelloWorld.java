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

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

public class HelloWorld implements Worker {
    @Override
    public String getTaskDefName() {
        return "hello_world_task";
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult result = new TaskResult(task);

        String name = (String) task.getInputData().get("name");
        result.addOutputData("hw_response", "Hello, " + name);

        result.setStatus(TaskResult.Status.COMPLETED);
        System.out.println("...Completed executing " + task.getTaskId());
        return result;
    }

    @Override
    public int getPollingInterval() {
        return 10;
    }
}
