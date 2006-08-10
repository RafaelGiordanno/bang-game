//
// $Id$

package com.threerings.bang.game.data.effect;

import java.util.Observer;

import com.samskivert.util.IntIntMap;

import com.threerings.util.MessageBundle;

import com.threerings.bang.game.data.BangObject;
import com.threerings.bang.game.data.piece.Piece;
import com.threerings.bang.game.data.piece.Unit;

import static com.threerings.bang.Log.log;

/**
 * Removes any influence or hindrance from a unit.
 */
public class ClearModificationsEffect extends Effect
{
    /** The identifier for the type of effect we produce. */
    public static final String CLEAR_MODIFICATIONS = "clear_modifications";

    /** The id of the piece that is being cleared. */
    public int pieceId;

    @Override // documentation inherited
    public void init (Piece piece)
    {
        pieceId = piece.pieceId;
    }

    @Override // documentation inherited
    public int[] getAffectedPieces ()
    {
        return new int[] { pieceId };
    }

    @Override // docuemtatnion inherited
    public void prepare (BangObject bangobj, IntIntMap dammap)
    {
        Piece piece = bangobj.pieces.get(pieceId);
        if (piece instanceof Unit) {
            _unit = (Unit)piece;
        }
    }

    @Override // documentation inherited
    public boolean isApplicable ()
    {
        return (_unit != null && 
                (_unit.hindrance != null || _unit.influence != null));
    }

    @Override // documentation inherited
    public boolean apply (BangObject bangobj, Observer obs)
    {
        _unit = (Unit)bangobj.pieces.get(pieceId);
        if (_unit == null) {
            log.warning("Missing target for clean modifications effect " +
                        "[id=" + pieceId + "].");
            return false;
        }

        _unit.hindrance = null;
        _unit.influence = null;
        reportEffect(obs, _unit, CLEAR_MODIFICATIONS);
        return true;
    }

    /** Reference to the unit we're affecting. */
    protected transient Unit _unit;
}
