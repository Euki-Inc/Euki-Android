/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kollectivemobile.euki.utils.advrecyclerview.utils;

import android.view.View;

import com.kollectivemobile.euki.utils.advrecyclerview.draggable.DraggableItemState;
import com.kollectivemobile.euki.utils.advrecyclerview.draggable.DraggableItemViewHolder;
import com.kollectivemobile.euki.utils.advrecyclerview.draggable.annotation.DraggableItemStateFlags;

import androidx.annotation.NonNull;

public abstract class AbstractDraggableSwipeableItemViewHolder extends AbstractSwipeableItemViewHolder implements DraggableItemViewHolder {
    private final DraggableItemState mDragState = new DraggableItemState();

    public AbstractDraggableSwipeableItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDragStateFlags(@DraggableItemStateFlags int flags) {
        mDragState.setFlags(flags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @DraggableItemStateFlags
    public int getDragStateFlags() {
        return mDragState.getFlags();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public DraggableItemState getDragState() {
        return mDragState;
    }
}
