/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package android.support.v4.app;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NotificationCompatTest {

    @SmallTest
    @Test
    public void testNotificationActionBuilder_copiesRemoteInputs() throws Throwable {
        NotificationCompat.Action a = newActionBuilder()
                .addRemoteInput(new RemoteInput("a", "b", null, false, null)).build();

        NotificationCompat.Action aCopy = new NotificationCompat.Action.Builder(a).build();

        assertSame(a.getRemoteInputs()[0], aCopy.getRemoteInputs()[0]);
    }

    @SmallTest
    @Test
    public void testNotificationActionBuilder_copiesAllowGeneratedReplies() throws Throwable {
        NotificationCompat.Action a = newActionBuilder()
                .setAllowGeneratedReplies(true).build();

        NotificationCompat.Action aCopy = new NotificationCompat.Action.Builder(a).build();

        assertEquals(a.getAllowGeneratedReplies(), aCopy.getAllowGeneratedReplies());
    }

    private NotificationCompat.Action.Builder newActionBuilder() {
        return new NotificationCompat.Action.Builder(0, "title", null);
    }
}