//
// $Id$

package com.threerings.bang.web;

import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.samskivert.io.PersistenceException;
import com.samskivert.jdbc.ConnectionProvider;
import com.samskivert.jdbc.StaticConnectionProvider;
import com.samskivert.servlet.JDBCTableSiteIdentifier;
import com.samskivert.servlet.SiteIdentifier;
import com.samskivert.servlet.util.RequestUtils;
import com.samskivert.util.ServiceUnavailableException;
import com.samskivert.velocity.Application;
import com.samskivert.velocity.Logic;

import com.threerings.user.OOOUserManager;

import com.threerings.bang.server.ServerConfig;
import com.threerings.bang.server.persist.PlayerRepository;
import com.threerings.bang.server.persist.StatRepository;

import static com.threerings.bang.Log.log;

/**
 * The Sheriff's Office: the admin website for Bang! Howdy.
 */
public class OfficeApp extends Application
{
    /**
     * Returns a reference to our user manager.
     */
    public OOOUserManager getUserManager ()
    {
        return _usermgr;
    }

    /**
     * Returns a reference to the player repository.
     */
    public PlayerRepository getPlayerRepository ()
    {
        return _playrepo;
    }

    /**
     * Returns a reference to the stat repository.
     */
    public StatRepository getStatRepository ()
    {
        return _statrepo;
    }

    @Override // documentation inherited
    protected void willInit (ServletConfig config)
    {
        super.willInit(config);

	try {
            // create a static connection provider
            _conprov = new StaticConnectionProvider(
                ServerConfig.getJDBCConfig());

            // create our user manager
            _usermgr = new OOOUserManager(
                ServerConfig.config.getSubProperties("oooauth"), _conprov);

            // create our repositories
            _playrepo = new PlayerRepository(_conprov);
            _statrepo = new StatRepository(_conprov);

	    log.info("Sheriff's Office initialized.");

	} catch (Throwable t) {
	    log.log(Level.WARNING, "Error initializing Sheriff's Office.", t);
	}
    }

    @Override // document inherited
    protected SiteIdentifier createSiteIdentifier (ServletContext ctx)
    {
        try {
            return new JDBCTableSiteIdentifier(_conprov);
        } catch (PersistenceException pe) {
            throw new ServiceUnavailableException(
                "Can't access site database.", pe);
        }
    }

    // document inherited
    protected String handleException (
        HttpServletRequest req, Logic logic, Exception error)
    {
        log.log(Level.WARNING, logic.getClass().getName() +
                " failed for: " + RequestUtils.reconstructURL(req), error);
        return "error.internal";
    }

    protected ConnectionProvider _conprov;
    protected OOOUserManager _usermgr;

    protected PlayerRepository _playrepo;
    protected StatRepository _statrepo;
}
