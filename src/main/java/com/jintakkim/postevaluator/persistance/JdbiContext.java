package com.jintakkim.postevaluator.persistance;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;

/**
 * 싱글톤으로만 사용해야 한다.
 * 트렌젝션 기본 사용.
 */
public class JdbiContext {
    private final ThreadLocal<Handle> transactionHandle = new ThreadLocal<>();

    private final Jdbi jdbi;

    public JdbiContext(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public <R, X extends Exception> R withHandle(HandleCallback<R, X> callback) throws X {
        Handle boundHandle = transactionHandle.get();

        if (boundHandle != null) {
            return callback.withHandle(boundHandle);
        } else {
            return jdbi.withHandle(callback);
        }
    }

    public <X extends Exception> void useHandle(HandleConsumer<X> consumer) throws X {
        Handle boundHandle = transactionHandle.get();

        if (boundHandle != null) {
            consumer.useHandle(boundHandle);
        } else {
            jdbi.useHandle(consumer);
        }
    }

    public <X extends Exception> void useTransaction(HandleConsumer<X> callback) throws X {
        Handle boundHandle = transactionHandle.get();

        if (boundHandle != null) {
            callback.useHandle(boundHandle);
        } else {
            jdbi.useTransaction(handle -> {
                try {
                    transactionHandle.set(handle);
                    callback.useHandle(handle);
                } finally {
                    transactionHandle.remove();
                }
            });
        }
    }
}