//
// $Id$

package com.threerings.bang.client;

import org.lwjgl.opengl.DisplayMode;

import com.samskivert.util.Config;

/**
 * Contains client-side preferences.
 */
public class BangPrefs
{
    /** Contains our client-side preferences. */
    public static Config config = new Config("bang");

    /**
     * Returns the display mode in the supplied list that matches the one
     * configured as our preference, or null if there are no matches.
     */
    public static DisplayMode getDisplayMode (DisplayMode[] modes)
    {
        int width = config.getValue("display_width", 1024);
        int height = config.getValue("display_height", 768);
        int bpp = config.getValue("display_bpp", 16);
        int freq = config.getValue("display_freq", 60);

        for (int ii = 0; ii < modes.length; ii++) {
            DisplayMode mode = modes[ii];
            if (width == mode.getWidth() && height == mode.getHeight() &&
                bpp == mode.getBitsPerPixel() && freq == mode.getFrequency()) {
                return mode;
            }
        }
        return null;
    }

    /**
     * Returns whether or not we prefer fullscreen mode.
     */
    public static boolean isFullscreen ()
    {
        return config.getValue("display_fullscreen", false);
    }

    /**
     * Stores our preferred display mode.
     */
    public static void updateDisplayMode (DisplayMode mode)
    {
        config.setValue("display_width", mode.getWidth());
        config.setValue("display_height", mode.getHeight());
        config.setValue("display_bpp", mode.getBitsPerPixel());
        config.setValue("display_freq", mode.getFrequency());
    }

    /**
     * Updates our fullscreen preference.
     */
    public static void updateFullscreen (boolean fullscreen)
    {
        config.setValue("display_fullscreen", fullscreen);
    }
}
