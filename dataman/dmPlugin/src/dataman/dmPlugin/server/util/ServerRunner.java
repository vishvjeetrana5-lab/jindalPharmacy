package dataman.dmPlugin.server.util;

/*
 * #%L
 * NanoHttpd-Webserver
 * %%
 * Copyright (C) 2012 - 2015 nanohttpd
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the nanohttpd nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
import aarogya.projectMenu.mi.shared.LocalSettingsExtendedSaveCommands;
import dataman.dmPlugin.CFunctions;
import dataman.dmPlugin.ILocalSettingsRefreshCommander;
import dataman.dmPlugin.routing.DmPluginBaseRouter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import dataman.dmPlugin.server.NanoHTTPD;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerRunner {

    /**
     * logger to log to.
     */
    private static final Logger LOG = Logger.getLogger(ServerRunner.class.getName());

    private static NanoHTTPD server = null;

    private static boolean boolRunning = false;

    public static void executeInstance(NanoHTTPD server) {
        try {
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }

        System.out.println("Server started, Hit Enter to stop.\n");

        try {
            System.in.read();
        } catch (Throwable ignored) {
        }

        server.stop();
        System.out.println("Server stopped.\n");
    }

    public static <T extends DmPluginBaseRouter> void setServer(Class<T> serverClass) {
        try {
            ServerRunner.server = serverClass.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isRunning() {
        return ServerRunner.boolRunning;
    }

    public static boolean startServer() {
        boolean bool = false;
        try {
            if (!isRunning()) {
                ServerRunner.server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
                ServerRunner.boolRunning = true;
            }

            Map<String, String> additionalParams = new HashMap<>();
            additionalParams.put(LocalSettingsExtendedSaveCommands.LSE_StartMachines.toString(), Boolean.TRUE.toString());

            CFunctions.initializeDmPlugIn(additionalParams);

            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        } finally {
            if (CFunctions.getAllAttachedLocalSettingsReloadedRefreshSubscriber() != null) {
                Iterator<String> iterator = CFunctions.getAllAttachedLocalSettingsReloadedRefreshSubscriber().keySet().iterator();
                while (iterator.hasNext()) {
                    String keyName = iterator.next();
                    ILocalSettingsRefreshCommander localSettingsRefreshCommander = CFunctions.getAllAttachedLocalSettingsReloadedRefreshSubscriber().get(keyName);
                    if (localSettingsRefreshCommander != null) {
                        try {
                            localSettingsRefreshCommander.executeAfterServerStarted();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return bool;
    }

    public static boolean stopServer() {
        boolean bool = false;
        try {
            if (isRunning()) {
                ServerRunner.server.stop();
                ServerRunner.boolRunning = false;
                bool = true;
            }
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        } finally {
            if (CFunctions.getAllAttachedLocalSettingsReloadedRefreshSubscriber() != null) {
                Iterator<String> iterator = CFunctions.getAllAttachedLocalSettingsReloadedRefreshSubscriber().keySet().iterator();
                while (iterator.hasNext()) {
                    String keyName = iterator.next();
                    ILocalSettingsRefreshCommander localSettingsRefreshCommander = CFunctions.getAllAttachedLocalSettingsReloadedRefreshSubscriber().get(keyName);
                    if (localSettingsRefreshCommander != null) {
                        try {
                            localSettingsRefreshCommander.executeAfterServerStopped();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return bool;
    }
}
