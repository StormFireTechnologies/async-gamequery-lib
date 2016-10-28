/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.asyncgamequerylib.protocols.valve.source;

import io.netty.channel.ChannelOption;
import org.ribasco.asyncgamequerylib.core.enums.ChannelType;
import org.ribasco.asyncgamequerylib.core.enums.ProcessingMode;
import org.ribasco.asyncgamequerylib.core.messenger.GameServerMessenger;
import org.ribasco.asyncgamequerylib.core.transport.NettyPooledTcpTransport;
import org.ribasco.asyncgamequerylib.protocols.valve.source.request.SourceRconAuthRequest;
import org.ribasco.asyncgamequerylib.protocols.valve.source.request.SourceRconCmdRequest;
import org.ribasco.asyncgamequerylib.protocols.valve.source.request.SourceRconTerminator;
import org.ribasco.asyncgamequerylib.protocols.valve.source.response.SourceRconAuthResponse;
import org.ribasco.asyncgamequerylib.protocols.valve.source.response.SourceRconCmdResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SourceRconMessenger extends GameServerMessenger<SourceRconRequest, SourceRconResponse, NettyPooledTcpTransport<SourceRconRequest>> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconMessenger.class);

    public SourceRconMessenger() {
        super(new NettyPooledTcpTransport<>(), new SourceRconSessionIdFactory(), ProcessingMode.SYNCHRONOUS);
    }

    @Override
    public void configureTransport(NettyPooledTcpTransport<SourceRconRequest> transport) {
        transport.setChannelType(ChannelType.NIO_TCP);
        transport.setChannelInitializer(new SourceRconChannelInitializer(this));
        //Channel Options
        transport.addChannelOption(ChannelOption.SO_SNDBUF, 1048576);
        transport.addChannelOption(ChannelOption.SO_RCVBUF, 1048576 * 4);
    }

    @Override
    public void configureMappings(Map<Class<? extends SourceRconRequest>, Class<? extends SourceRconResponse>> map) {
        map.put(SourceRconAuthRequest.class, SourceRconAuthResponse.class);
        map.put(SourceRconCmdRequest.class, SourceRconCmdResponse.class);
        map.put(SourceRconTerminator.class, SourceRconCmdResponse.class);
    }
}