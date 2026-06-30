package artemis.systems

import com.artemis.Aspect
import com.artemis.EntitySystem
import com.artemis.utils.IntBag
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.min

abstract class BatchProcessingSystem(private val batchSize: Int) : EntitySystem(Aspect.all()) {

    private val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private var activeEntities: IntBag? = null

    override fun processSystem() {
        activeEntities = subscription.getEntities()
        if (activeEntities!!.size() == 0) return

        val tasks: MutableList<Runnable> = ArrayList()
        var start = 0
        while (start < activeEntities!!.size()) {
            val end = min(start + batchSize, activeEntities!!.size())
            val batch = IntBag(end - start)
            for (i in start..<end) {
                batch.add(activeEntities!!.get(i))
            }
            val batchCopy = batch
            tasks.add(Runnable { processBatch(batchCopy) })
            start += batchSize
        }

        val latch = CountDownLatch(tasks.size)
        for (task in tasks) {
            executor.submit(Runnable {
                try {
                    task.run()
                } finally {
                    latch.countDown()
                }
            })
        }

        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    protected abstract fun processBatch(batch: IntBag)
}