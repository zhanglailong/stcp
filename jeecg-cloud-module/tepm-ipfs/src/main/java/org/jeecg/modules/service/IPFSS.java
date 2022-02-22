package org.jeecg.modules.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.ipfs.api.IPFS;
import io.ipfs.api.JSONParser;
import io.ipfs.multiaddr.MultiAddress;
import org.jeecg.modules.entity.PinKeys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class IPFSS extends IPFS {
    public IPFSS(String host, int port) {
        super(host, port);
    }

    public IPFSS(String multiaddr) {
        super(multiaddr);
    }

    public IPFSS(MultiAddress addr) {
        super(addr);
    }

    public IPFSS(String host, int port, String version, boolean ssl) {
        super(host, port, version, ssl);
    }

    public IPFSS(String host, int port, String version, int connectTimeoutMillis, int readTimeoutMillis, boolean ssl) {
        super(host, port, version, connectTimeoutMillis, readTimeoutMillis, ssl);
    }

    public Map<String, PinKeys> pin() throws IOException {
        Map reply = this.retrieveMap("pin/ls");
        Map<String, PinKeys> pins = JSONObject.parseObject(JSON.toJSONString(reply.get("Keys")), new TypeReference<Map<String, PinKeys>>() {});
        return pins;
    }

    private Map retrieveMap(String path) throws IOException {
        return (Map)this.retrieveAndParse(path);
    }

    private Object retrieveAndParse(String path) throws IOException {
        byte[] res = this.retrieve(path);
        return JSONParser.parse(new String(res));
    }

    private byte[] retrieve(String path) throws IOException {
        URL target = new URL(this.protocol, this.host, this.port, "/api/v0/" + path);
        return get(target, 10000, 60000);
    }



    private static byte[] get(URL target, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
        HttpURLConnection conn = configureConnection(target, "POST", connectTimeoutMillis, readTimeoutMillis);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        InputStream in;
        try {
            OutputStream out = conn.getOutputStream();
            out.write(new byte[0]);
            out.flush();
            out.close();
            in = conn.getInputStream();
            ByteArrayOutputStream resp = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];

            int r;
            while((r = in.read(buf)) >= 0) {
                resp.write(buf, 0, r);
            }

            return resp.toByteArray();
        } catch (ConnectException var9) {
            throw new RuntimeException("Couldn't connect to IPFS daemon at " + target + "\n Is IPFS running?");
        } catch (IOException var10) {
            in = conn.getErrorStream();
            String err = in == null ? var10.getMessage() : new String(readFully(in));
            throw new RuntimeException("IOException contacting IPFS daemon.\n" + err + "\nTrailer: " + conn.getHeaderFields().get("Trailer"), var10);
        }
    }

    private static HttpURLConnection configureConnection(URL target, String method, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)target.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(connectTimeoutMillis);
        conn.setReadTimeout(readTimeoutMillis);
        return conn;
    }

    private static final byte[] readFully(InputStream in) {
        try {
            ByteArrayOutputStream resp = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];

            int r;
            while((r = in.read(buf)) >= 0) {
                resp.write(buf, 0, r);
            }

            return resp.toByteArray();
        } catch (IOException var4) {
            throw new RuntimeException("Error reading InputStrean", var4);
        }
    }

}
