/*
 * Copyright (C) 2017 The Android Open Source Project
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
 * limitations under the License.
 */

package android.arch.persistence.room.integration.testapp.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.integration.testapp.PKeyTestDatabase;
import android.arch.persistence.room.integration.testapp.vo.IntAutoIncPKeyEntity;
import android.arch.persistence.room.integration.testapp.vo.IntegerAutoIncPKeyEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class PrimaryKeyTest {
    private PKeyTestDatabase mDatabase;
    @Before
    public void setup() {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(),
                PKeyTestDatabase.class).build();
    }

    @Test
    public void integerTest() {
        IntegerAutoIncPKeyEntity entity = new IntegerAutoIncPKeyEntity();
        entity.data = "foo";
        mDatabase.integerPKeyDao().insertMe(entity);
        IntegerAutoIncPKeyEntity loaded = mDatabase.integerPKeyDao().getMe(1);
        assertThat(loaded, notNullValue());
        assertThat(loaded.data, is(entity.data));
    }

    @Test
    public void dontOverrideNullable0() {
        IntegerAutoIncPKeyEntity entity = new IntegerAutoIncPKeyEntity();
        entity.pKey = 0;
        entity.data = "foo";
        mDatabase.integerPKeyDao().insertMe(entity);
        IntegerAutoIncPKeyEntity loaded = mDatabase.integerPKeyDao().getMe(0);
        assertThat(loaded, notNullValue());
        assertThat(loaded.data, is(entity.data));
    }

    @Test
    public void intTest() {
        IntAutoIncPKeyEntity entity = new IntAutoIncPKeyEntity();
        entity.data = "foo";
        mDatabase.intPKeyDao().insertMe(entity);
        IntAutoIncPKeyEntity loaded = mDatabase.intPKeyDao().getMe(1);
        assertThat(loaded, notNullValue());
        assertThat(loaded.data, is(entity.data));
    }

    @Test
    public void getInsertedId() {
        IntAutoIncPKeyEntity entity = new IntAutoIncPKeyEntity();
        entity.data = "foo";
        final long id = mDatabase.intPKeyDao().insertAndGetId(entity);
        assertThat(mDatabase.intPKeyDao().getMe((int) id).data, is("foo"));
    }

    @Test
    public void getInsertedIds() {
        IntAutoIncPKeyEntity entity = new IntAutoIncPKeyEntity();
        entity.data = "foo";
        IntAutoIncPKeyEntity entity2 = new IntAutoIncPKeyEntity();
        entity2.data = "foo2";
        final long[] ids = mDatabase.intPKeyDao().insertAndGetIds(entity, entity2);
        assertThat(mDatabase.intPKeyDao().loadDataById(ids), is(Arrays.asList("foo", "foo2")));
    }

    @Test
    public void getInsertedIdFromInteger() {
        IntegerAutoIncPKeyEntity entity = new IntegerAutoIncPKeyEntity();
        entity.data = "foo";
        final long id = mDatabase.integerPKeyDao().insertAndGetId(entity);
        assertThat(mDatabase.integerPKeyDao().getMe((int) id).data, is("foo"));
    }

    @Test
    public void getInsertedIdsFromInteger() {
        IntegerAutoIncPKeyEntity entity = new IntegerAutoIncPKeyEntity();
        entity.data = "foo";
        IntegerAutoIncPKeyEntity entity2 = new IntegerAutoIncPKeyEntity();
        entity2.data = "foo2";
        final long[] ids = mDatabase.integerPKeyDao().insertAndGetIds(entity, entity2);
        assertThat(mDatabase.integerPKeyDao().loadDataById(ids), is(Arrays.asList("foo", "foo2")));
    }
}