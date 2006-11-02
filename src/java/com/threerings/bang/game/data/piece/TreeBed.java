//
// $Id$

package com.threerings.bang.game.data.piece;

import java.util.ArrayList;

import com.threerings.bang.game.client.sprite.PieceSprite;
import com.threerings.bang.game.client.sprite.TreeBedSprite;
import com.threerings.bang.game.data.BangObject;
import com.threerings.bang.game.data.effect.Effect;
import com.threerings.bang.game.data.effect.FetishEffect;
import com.threerings.bang.game.data.effect.TreeBedEffect;
import com.threerings.bang.game.util.PieceUtil;

import static com.threerings.bang.Log.log;

/**
 * A tree bed that grows a tree in distinct phases, with the damage keeping
 * track of progress to the next phase.
 */
public class TreeBed extends Prop
{
    /** The final phase of the tree's growth. */
    public static final byte FULLY_GROWN = 3;

    /** The damage levels at which the tree grows to new phases. */
    public static final int[] GROWTH_DAMAGE = { 66, 33, 0 };
    
    /** The current growth phase of the tree, from 0 to FULLY_GROWN. */
    public byte growth;

    public TreeBed ()
    {
        damage = 100;
    }
    
    @Override // documentation inherited
    public void init ()
    {
        lastActed = Short.MAX_VALUE;
        growth = 0;
        damage = 100;
    }

    @Override // documentation inherited
    public boolean removeFromBoard (BangObject bangobj)
    {
        return true; // the scenario adds them before each wave
    }
    
    @Override // documentation inherited
    public void wasKilled (short tick)
    {
        // don't reset our lastActed
    }
    
    @Override // documentation inherited
    public int getGoalRadius (Piece mover)
    {
        return (mover.owner != -1 && isAlive() && damage > 0) ? +1 : -1;
    }
    
    @Override // documentation inherited
    public boolean isAlive ()
    {
        return super.isAlive() || growth == 0;
    }
    
    @Override // documentation inherited
    public boolean expireWreckage (short tick)
    {
        return false;
    }

    /**
     * "Damages" this tree bed by the specified amount, causing it to grow to
     * new phases when the damage reaches certain threshold levels.
     */
    public void damage (int dinc)
    {
        damage = Math.max(Math.min(damage + dinc, 100), 0);
        for (int ii = 0; ii < GROWTH_DAMAGE.length; ii++) {
            if (damage <= GROWTH_DAMAGE[ii]) {
                growth = (byte)Math.max(growth, ii + 1);
            }
        }
    }

    /**
     * Returns the amount of damage in proportion to the tree's current growth
     * state.
     */
    public float getPercentDamage ()
    {
        if (growth == 0) {
            return 0f;
        }
        int gdamage = GROWTH_DAMAGE[growth - 1];
        return (float)Math.max(0, (damage - gdamage)) / (100 - gdamage);
    }
    
    @Override // documentation inherited
    public ArrayList<Effect> tick (
            short tick, BangObject bangobj, Piece[] pieces)
    {
        // can't heal dead trees
        if (!isAlive()) {
            return null;
        }

        // normal units cause the tree to grow; logging robots cause it to
        // shrink
        int dinc = 0, ddec = 0;
        boolean doubleGrowth = false;
        ArrayList<Piece> growers = new ArrayList<Piece>();
        for (Piece piece : pieces) {
            if (piece instanceof Unit && getDistance(piece) == 1 &&
                piece.isAlive()) {
                Unit unit = (Unit)piece;
                if (FetishEffect.FROG_FETISH.equals(unit.holding)) {
                    doubleGrowth = true;
                }
                int pdamage = unit.getTreeProximityDamage();
                if (pdamage > 0) {
                    continue;
                } else {
                    ddec += pdamage;
                }
                growers.add(piece);
            }
        }
        int tdamage = dinc + ddec * (doubleGrowth ? 2 : 1);
        if ((dinc == 0 && ddec == 0) || (growth == FULLY_GROWN &&
            damage == 0 && tdamage < 0)) {
            return null;
        }
        ArrayList<Effect> effects = new ArrayList<Effect>();
        effects.add(new TreeBedEffect(this,
            growers.toArray(new Piece[growers.size()]), tdamage));
        return effects;
    }
    
    @Override // documentation inherited
    public PieceSprite createSprite ()
    {
        return new TreeBedSprite();
    }
}
