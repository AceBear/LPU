package org.lpu.system

import org.lpu.*
import org.slf4j.LoggerFactory

/**
 * 执行逻辑的起始点
 */
class EndLPU<T>() : AnyLPU() {
    companion object {
        val s_logger = LoggerFactory.getLogger(EndLPU::class.java)
    }

    val input = InPort<T>(this)

    init {
        listInPorts.add(input)
    }

    override fun run() {
    }

    fun end():T{
        run()
        val ret = input.pop()
        s_logger.info("End : $ret")
        return ret
    }
}