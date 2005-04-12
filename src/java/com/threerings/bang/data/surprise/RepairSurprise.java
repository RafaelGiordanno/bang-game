//
// $Id$

package com.threerings.bang.data.surprise;

import com.threerings.bang.data.BangObject;
import com.threerings.bang.data.effect.AreaRepairEffect;
import com.threerings.bang.data.effect.Effect;

/**
 * A surprise that allows the player to repair a group of pieces.
 */
public class RepairSurprise extends Surprise
{
    public int repair = 60;

    public int radius = 2;

    @Override // documentation inherited
    public void init (BangObject bangobj, int owner)
    {
        super.init(bangobj, owner);

        // if our player is "in the nooksak", give them good repair
        if (bangobj.pstats[owner].power < 30) {
            repair = 100;
            radius = 3;
        } else if (bangobj.pstats[owner].powerFactor < 0.34) {
            repair = 100;
        }
    }

    @Override // documentation inherited
    public String getIconPath ()
    {
        return "repair" + radius;
    }

    @Override // documentation inherited
    public int getRadius ()
    {
        return radius;
    }

    @Override // documentation inherited
    public Effect activate (int x, int y)
    {
        return new AreaRepairEffect(repair, getRadius(), x, y);
    }
}
