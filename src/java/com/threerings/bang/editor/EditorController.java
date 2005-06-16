//
// $Id$

package com.threerings.bang.editor;

import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JFileChooser;

import java.io.IOException;

import com.samskivert.swing.event.CommandEvent;
import com.samskivert.util.Tuple;

import com.threerings.util.MessageBundle;

import com.threerings.crowd.client.PlaceView;
import com.threerings.crowd.data.PlaceConfig;
import com.threerings.crowd.data.PlaceObject;
import com.threerings.crowd.util.CrowdContext;

import com.threerings.parlor.game.client.GameController;

import com.threerings.bang.data.BangBoard;
import com.threerings.bang.data.BangCodes;
import com.threerings.bang.data.BangObject;
import com.threerings.bang.data.PieceDSet;
import com.threerings.bang.data.Terrain;
import com.threerings.bang.data.piece.Piece;
import com.threerings.bang.util.BangContext;
import com.threerings.bang.util.BoardUtil;

import static com.threerings.bang.Log.log;

/**
 * Handles the logic and flow for the Bang! board editor.
 */
public class EditorController extends GameController
{
    /** The name of the command posted by the "Back to lobby" button in
     * the side bar. */
    public static final String BACK_TO_LOBBY = "BackToLobby";

    /** Instructs us to create a piece of the supplied type. */
    public static final String CREATE_PIECE = "CreatePiece";

    /** Instructs us to load a new board. */
    public static final String LOAD_BOARD = "LoadBoard";

    /** Instructs us to save the current board. */
    public static final String SAVE_BOARD = "SaveBoard";

    /** Handles a request to leave the game. Generated by the {@link
     * #BACK_TO_LOBBY} command. */
    public void handleBackToLobby (Object source)
    {
        _ctx.getLocationDirector().moveBack();
    }

    /** Handles a request to create a new piece and add it to the board.
     * Generated by the {@link #CREATE_PIECE} command. */
    public void handleCreatePiece (Object source, Piece piece)
    {
        piece = (Piece)piece.clone();
        piece.assignPieceId();
        piece.position(0, 0);
        _bangobj.addToPieces(piece);
    }

    /** Handles a request to load the current board.  Generated by the
     * {@link #LOAD_BOARD} command. */
    public void handleLoadBoard (Object source)
    {
        if (_chooser == null) {
            _chooser = new JFileChooser();
        }
        int rv = _chooser.showOpenDialog(_panel);
        if (rv != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            log.info("Loading from: " + _chooser.getSelectedFile());
            Tuple tup = BoardUtil.loadBoard(_chooser.getSelectedFile());
            _bangobj.setBoard((BangBoard)tup.left);
            Piece[] pieces = (Piece[])tup.right;
            // reassign piece ids
            for (int ii = 0; ii < pieces.length; ii++) {
                pieces[ii].pieceId = (ii+1);
            }
            Piece.setNextPieceId(pieces.length);
            _bangobj.setPieces(new PieceDSet(pieces));
            _panel.view.refreshBoard();
        } catch (IOException ioe) {
            String msg = MessageBundle.tcompose(
                "m.load_error", ioe.getMessage());
            displayMessage(msg, true);
        }
    }

    /** Handles a request to save the current board.  Generated by the
     * {@link #SAVE_BOARD} command. */
    public void handleSaveBoard (Object source)
    {
        if (_chooser == null) {
            _chooser = new JFileChooser();
        }
        int rv = _chooser.showSaveDialog(_panel);
        if (rv != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            BoardUtil.saveBoard(_bangobj.board, _bangobj.getPieceArray(),
                                _chooser.getSelectedFile());
            log.info("Saved to: " + _chooser.getSelectedFile());
        } catch (IOException ioe) {
            String msg = MessageBundle.tcompose(
                "m.save_error", ioe.getMessage());
            displayMessage(msg, true);
        }
    }

    // documentation inherited
    public void init (CrowdContext ctx, PlaceConfig config)
    {
        super.init(ctx, config);
        _ctx = (BangContext)ctx;
        _config = (EditorConfig)config;
    }

    // documentation inherited
    public void willEnterPlace (PlaceObject plobj)
    {
        super.willEnterPlace(plobj);
        _bangobj = (BangObject)plobj;

        // we may be returning to an already started game
        if (_bangobj.isInPlay()) {
            _panel.startGame(_bangobj, _config);
        }
    }

    // documentation inherited
    protected PlaceView createPlaceView (CrowdContext ctx)
    {
        _panel = new EditorPanel((BangContext)ctx, this);
        return _panel;
    }

    // documentation inherited
    protected void gameDidStart ()
    {
        super.gameDidStart();

        // our panel needs to do some game starting business
        _panel.startGame(_bangobj, _config);
    }

    // documentation inherited
    protected void gameWillReset ()
    {
        super.gameWillReset();
        _panel.endGame();
    }

    // documentation inherited
    protected void gameDidEnd ()
    {
        super.gameDidEnd();
        _panel.endGame();
    }

    /** Displays an error feedback message to the user. */
    protected void displayMessage (String message, boolean attention)
    {
        // TODO: add some sort of status display
    }

    /** A casted reference to our context. */
    protected BangContext _ctx;

    /** The configuration of this game. */
    protected EditorConfig _config;

    /** Contains our main user interface. */
    protected EditorPanel _panel;

    /** A casted reference to our game object. */
    protected BangObject _bangobj;

    /** The file chooser we use for loading and saving. */
    protected JFileChooser _chooser;
}
