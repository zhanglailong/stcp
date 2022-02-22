/*******************************************************************************
 * Copyright (c) 2017-2020, org.smartboot. All rights reserved.
 * project name: smart-socket
 * file name: StringProtocol.java
 * Date: 2020-04-25
 * Author: sandao (zhengjunweimail@163.com)
 *
 ******************************************************************************/

package org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

/**
 * @author 三刀
 * @version V1.0 , 2018/11/23
 */
@Service
public class StringProtocol implements Protocol<String> {

    @Override
    public String decode(ByteBuffer readBuffer, AioSession session) {
        int remaining = readBuffer.remaining();
        if (remaining < Integer.BYTES) {
            return null;
        }
        readBuffer.mark();
        int length = readBuffer.getInt();
        if (length > readBuffer.remaining()) {
            readBuffer.reset();
            return null;
        }
        byte[] b = new byte[length];
        readBuffer.get(b);
        readBuffer.mark();
        return new String(b);
    }
}
