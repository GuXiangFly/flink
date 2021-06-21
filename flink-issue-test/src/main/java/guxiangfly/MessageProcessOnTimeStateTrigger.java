package guxiangfly;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.ArrayList;

/**
 * @Author guxiang02
 * @Date 2021/4/27
 **/
@Slf4j
public class MessageProcessOnTimeStateTrigger extends Trigger<Object, TimeWindow>{



    /**
     * 默认实现一次更新
     */
    long updateInterval = 1000L  *60L* 5L;


    String tairKey = "BdJobRealtimeForVisitAggrForBdId_EventTimeSlot_key";

    public MessageProcessOnTimeStateTrigger(long updateInterval) {
        this.updateInterval = updateInterval;
    }

    /**
     * 来了一条数据，处理这条数据
     * @param element
     * @param timestamp
     * @param window
     * @param ctx
     * @return
     * @throws Exception
     */
    @Override
    public TriggerResult onElement(Object element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
        ValueState<Boolean> firstSeen = ctx.getPartitionedState(
                new ValueStateDescriptor<Boolean>("first-seen", Types.BOOLEAN)
        );

        ListState<Long> timerListState = ctx.getPartitionedState(
                new ListStateDescriptor<Long>("timer-list", Types.LONG)
        );

        log.info("MessageProcessOnTimeTrigger onElement value is:{}", JSON.toJSONString(element));


        if (firstSeen.value() == null) {
            String s = JSON.toJSONString(element);
            Object ctimeObj = JSON.parseObject(s).get("ctime");
            long ctime = Long.parseLong(ctimeObj.toString());
            long currentWatermark = ctime;
            long t=  currentWatermark - currentWatermark% updateInterval + updateInterval;
            log.info("第一条数据来的时候 ctx.getCurrentWatermark() 的值是 " + ctx.getCurrentWatermark());

            long registerTime = t;
            for (int i = 0; i < 100000; i++) {
                if (registerTime > window.getEnd()){
                    break;
                }
                log.info("==============MessageProcessOnTimeTrigger init register timer:{}=======",registerTime);
                ctx.registerEventTimeTimer(registerTime);
                timerListState.add(registerTime);
                registerTime = registerTime + updateInterval;
            }


            firstSeen.update(true);
        }

        return TriggerResult.CONTINUE;
    }

    /**
     *  处理时间定时器时，要处理啥
     * @param time
     * @param window
     * @param ctx
     * @return
     * @throws Exception
     */
    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    /**
     * 事件时间改变， 类似watermark往前推移了
     * @param time
     * @param window
     * @param ctx
     * @return
     * @throws Exception
     */
    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        //判断l是否为窗口的结束时间

        log.info("====MessageProcessOnTimeTrigger onEventTime  trigger  on time:{} ====",time);

        ValueState<Long> eventTimeSlotValueState = ctx.getPartitionedState(new ValueStateDescriptor<Long>("event-time-slot", Long.class));
        eventTimeSlotValueState.update(time);


        if(time==window.getEnd()){
            //触发窗口的计算，并且清空数据
            log.info("==============MessageProcessOnTimeTrigger has close time:{}",time);
            return TriggerResult.FIRE_AND_PURGE;
        }
        //只触发计算
        return TriggerResult.FIRE;
    }

    @Override
    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
        ValueState<Boolean> firstSeen = ctx.getPartitionedState(new ValueStateDescriptor<Boolean>("first-seen", Types.BOOLEAN));
        firstSeen.update(null);



        //清除定时器
        ListState<Long> timerListState = ctx.getPartitionedState(
                new ListStateDescriptor<Long>("timer-list", Types.LONG)
        );
        ArrayList<Long> timerList = Lists.newArrayList(timerListState.get().iterator());
        for (Long timer : timerList) {
            log.info("==============MessageProcessOnTimeTrigger clear delete timer:{}=======",timer);
            ctx.deleteEventTimeTimer(timer);
        }
        timerListState.clear();
    }
}
