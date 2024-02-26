package com.selfdot.pixilcraftmounts.util;

import dev.architectury.event.events.common.TickEvent;
import net.minecraft.server.world.ServerWorld;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class TaskScheduler {

    private static final TaskScheduler INSTANCE = new TaskScheduler();

    private TaskScheduler() { }

    public static TaskScheduler getInstance() {
        return INSTANCE;
    }

    private long tick = 0;
    private final Queue<Task> tasks = new PriorityQueue<>(Comparator.comparingLong(task -> task.time));

    public void register() {
        TickEvent.SERVER_LEVEL_PRE.register(this::onServerTick);
    }

    private void onServerTick(ServerWorld world) {
        while (!tasks.isEmpty() && tasks.peek().time <= tick) tasks.poll().task.run();
        tick++;
    }

    public void schedule(Runnable task, long delayTicks) {
        tasks.add(new Task(task, tick + delayTicks));
    }

    private record Task(Runnable task, long time) { }

}
