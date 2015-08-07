package io.sphere.sdk.channels.commands;

import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.commands.UpdateCommandDslBuilder;
import io.sphere.sdk.commands.UpdateCommandDslImpl;
import io.sphere.sdk.models.Versioned;

import java.util.List;


final class ChannelUpdateCommandImpl extends UpdateCommandDslImpl<Channel, ChannelUpdateCommand> implements ChannelUpdateCommand {
    public ChannelUpdateCommandImpl(final Versioned<Channel> versioned, final List<? extends UpdateAction<Channel>> updateActions) {
        super(versioned, updateActions, ChannelEndpoint.ENDPOINT, ChannelUpdateCommandImpl::new);
    }

    ChannelUpdateCommandImpl(final UpdateCommandDslBuilder<Channel, ChannelUpdateCommand> builder) {
        super(builder);
    }
}