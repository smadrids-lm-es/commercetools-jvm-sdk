package io.sphere.sdk.orderedits.commands.stagedactions;

import io.sphere.sdk.commands.StagedUpdateActionImpl;
import io.sphere.sdk.orderedits.OrderEdit;
import io.sphere.sdk.orderedits.OrderEditStagedUpdateAction;

abstract class OrderEditStagedUpdateActionImpl extends StagedUpdateActionImpl<OrderEdit> implements OrderEditStagedUpdateAction {

    public OrderEditStagedUpdateActionImpl(final String action) {
        super(action);
    }

}