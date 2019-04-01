package com.arctouch.codechallenge.util

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import java.util.concurrent.Executor

import java.util.concurrent.TimeUnit

val immediate = object : Scheduler() {
    override fun scheduleDirect(run: Runnable,
                                delay: Long, unit: TimeUnit
    ): Disposable {
        return super.scheduleDirect(run, 0, unit)
    }

    override fun createWorker(): Scheduler.Worker {
        return ExecutorScheduler.ExecutorWorker(
            Executor { it.run() })
    }
}
