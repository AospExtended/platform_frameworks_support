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

package android.arch.util.paging;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.util.ArrayList;
import java.util.List;

/**
 * NullPaddedList is a simple list, with optional null padding on the beginning and end.
 *
 * @param <Type> The type of the entries in the list.
 *
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class NullPaddedList<Type> extends PagedList<Type> {
    List<Type> mList;
    int mTrailingNullCount;
    int mLeadingNullCount;
    int mPositionOffset;

    // track the items prepended/appended since the PagedList was initialized
    int mNumberPrepended;
    int mNumberAppended;

    NullPaddedList() {
    }

    /**
     * Create a static, immutable NullPaddedList with the specified list,
     *
     * @param leadingNullCount Number of empty items in advance of the passed list.
     * @param list List of items.
     * @param trailingNullCount Number of empty items following the passed list.
     */
    NullPaddedList(int leadingNullCount, List<Type> list, int trailingNullCount) {
        if (leadingNullCount < 0 || trailingNullCount < 0) {
            throw new IllegalArgumentException("leading/trailing null count must be non-negative");
        }
        mList = list;
        mLeadingNullCount = leadingNullCount;
        mTrailingNullCount = trailingNullCount;
    }

    NullPaddedList(int leadingNullCount, int totalCount, List<Type> list) {
        int trailingNullCount = totalCount - (leadingNullCount) - list.size();

        mList = list;
        mLeadingNullCount = leadingNullCount;
        mTrailingNullCount = trailingNullCount;
    }

    NullPaddedList(int positionOffset, List<Type> list) {
        mList = list;
        mPositionOffset = positionOffset;
    }

    /**
     * Create a copy of the passed NullPaddedList.
     *
     * @param other Other list to copy.
     */
    NullPaddedList(NullPaddedList<Type> other) {
        mLeadingNullCount = other.getLeadingNullCount();
        mList = other.isImmutable() ? other.mList : new ArrayList<>(other.mList);
        mTrailingNullCount = other.getTrailingNullCount();

        mNumberPrepended = other.getNumberPrepended();
        mNumberAppended = other.getNumberAppended();
    }

    // --------------- PagedList API ---------------

    @Override
    public Type get(int index) {
        if (index >= size()) {
            throw new IllegalArgumentException();
        }

        index -= mLeadingNullCount;
        if (index < 0) {
            return null;
        }
        if (index >= mList.size()) {
            return null;
        }
        return mList.get(index);
    }

    @Override
    public void loadAround(int index) {
        // do nothing - immutable, so no fetching will be done
    }

    @Override
    public final int size() {
        return getLoadedCount() + getLeadingNullCount() + getTrailingNullCount();
    }

    public boolean isImmutable() {
        return true;
    }

    @Override
    public PagedList<Type> snapshot() {
        if (isImmutable()) {
            return this;
        }
        return new NullPaddedList<>(this);
    }

    @Override
    boolean isContiguous() {
        return true;
    }

    @Override
    public void addCallback(@Nullable PagedList<Type> previousSnapshot,
            Callback callback) {
        // no op, immutable
    }

    @Override
    public void removeCallback(Callback callback) {
        // no op, immutable
    }

    // --------------- Contiguous API ---------------

    /**
     * Position offset of the data in the list.
     * <p>
     * The item returned from <code>get(i)</code> has a position of
     * <code>i + getPositionOffset() + getLeadingNullCount()</code>.
     * <p>
     * This position corresponds to positions that are passed to a PositionalDataSource.
     */
    int getPositionOffset() {
        return mPositionOffset;
    }

    /**
     * Number of loaded items. This does not account for leading or trailing null padding.
     *
     * @return Number of loaded items.
     */
    public int getLoadedCount() {
        return mList.size();
    }

    /**
     * Number of empty, unloaded items ahead of the loaded item region.
     *
     * @return Number of nulls before the loaded list.
     */
    public int getLeadingNullCount() {
        return mLeadingNullCount;
    }

    /**
     * Number of empty, unloaded items behind the loaded item region.
     *
     * @return Number of nulls after the loaded list.
     */
    public int getTrailingNullCount() {
        return mTrailingNullCount;
    }

    int getNumberPrepended() {
        return mNumberPrepended;
    }

    int getNumberAppended() {
        return mNumberAppended;
    }
}