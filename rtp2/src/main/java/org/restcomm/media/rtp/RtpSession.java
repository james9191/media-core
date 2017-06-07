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
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.media.rtp;

import org.restcomm.media.rtp.session.RtpSessionEvent;
import org.restcomm.media.rtp.session.RtpSessionFsm;
import org.restcomm.media.rtp.session.RtpSessionFsmBuilder;
import org.restcomm.media.rtp.session.RtpSessionModeUpdateContext;
import org.restcomm.media.rtp.session.RtpSessionOutgoingPacketContext;
import org.restcomm.media.spi.ConnectionMode;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class RtpSession {

    private final RtpSessionFsm fsm;
    private final RtpSessionContext context;

    public RtpSession(RtpSessionContext context) {
        this.context = context;
        this.fsm = RtpSessionFsmBuilder.INSTANCE.build(this.context);
    }

    public void updateMode(ConnectionMode mode) {
        RtpSessionModeUpdateContext txContext = new RtpSessionModeUpdateContext(mode);
        this.fsm.fire(RtpSessionEvent.MODE_UPDATE, txContext);
    }

    public void incomingRtp(RtpPacket packet) {
        RtpSessionOutgoingPacketContext txContext = new RtpSessionOutgoingPacketContext(packet);
        this.fsm.fire(RtpSessionEvent.INCOMING_PACKET, txContext);
    }

    public void outgoingRtp(RtpPacket packet) {
        RtpSessionOutgoingPacketContext txContext = new RtpSessionOutgoingPacketContext(packet);
        this.fsm.fire(RtpSessionEvent.OUTGOING_PACKET, txContext);
    }

    public void close() {
        this.fsm.fire(RtpSessionEvent.CLOSE);
    }

}
