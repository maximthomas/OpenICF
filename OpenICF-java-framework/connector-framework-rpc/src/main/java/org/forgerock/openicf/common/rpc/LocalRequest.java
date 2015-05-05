/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015 ForgeRock AS. All rights reserved.
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://forgerock.org/license/CDDLv1.0.html
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at http://forgerock.org/license/CDDLv1.0.html
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 */

package org.forgerock.openicf.common.rpc;

import org.forgerock.util.promise.FailureHandler;
import org.forgerock.util.promise.SuccessHandler;

/**
 * A LocalRequest represents a remotely requested procedure call locally.
 * <p/>
 * The {@link RemoteRequest} and LocalRequest are the representation of the same
 * call on caller and receiver side.
 *
 */
public abstract class LocalRequest<M, V, E extends Exception, G extends RemoteConnectionGroup<M, G, H, P>, H extends RemoteConnectionHolder<M, G, H, P>, P extends RemoteConnectionContext<M, G, H, P>>
        implements SuccessHandler<V>, FailureHandler<E> {

    private final long requestId;

    private final P remoteConnectionContext;

    protected LocalRequest(final long requestId, final H socket) {
        this.requestId = requestId;
        remoteConnectionContext = socket.getRemoteConnectionContext();
        remoteConnectionContext.getRemoteConnectionGroup().receiveRequest(this);
    }

    protected abstract boolean tryHandleResult(V result);

    protected abstract boolean tryHandleError(E error);

    protected abstract boolean tryCancel();

    public long getRequestId() {
        return requestId;
    }

    public P getRemoteConnectionContext() {
        return remoteConnectionContext;
    }

    public final boolean cancel() {
        remoteConnectionContext.getRemoteConnectionGroup().removeRequest(getRequestId());
        return tryCancel();
    }

    public final void handleResult(final V result) {
        remoteConnectionContext.getRemoteConnectionGroup().removeRequest(getRequestId());
        tryHandleResult(result);
    }

    public final void handleError(final E error) {
        remoteConnectionContext.getRemoteConnectionGroup().removeRequest(getRequestId());
        tryHandleError(error);

    }

    public void handleIncomingMessage(final H sourceConnection, final M message) {
        throw new UnsupportedOperationException("This request does not supports");
    }

}
