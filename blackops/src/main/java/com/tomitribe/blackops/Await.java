/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested 
 * of its trade secrets, irrespective of what has been deposited with the 
 * U.S. Copyright Office.
 */
package com.tomitribe.blackops;

import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Await {
    private Await() {
    }

    public static <T> T check(final Supplier<T> supplier) throws TimeoutException {
        return check(supplier, 0, 3, TimeUnit.SECONDS, Integer.MAX_VALUE, TimeUnit.DAYS);
    }

    public static <T> T check(final Supplier<T> supplier, final int initialDelay, final int period,
                              final TimeUnit unit) throws TimeoutException {
        return check(supplier, initialDelay, period, unit, Integer.MAX_VALUE, TimeUnit.DAYS);
    }

    public static <T> T check(final Supplier<T> supplier, final int initialDelay, final int period,
                              final TimeUnit unit, final int timeout, final TimeUnit timeoutUnit) throws TimeoutException {
        final Runnable command = () -> {
            final T value = supplier.get();
            if (value != null) {
                throw new TimerCompleteException(value);
            }
        };

        return check(command, initialDelay, period, unit, timeout, timeoutUnit);
    }

    public static <T> T check(final Runnable command, final int initialDelay, final int period,
                              final TimeUnit unit, final int timeout, final TimeUnit timeoutUnit) throws TimeoutException {
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        try {

            final ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(command, initialDelay, period, unit
            );

            try {

                scheduledFuture.get(timeout, timeoutUnit);

            } catch (InterruptedException e) {

                Thread.interrupted();
                throw new IllegalStateException(e);

            } catch (ExecutionException e) {

                final Throwable cause = e.getCause();

                if (cause instanceof TimerCompleteException) {
                    final TimerCompleteException completeException = (TimerCompleteException) cause;
                    return (T) completeException.getObject();
                }

                throw (RuntimeException) cause;

            }
        } finally {
            executorService.shutdown();
        }

        throw new IllegalStateException();
    }

    public static void await(final PrintStream out, final Predicate<Map<String, State>> predicate, final Supplier<Map<String, State>> supplier) throws TimeoutException {
        await(out, predicate, supplier, 0, 3, TimeUnit.SECONDS);
    }

    public static void await(final PrintStream out, final Predicate<Map<String, State>> predicate, final Supplier<Map<String, State>> supplier,
                             final int initialDelay, final int period, final TimeUnit unit) throws TimeoutException {

        final long start = System.currentTimeMillis();

        check(() -> {

            final Map<String, State> states = supplier.get();

            out.printf("\r%s - %ss" + States.printStates(states), TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start));

            return predicate.test(states) ? "Done" : null;
        }, initialDelay, period, unit);
    }


    private static class TimerCompleteException extends RuntimeException {

        private final Object object;

        public TimerCompleteException(final Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }
    }
}
