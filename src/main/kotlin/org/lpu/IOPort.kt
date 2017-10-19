package org.lpu

import org.lpu.vis.Visual

/**
 * 输入输出端口
 */
abstract class IOPort(val lpu:LPU) : Visual(){
    /**
     * 接驳的连接器
     */
    var joint: Joint? = null
}

/**
 * Output IO Port
 */
class OutPort<in T>(lpu: LPU): IOPort(lpu){
    /**
     * 把数据压入输出端口
     * 如果没有接驳的连接器,表明这是一个悬空的输出端口,它的输出会被忽略
     */
    fun push(data:T) {
        joint?.push(data)
    }
}

/**
 * Input IO Port
 */
class InPort<out T>(lpu: LPU): IOPort(lpu){
    /**
     * 输入数据是否就绪
     * 如果没有接驳的连接器,表明这是一个悬空的输入端口,它永远不会就绪
     */
    fun isReady():Boolean{
        return joint?.isDataReady()?:false
    }

    /**
     * 取出输入的数据
     * 如果没有接驳的连接器,表明这是一个悬空的输入端口
     * 不允许从悬空的输入端口中取数据
     *
     * Jvm不存储genric类型,运行时有可能抛出类型转换异常
     * 表明输出的数据并不适合输入
     */
    @Suppress("UNCHECKED_CAST")
    fun pop():T{
        return joint!!.pop() as T
    }
}