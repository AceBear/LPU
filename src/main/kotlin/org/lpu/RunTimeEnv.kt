package org.lpu

import org.lpu.system.*
import org.slf4j.LoggerFactory
import java.util.concurrent.*



/**
 * Runtime environment
 */
class RunTimeEnv {
    companion object {
        val s_logger = LoggerFactory.getLogger(RunTimeEnv::class.java)
    }
    /**
     * start ponit
     */
    lateinit var start: StartLPU<*>

    /**
     * ready queue
     */
    private val queueReady = LinkedBlockingQueue<LPU>()

    /**
     * executor
     */
    private val executor = Executors.newFixedThreadPool(8)

    fun go():Any?{
        start.run()

        while(true) {
            val lpu = queueReady.take()
            if(lpu is EndLPU<*>){
                executor.shutdown()

                return lpu.end()
            }
            else {
                executor.execute() {
                    while (lpu.isReady()) {
                        // 确保同一个LPU不会同时被执行
                        if(lpu.tryEntry()) {
                            try {
                                lpu.run()
                            } catch (ex: Exception) {
                                s_logger.error(printStackTrace(ex))
                            }finally {
                                lpu.leave()
                            }
                        }
                        else{
                            s_logger.warn("Multiple LPU enter trying detected!")
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查一个LPU是否可调度
     */
    fun check(lpu:LPU){
        if(lpu.isReady()){
            synchronized(queueReady) {
                // 确保queueReady中不会有重复的lpu
                if (!queueReady.contains(lpu)) {
                    queueReady.add(lpu)
                }
            }
        }
    }

    /**
     * 把一个LPU的输出端口连接到另一个LPU的输入端口上
     */
    fun join(outPort:OutPort<*>, inPort:InPort<*>):Joint{
        val joint = Joint(outPort, inPort)
        joint.env = this
        return joint
    }

    private fun printStackTrace(ex: Exception): String {
        val sbTxt = StringBuilder()

        var exx: Throwable? = ex
        while (exx != null) {
            sbTxt.append("\n    ")
            sbTxt.append(exx.toString())
            for (e in exx.stackTrace) {
                sbTxt.append("\n        ")
                sbTxt.append(e.toString().trim { it <= ' ' })
            }
            exx = exx.cause
        }
        return sbTxt.toString()
    }
}