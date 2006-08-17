//
// $Id$

package com.threerings.bang.game.data.card;

import com.threerings.bang.data.BangCodes;
import com.threerings.bang.game.data.BangObject;

import com.threerings.bang.game.data.effect.HoldEffect;
import com.threerings.bang.game.data.effect.NuggetEffect;
import com.threerings.bang.game.data.effect.Effect;
import com.threerings.bang.game.data.piece.Piece;
import com.threerings.bang.game.data.piece.Unit;

/**
 * A card that allows the player to make a unit drop any held bonus (it's
 * called drop nugget for legacy reasons).
 */
public class DropNugget extends Card
{
    @Override // documentation inherited
    public String getType ()
    {
        return "drop_nugget";
    }

    @Override // documentation inherited
    public boolean isValidPiece (BangObject bangobj, Piece target)
    {
        return (target instanceof Unit && target.isAlive() &&
            ((Unit)target).holding != null;
    }

    @Override // documentation inherited
    public String getTownId ()
    {
        return BangCodes.FRONTIER_TOWN;
    }

    @Override // documentation inherited
    public int getWeight ()
    {
        return 50;
    }

    @Override // documentation inherited
    public int getScripCost ()
    {
        return 90;
    }

    @Override // documentation inherited
    public Effect activate (BangObject bangobj, Object target)
    {
        Unit unit = (Unit)bangobj.pieces.get((Integer)target);
        return HoldEffect.dropBonus(bangobj, unit, -1, unit.holding);
    }
}
