package org.lpu.system

import org.lpu.*
import org.slf4j.LoggerFactory

/**
 * 只有一个输入和一个输出的LPU
 * 且输入输出的数据类型相同
 */
open class SameLPU<T>: AnyLPU() {
    companion object {
        val s_logger = LoggerFactory.getLogger(StartLPU::class.java)
    }

    val input = InPort<T>(this)
    val output = OutPort<T>(this)

    init {
        listInPorts.add(input)
        listOutPorts.add(output)
    }

    override fun run() {
        val data = input.pop()
        Thread.sleep(5000)
        s_logger.info("SameLPU[${this.hashCode().toString(16)}]: $data")
        output.push(data)
    }
}