//
// $Id$

package com.threerings.bang.client;

import com.threerings.presents.client.InvocationReceiver;

import com.threerings.bang.data.Handle;

/**
 * Defines player related notification services.
 */
public interface PlayerReceiver extends InvocationReceiver
{
    /**
     * Called when this user has been invited to be another's pardner.
     */
    public void receivedPardnerInvite (Handle handle);
}
