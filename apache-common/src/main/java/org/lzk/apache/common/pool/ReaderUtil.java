package org.lzk.apache.common.pool;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.lzk.apache.common.pool.factory.StringBufferFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Duration;

public class ReaderUtil {

    private ObjectPool<StringBuffer> pool;

    public ReaderUtil(ObjectPool<StringBuffer> pool) {
        this.pool = pool;
    }

    /**
     * Dumps the contents of the {@link Reader} to a String, closing the {@link Reader} when done.
     */
    public String readToString(Reader in)
            throws IOException {
        StringBuffer buf = null;
        try {
            buf = pool.borrowObject();
            for (int c = in.read(); c != -1; c = in.read()) {
                buf.append((char) c);
            }
            return buf.toString();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to borrow buffer from pool" + e.toString());
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                // ignored
            }
            try {
                if (null != buf) {
                    pool.returnObject(buf);
                }
            } catch (Exception e) {
                // ignored
            }
        }
    }

    public static void main(String[] args) throws IOException {

        GenericObjectPoolConfig<StringBuffer> poolConfig = new GenericObjectPoolConfig<>();
        /**
         * 池中最大空闲对象
         */
        poolConfig.setMaxIdle(10);
        /**
         * 池中最小空闲对象
         */
        poolConfig.setMinIdle(2);
        /**
         * 由池产生的全部对象
         */
        poolConfig.setMaxTotal(20);
        /**
         * 对象是等待时间
         */
        poolConfig.setMaxWait(Duration.ofMillis(10000));



        GenericObjectPool<StringBuffer> pool = new GenericObjectPool<>(new StringBufferFactory(), poolConfig);

        ReaderUtil readerUtil = new ReaderUtil(pool);

        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("pool.txt");

        String s = readerUtil.readToString(new InputStreamReader(resourceAsStream));
        System.out.println(s);
    }
}