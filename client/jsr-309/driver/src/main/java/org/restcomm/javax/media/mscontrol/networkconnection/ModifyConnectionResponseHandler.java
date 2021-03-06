/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag. 
 *  
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *  
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.restcomm.javax.media.mscontrol.networkconnection;

import org.restcomm.fsm.UnknownTransitionException;
import org.restcomm.javax.media.mscontrol.networkconnection.fsm.ConnectionTransition;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpListener;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.ModifyConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

/**
 *
 * @author kulikov
 */
public class ModifyConnectionResponseHandler implements JainMgcpListener {
    private NetworkConnectionImpl connection;
    
    public ModifyConnectionResponseHandler(NetworkConnectionImpl connection) {
        this.connection = connection;
    }

    public void processMgcpCommandEvent(JainMgcpCommandEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void processMgcpResponseEvent(JainMgcpResponseEvent evt) {
        ModifyConnectionResponse resp = (ModifyConnectionResponse) evt;
        switch (resp.getReturnCode().getValue()) {
            case ReturnCode.TRANSACTION_BEING_EXECUTED:
                return;
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
                try {
                    connection.sdpPortManager.setLocalDescriptor(resp.getLocalConnectionDescriptor().toString());
                    connection.fsm.signal(ConnectionTransition.OPENED);
                } catch (UnknownTransitionException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                    connection.fsm.signalAsync(ConnectionTransition.FAILURE);
                }
                break;
            case ReturnCode.ENDPOINT_UNKNOWN :
                break;
            case ReturnCode.CONNECTION_WAS_DELETED:
                break;
            default:
                break;
        }
    }
}
