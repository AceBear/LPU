package org.lpu.system

import org.lpu.*
import org.slf4j.LoggerFactory

/**
 * 执行逻辑的起始点
 */
class StartLPU<T>(val arg:T) : LPU() {
    companion object {
        val s_logger = LoggerFactory.getLogger(StartLPU::class.java)
    }

    val output = OutPort<T>(this)

    init {
        listOutPorts.add(output)
    }

    override fun run() {
        s_logger.info("Start!")
        output.push(arg)
    }

    override fun isReady(): Boolean {
        return true
    }
}