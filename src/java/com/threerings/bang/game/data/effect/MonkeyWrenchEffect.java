//
// $Id$

package com.threerings.bang.game.data.effect;

import com.threerings.bang.data.UnitConfig;

import com.threerings.bang.game.data.BangObject;
import com.threerings.bang.game.data.piece.Hindrance;
import com.threerings.bang.game.data.piece.Unit;

/**
 * An effect that causes a steam unit to take damage at every step.
 */
public class MonkeyWrenchEffect extends SetHindranceEffect
{
    @Override // documentation inherited
    public boolean isApplicable ()
    {
        return super.isApplicable() &&
            _unit.getConfig().make == UnitConfig.Make.STEAM;
    }

    @Override // documentation inherited
    protected Hindrance createHindrance (final Unit target)
    {
        return new Hindrance() {
            public String getName () {
                return "monkey_wrench";
            }
            public Effect maybeGeneratePostMoveEffect (int steps) {
                return new DamageEffect(target, steps * DAMAGE_PER_STEP);
            }
        };
    }

    @Override // documentation inherited
    protected String getEffectName ()
    {
        return "monkey_wrench";
    }
    
    /** The amount of damage to inflict per step. */
    protected static final int DAMAGE_PER_STEP = 5;
}
