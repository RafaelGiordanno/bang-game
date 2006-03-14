//
// $Id$

package com.threerings.bang.saloon.data;

import java.util.ArrayList;

import com.threerings.bang.game.data.GameCodes;
import com.threerings.io.SimpleStreamableObject;

/**
 * Defines various criterion for finding opponents.
 */
public class Criterion extends SimpleStreamableObject
{
    /** Controls ranking closeness when matchmaking. */
    public static final int TIGHT = 0;

    /** Controls ranking closeness when matchmaking. */
    public static final int LOOSE = 1;

    /** Controls ranking closeness when matchmaking. */
    public static final int OPEN = 2;

    /** A bitmask indicating which round counts to allow. */
    public int rounds;

    /** A bitmask indicating which player counts to allow. */
    public int players;

    /** A bitmask indicating which rankednesses to allow. */
    public int ranked;

    /** Indicates the ranking range to allow when matchmaking, one of {@link
     * #TIGHT}, {@link #LOOSE} or {@link #OPEN}. */
    public int range;

    /** Utility function for creating criterion instances. */
    public static int compose (boolean val1, boolean val2, boolean val3)
    {
        return (val1 ? 1 : 0) | (val2 ? (1<<1) : 0) | (val3 ? (1<<2) : 0);
    }

    /**
     * Returns true if this criterion is compatible with the specified other
     * criterion. <em>Note:</em> the {@link #range} criterion is not accounted
     * for by this method as that requires knowledge of the involved players'
     * ratings.
     *
     * @return true if the criterion are compatible false if not.
     */
    public boolean isCompatible (Criterion other)
    {
        return (rounds & other.rounds) != 0 &&
            (players & other.players) != 0 &&
            (ranked & other.ranked) != 0;
    }

    /**
     * Merges the supplied other criterion into this criterion. Our bitmasks
     * will be set to the intersection of the two criterion and our rating
     * range will be set to the stricter of the two ranges.
     */
    public void merge (Criterion other)
    {
        rounds &= other.rounds;
        players &= other.players;
        ranked &= other.ranked;
        range = Math.min(range, other.range);
    }

    /**
     * Returns true if the specified player count is allowed.
     */
    public boolean isValidPlayerCount (int playerCount)
    {
        return (players & (1 << (playerCount-2))) != 0;
    }

    /**
     * Returns the largest allowed player count.
     */
    public int getDesiredPlayers ()
    {
        return highestBitIndex(players) + 1;
    }

    /**
     * Returns true if the specified round count is allowed.
     */
    public boolean isValidRoundCount (int roundCount)
    {
        return (rounds & (1 << (roundCount-1))) != 0;
    }

    /**
     * Returns the highest allowed number of rounds.
     */
    public int getDesiredRounds ()
    {
        return highestBitIndex(rounds);
    }

    /**
     * Returns the desired ranked setting, favoring ranked games over unranked.
     */
    public boolean getDesiredRankedness ()
    {
        return (ranked & (1|(1<<2))) != 0;
    }

    /**
     * Returns a string describing the allowable player counts.
     */
    public String getPlayerString ()
    {
        ArrayList<String> values = new ArrayList<String>();
        for (int ii = 2; ii <= GameCodes.MAX_PLAYERS; ii++) {
            if (isValidPlayerCount(ii)) {
                values.add(String.valueOf(ii));
            }
        }
        return join(values);
    }

    /**
     * Returns a string describing the allowable round counts.
     */
    public String getRoundString ()
    {
        ArrayList<String> values = new ArrayList<String>();
        for (int ii = 1; ii <= GameCodes.MAX_ROUNDS; ii++) {
            if (isValidRoundCount(ii)) {
                values.add(String.valueOf(ii));
            }
        }
        return join(values);
    }

    protected int highestBitIndex (int bitmask)
    {
        int highest = 0;
        while (bitmask > 0) {
            bitmask >>= 1;
            highest++;
        }
        return highest;
    }

    /**
     * Special join function that assumes we only have three possible values.
     */
    protected String join (ArrayList<String> values)
    {
        switch (values.size()) {
        case 1: return values.get(0);
        case 2: return values.get(0) + ", " + values.get(1);
        case 3: return values.get(0) + "-" + values.get(2);
        default:
            StringBuffer buf = new StringBuffer();
            for (String value : values) {
                if (buf.length() > 0) {
                    buf.append(",");
                }
                buf.append(value);
            }
            return buf.toString();
        }
    }
}
