package org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.socket.buffer.BufferPage;
import org.smartboot.socket.buffer.BufferPagePool;
import org.smartboot.socket.extension.plugins.AbstractPlugin;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.util.QuickTimerTask;

import java.lang.reflect.Field;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author: test
 * */
public class BufferPageMonitor<T> extends AbstractPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(BufferPageMonitor.class);
    /**
     * 任务执行频率
     */
    private int seconds = 0;

    private AioQuickServer<T> server;

    private ScheduledFuture<?> future;

    public BufferPageMonitor(AioQuickServer<T> server, int seconds) {
        this.seconds = seconds;
        this.server = server;
        init();
    }

    private void init() {
        long mills = TimeUnit.SECONDS.toMillis(seconds);
        future = QuickTimerTask.scheduleAtFixedRate(() -> {
            {
                if (server == null) {
                    LOGGER.error("unKnow server or client need to monitor!");
                    shutdown();
                    return;
                }
                try {
                    Field bufferPoolField = AioQuickServer.class.getDeclaredField("bufferPool");
                    bufferPoolField.setAccessible(true);
                    BufferPagePool pagePool = (BufferPagePool) bufferPoolField.get(server);
                    if (pagePool == null) {
                        LOGGER.error("server maybe has not started!");
                        shutdown();
                        return;
                    }
                    Field field = BufferPagePool.class.getDeclaredField("bufferPages");
                    field.setAccessible(true);
                    BufferPage[] pages = (BufferPage[]) field.get(pagePool);
                    String logger = "";
                    for (BufferPage page : pages) {
                        logger += "\r\n" + page.toString();
                    }
                    //LOGGER.info(logger);
                } catch (Exception e) {
                    LOGGER.error("", e);
                }
            }
        }, mills, mills);
    }

    private void shutdown() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }
}
