//
// $Id$

package com.threerings.bang.game.data.card;

import com.threerings.bang.data.UnitConfig;

import com.threerings.bang.game.data.BangObject;
import com.threerings.bang.game.data.effect.Effect;
import com.threerings.bang.game.data.effect.SnakeBiteEffect;
import com.threerings.bang.game.data.piece.Piece;
import com.threerings.bang.game.data.piece.Unit;

/**
 * A card that makes a unit take damage at every tick.
 */
public class SnakeBite extends Card
{
    @Override // documentation inherited
    public String getType ()
    {
        return "snake_bite";
    }

    @Override // documentation inherited
    public boolean isValidPiece (BangObject bangobj, Piece target)
    {
        return (target instanceof Unit && target.isAlive() && 
                ((Unit)target).getConfig().make == UnitConfig.Make.HUMAN);
    }

    @Override // documentation inherited
    public int getWeight ()
    {
        return 40;
    }

    @Override // documenataion inherited
    public Effect activate (BangObject bangobj, Object target)
    {
        SnakeBiteEffect effect = new SnakeBiteEffect();
        effect.pieceId = (Integer)target;
        return effect;
    }

    @Override // documentation inherited
    public int getScripCost ()
    {
        return 150;
    }

    @Override // documentation inherited
    public int getCoinCost ()
    {
        return 0;
    }
}
