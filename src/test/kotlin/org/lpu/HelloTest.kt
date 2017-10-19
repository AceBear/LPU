package org.lpu

import org.junit.jupiter.api.Test
import org.lpu.system.*
import org.slf4j.LoggerFactory


class HelloTest {
    companion object {
        val s_logger = LoggerFactory.getLogger(HelloTest::class.java)
    }

    @Test
    fun hello(){
        val rte = RunTimeEnv()
        rte.start = StartLPU("Hello LPU")

        val showMsg = SameLPU<String>()
        val end = EndLPU<String>()

        rte.join(rte.start.output, showMsg.input)
        rte.join(showMsg.output, end.input)

        rte.go()
    }
}