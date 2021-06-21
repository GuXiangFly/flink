package guxiangfly;


import guxiangfly.utils.DateUtils;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperator;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @Author guxiang02
 * @Date 2021/5/28
 **/
public class BdVisitHisAggrByBdIdWindowFunction extends RichWindowFunction<BdVisitHisWideBean, BdVisitHisGroupByBdIdBean, Long, TimeWindow> {

    ValueState<Long> eventTimeSlotValueState  = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        eventTimeSlotValueState  = getRuntimeContext().getState(new ValueStateDescriptor<Long>("event-time-slot",  Types.LONG));
    }

    @Override
    public void apply(Long aLong, TimeWindow window, Iterable<BdVisitHisWideBean> input, Collector<BdVisitHisGroupByBdIdBean> out) throws Exception {
        BdVisitHisGroupByBdIdBean result = new BdVisitHisGroupByBdIdBean();

        result.setBd_id(aLong);
        Long eventTimeSlotValue = eventTimeSlotValueState.value();
        if (eventTimeSlotValue == null) {
            return;
        }
        Set<Long> poiIdSetForVisitNumTotal = new HashSet<>();
        Set<Long> poiIdSetForVisitNum500m = new HashSet<>();


        for (BdVisitHisWideBean bdVisitHisWideBean : input) {
            /** 删选出 ctime比 eventTime slot 小的数据进行统计 */
            if (bdVisitHisWideBean.getCtime() > eventTimeSlotValue) {
                continue;
            }

            result.setCompany_id(bdVisitHisWideBean.getCompany_id());
            result.setBd_id(bdVisitHisWideBean.getBd_id());

            /**总拜访门店数 （按门店去重）*/
            if (poiIdSetForVisitNumTotal.add(bdVisitHisWideBean.getPoi_id())){
                result.setVisit_num_total(result.getVisit_num_total() + 1);
            }

            /**500m定位内拜访门店数 （按门店去重）*/
            if (bdVisitHisWideBean.getDistance() == null){
                bdVisitHisWideBean.setDistance(999999);
            }
            if (bdVisitHisWideBean.getDistance()<=500 && poiIdSetForVisitNum500m.add(bdVisitHisWideBean.getPoi_id())){
                result.setVisit_num_500m(result.getVisit_num_500m()+1);
            }

            Integer type = bdVisitHisWideBean.getType();
            /**上门拜访数*/
            if (type == 1) {
                result.setVisit_num_real(result.getVisit_num_real() + 1);
            }
            /**电话拜访数*/
            if (type == 3) {
                result.setVisit_num_phone(result.getVisit_num_phone() + 1);
            }

            Integer visit_type = bdVisitHisWideBean.getVisit_type();
            if (visit_type == 1) {
                result.setVisit_num_hunt(result.getVisit_num_hunt() + 1);
            }
            if (visit_type == 2) {
                result.setVisit_num_farm(result.getVisit_num_farm() + 1);
            }

            String visit_purposes = bdVisitHisWideBean.getVisit_purposes();
            Set<Integer> purpose_set = Arrays.stream(visit_purposes.split(",")).map(Integer::parseInt).collect(Collectors.toSet());
            if (purpose_set.contains(2)) {
                result.setVisit_num_material(result.getVisit_num_material() + 1);
            }

            result.setEvent_time(bdVisitHisWideBean.getCtime());
        }


        result.setProcess_time(System.currentTimeMillis());
        result.setEvent_time_str(DateUtils.getStrByDateLong(result.getEvent_time()));

        result.setEvent_slot_time(eventTimeSlotValue);
        result.setEvent_slot_time_str(DateUtils.getStrByDateLong(eventTimeSlotValue));

        result.setId(DateUtils.getStrByDateLong(eventTimeSlotValue,DateUtils.SECOND_PATERN) + "_"+ aLong);

        out.collect(result);
    }
}
