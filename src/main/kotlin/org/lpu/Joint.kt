package org.lpu

import java.util.concurrent.LinkedTransferQueue

/**
 * 连接器
 * 它把一个LPU的输出端口接驳到另一个LPU的输入端口上
 * 对于连接器来说,它的输入是一个LPU的输出端口,它的输出是另一个LPU的输入端口
 */
class Joint(val input: OutPort<*>, val output: InPort<*>) {

    init {
        input.joint = this
        output.joint = this
    }

    /**
     * RunTime Environment
     */
    lateinit var env:RunTimeEnv

    /**
     * 数据队列
     * 输入输出端口的速率往往并不一致
     * 此队列用于适配这种不一致情况
     * 当输入速率较高时,此队列上堆积数据
     */
    private val queueData = LinkedTransferQueue<Any?>()

    /**
     * 把数据压入连接器中
     * 数据来自于LPU的输出端口
     */
    fun push(data:Any?){
        // 压入数据
        queueData.add(data)
        // env对连接器连接的下游LPU检查可调度情况
        env.check(output.lpu)
    }

    /**
     * 连接器中的数据是否准备就绪
     * 如果数据准备就绪,LPU的输入端口可以随后调用pop()方法取出数据
     */
    fun isDataReady():Boolean{
        return queueData.size > 0
    }

    /**
     * 从连接器中取出数据
     */
    fun pop():Any?{
        return queueData.poll()
    }
}