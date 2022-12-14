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
package io.orkes.samples.quickstart.workflow;

public class WorkflowInput {

    public enum NotificationPreference {
        SMS, EMAIL
    }
    private String userId;

    private NotificationPreference notificationPref = NotificationPreference.SMS;

    public WorkflowInput(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public NotificationPreference getNotificationPref() {
        return notificationPref;
    }

    public void setNotificationPref(NotificationPreference notificationPref) {
        this.notificationPref = notificationPref;
    }
}
