package org.lpu

import org.lpu.vis.Visual

/**
 * 逻辑处理单元
 * 有一组输入端口和一组输出端口
 */
abstract class LPU : Visual() {
    protected val listInPorts = ArrayList<InPort<*>>()
    protected val listOutPorts = ArrayList<OutPort<*>>()
    /**
     * run
     */
    abstract fun run()

    /**
     * 是否可以被调度了
     */
    abstract fun isReady():Boolean
}

/**
 * 任意一个输入端口就绪即可调度的LPU
 */
abstract class AnyLPU:LPU(){
    override fun isReady(): Boolean {
        for(lpu in listInPorts){
            if(lpu.isReady()) return true
        }
        return false
    }
}

/**
 * 所有输入端口就绪才可调度的LPU
 * 且至少包含1个输入端口
 */
abstract class AllLPU:LPU(){
    override fun isReady(): Boolean {
        for(lpu in listInPorts){
            if(!lpu.isReady()) return false
        }
        return listInPorts.size > 0
    }
}