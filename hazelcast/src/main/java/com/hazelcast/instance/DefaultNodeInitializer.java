/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.instance;

import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.IOService;
import com.hazelcast.nio.MemberSocketInterceptor;
import com.hazelcast.nio.tcp.DefaultPacketReader;
import com.hazelcast.nio.tcp.DefaultPacketWriter;
import com.hazelcast.nio.tcp.DefaultSocketChannelWrapperFactory;
import com.hazelcast.nio.tcp.PacketReader;
import com.hazelcast.nio.tcp.PacketWriter;
import com.hazelcast.nio.tcp.SocketChannelWrapperFactory;
import com.hazelcast.nio.tcp.TcpIpConnection;
import com.hazelcast.security.SecurityContext;
import com.hazelcast.storage.DataRef;
import com.hazelcast.storage.Storage;
import com.hazelcast.wan.WanReplicationService;
import com.hazelcast.wan.impl.WanReplicationServiceImpl;

public class DefaultNodeInitializer implements NodeInitializer {

    protected ILogger logger;
    protected ILogger systemLogger;
    protected Node node;
    protected String version;
    protected String build;

    @Override
    public void beforeInitialize(Node node) {
        this.node = node;
        systemLogger = node.getLogger("com.hazelcast.system");
        logger = node.getLogger("com.hazelcast.initializer");
        parseSystemProps();
    }

    @Override
    public void printNodeInfo(Node node) {
        systemLogger.info("Hazelcast " + version + " ("
                + build + ") starting at " + node.getThisAddress());
        systemLogger.info("Copyright (C) 2008-2014 Hazelcast.com");
    }

    @Override
    public void afterInitialize(Node node) {
    }

    protected void parseSystemProps() {
        final BuildInfo buildInfo = node.getBuildInfo();
        version = buildInfo.getVersion();
        build = buildInfo.getBuild();
        String revision = buildInfo.getRevision();
        if (!revision.isEmpty()) {
            build += " - " + revision;
        }
    }

    @Override
    public SecurityContext getSecurityContext() {
        logger.warning("Security features are only available on Hazelcast Enterprise!");
        return null;
    }

    @Override
    public Storage<DataRef> getOffHeapStorage() {
        throw new UnsupportedOperationException("Offheap feature is only available on Hazelcast Enterprise!");
    }

    @Override
    public WanReplicationService geWanReplicationService() {
        return new WanReplicationServiceImpl(node);
    }

    @Override
    public MemberSocketInterceptor getMemberSocketInterceptor() {
        logger.warning("SocketInterceptor feature is only available on Hazelcast Enterprise!");
        return null;
    }

    @Override
    public SocketChannelWrapperFactory getSocketChannelWrapperFactory() {
        return new DefaultSocketChannelWrapperFactory();
    }

    @Override
    public PacketReader createPacketReader(TcpIpConnection connection, IOService ioService) {
        return new DefaultPacketReader(connection, ioService);
    }

    @Override
    public PacketWriter createPacketWriter(final TcpIpConnection connection, final IOService ioService) {
        return new DefaultPacketWriter();
    }

    @Override
    public void destroy() {
        logger.info("Destroying node initializer.");
    }
}
