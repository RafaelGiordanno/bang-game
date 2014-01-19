//
// $Id$

package com.threerings.bang.client;

import com.threerings.presents.client.Client;
import com.threerings.presents.client.InvocationService;

import com.threerings.bang.data.Handle;

/**
 * A general purpose bootstrap invocation service.
 */
public interface PlayerService extends InvocationService
{
    /** Used to note a player as friendly */
    public static final int FOLK_IS_FRIEND = 1;

    /** Used to note a player as neither friendly nor unfriendly */
    public static final int FOLK_NEUTRAL = 2;

    /** Used to note a player as unfriendly */
    public static final int FOLK_IS_FOE = 3;

    /**
     * Invite the specified user to be our pardner.
     */
    public void invitePardner (Client client, Handle handle, String message,
                               ConfirmListener listener);

    /**
     * Responds to one of our pending notifications.
     */
    public void respondToNotification (Client client, Comparable<?> key, int resp,
                                       ConfirmListener listener);

    /**
     * Remove one of our pardners from our pardner list.
     */
    public void removePardner (Client client, Handle pardner, ConfirmListener listener);

    /**
     * Requests to play the specified tutorial. On success the game will start and the client will
     * enter the game. On failure the supplied listener will be notified.
     */
    public void playTutorial (Client client, String tutid, InvocationListener listener);

    /**
     * Request to play a practice session with the specified unit.
     */
    public void playPractice (Client client, String unit, InvocationListener listener);

    /**
     * Requests to play a single round match against the computer.
     */
    public void playComputer (Client client, int players, String[] scenario, String board,
                              boolean autoplay, InvocationListener listener);

    /**
     * Requests to play the specified bounty game.
     */
    public void playBountyGame (Client client, String bounty, String game,
                                InvocationListener listener);

    /**
     * Requests to view another player's wanted poster.
     */
    public void getPosterInfo (Client client, Handle handle, ResultListener listener);

    /**
     * Requests to update the configurable attributes of our wanted posted
     */
    public void updatePosterInfo (Client client, int playerId, String Statement,
                                  int[] badgeIds, ConfirmListener listener);

    /**
     * Registers another player as friendly, neutral or unfriendly.
     */
    public void noteFolk (Client client, int playerId, int opinion, ConfirmListener listener);

    /**
     * Registers a complaint about a particular player.
     */
    public void registerComplaint (Client client, Handle target, String reason,
                                   ConfirmListener listener);

    /**
     * Requests that the specified song be perpared for download. Informs the listener of the
     * randomly assigned identifier that can be used to download the song.
     */
    public void prepSongForDownload (Client client, String song, ResultListener listener);

    /**
     * Requests to destroy an item in the player's inventory.
     */
    public void destroyItem (Client client, int itemId, ConfirmListener listener);

    /**
     * Requests to create an account for the player.
     */
    public void createAccount (Client client, String username, String password, String email,
            String affiliate, long birthday, ConfirmListener listener);

    /**
     * Boots a player from the game.
     */
    public void bootPlayer (Client client, Handle handle, ConfirmListener listener);
}