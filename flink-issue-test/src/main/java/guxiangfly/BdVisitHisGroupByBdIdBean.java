package guxiangfly;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author guxiang02
 * @Date 2021/5/28
 **/
@Data
public class BdVisitHisGroupByBdIdBean implements Serializable {


    String id;

    Long company_id;

    Long bd_id;

    Long visit_num_total = 0L;

    Long visit_num_500m = 0L;

    Long visit_num_real = 0L;

    Long visit_num_phone = 0L;

    Long visit_num_hunt = 0L;

    Long visit_num_farm = 0L;

    Long visit_num_valid = 0L;

    Long visit_num_material = 0L;


    /**
     * 执行计算时间
     */
    Long process_time;

    /**
     * event_time
     */
    Long event_time;


    /**
     * event_time处理的整数时间
     */
    Long event_slot_time;


    /**
     * string格式event_time时间
     */
    String event_time_str;

    /**
     * string格式event_time时间
     */
    String event_slot_time_str;
}
