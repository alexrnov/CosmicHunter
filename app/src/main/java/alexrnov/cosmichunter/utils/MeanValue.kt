package alexrnov.cosmichunter.utils

import java.util.*

/**
 * Класс вычисляет среднее значение из группы элементов, которые хранятся в очереди.
 * Очередь имеет фиксированный объем [numberValues]. Когда очередь заполняется,
 * то при добавлении нового элемента, удаляется самый старый элемент.
 */
class MeanValue(private val numberValues: Short = 1000) {
  private val queue = ArrayDeque<Float>()

  /**
   * Добавляет новый элемент в коллекцию. При этом, если коллекция
   * заполнена, - удаляется самый старый объект. Таким образом, коллекция
   * все время обновляется.
   */
  fun add(v: Float): Float {
    queue.add(v)
    if (queue.size == 1) return v
    if (queue.size > numberValues) queue.poll()
    return average()
    //return queue.stream()
            //.collect(Collectors.averagingDouble{e -> e.toDouble()}).toFloat()
  }

  private fun average(): Float {
    var f = 0.0f
    queue.forEach { f += it }
    return f / queue.size
  }
}