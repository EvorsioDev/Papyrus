package ru.armagidon.papyrus.utils.placeholderloader;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AnimatedLoadingPlaceholder implements LoadingPlaceholder {

    private final AtomicInteger frameNumber = new AtomicInteger(0);
    private ScheduledFuture<?> task;
    private final List<Component> frames;
    private final long refreshRate;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public AnimatedLoadingPlaceholder(List<Component> frames, long refreshRate) {
        this.frames = ImmutableList.copyOf(frames);
        this.refreshRate = refreshRate;
        if (frames.size() == 0)
            throw new IllegalArgumentException("Empty animation");
    }


    @Override
    public Component getCurrent() {
        return frames.get(frameNumber.get());
    }

    @Override
    public synchronized void place(Runnable update) {
        frameNumber.set(0);
        if (task != null && !task.isCancelled()) {
            task.cancel(false);
        }
        task = scheduler.scheduleAtFixedRate(() -> {
            frameNumber.updateAndGet(frame -> (frame + 1) % frames.size());
            update.run();
        }, refreshRate, refreshRate, TimeUnit.MILLISECONDS);
    }

    @Override
    public void finish() {
        if (task != null && !task.isCancelled())
            task.cancel(false);
    }
}
