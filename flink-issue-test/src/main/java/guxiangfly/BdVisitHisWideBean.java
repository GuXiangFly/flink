package guxiangfly;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 拜访团长记录表
 * @date 2021-05-28
 */
@Data
public class BdVisitHisWideBean implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    private Long id;

    /**
     * 创建时间
     */
    private Long ctime;

    /**
     * 更新时间
     */
    private Long utime;

    /**
     * 团长passportid
     */
    private Long passport_id;

    /**
     * bdid
     */
    private Long bd_id;

    /**
     * mis英文拼音
     */
    private String mis_code;

    /**
     *  poi_id
     */
    private Long poi_id;

    /**
     * 拜访门店类型
     */
    private Integer poi_category;

    /**
     * 拜访方式 1-地推 2-电话外呼
     */
    private Integer type;

    /**
     * 1-开拓拜访 2-维护拜访
     */
    private Integer visit_type;

    /**
     * 拜访日期
     */
    private Long visit_date;

    /**
     * 拜访目的
     */
    private String visit_purposes;

    /**
     * 物料类型
     */
    private String visit_material_types;

    /**
     * 拜访结果 1-已注册 2-有意向 3-观望中 4-已拒绝
     */
    private Integer visit_result;

    /**
     * 拒绝原因
     */
    private String refuse_reasons;

    /**
     * 小区名称
     */
    private String area_name;

    /**
     * 团长是否有社区经验 1-是 2-否
     */
    private Integer is_rookie;

    /**
     * 团长手机号
     */
    private String phone;

    /**
     * bd添加拜访记录时与门店的距离（单位：米）
     */
    private Integer distance;

    /**
     * 备注信息
     */
    private String comment;

    /**
     * 是否有效（0:无效，1:有效）
     */
    private Integer valid;

    /**
     * 是否加入其他团购
     */
    private Integer is_join_other_group;

    /**
     * 其他团购名称
     */
    private String other_group_type;

    /**
     * 是否有微信群
     */
    private Integer has_wchat_group;

    /**
     * 微信群类型
     */
    private Integer wchat_group_type;

    /**
     * 微信群规模
     */
    private Integer wchat_group_scale;

    /**
     * 团长群的群人数
     */
    private Integer wchat_group_count;

    /**
     * 上月收入
     */
    private Integer last_month_income;

    /**
     * 是否有冷冻设备：0：无，1：有
     */
    private Integer has_freezer_machine;

    /**
     * 是否有冷藏设备：0：无，1：有
     */
    private Integer has_refrigerate_machine;

    /**
     * 商品是否在冷冻设备：0：无，1：有
     */
    private Integer goods_has_in_freezer;

    /**
     * 商品是否在冷藏设备：0：无，1：有
     */
    private Integer goods_has_in_refrigerate;

    /**
     * 线索id
     */
    private Long clue_id;

    /**
     * 拜访门店一级类型
     */
    private Integer visit_poi_first_type;

    /**
     * 拜访门店二级类型
     */
    private Integer visit_poi_second_type;

    /**
     * 社区团购经验(经营时长)
     */
    private Integer group_experience;

    /**
     * 各团购品牌销售件数
     */
    private String group_sale_num;

    /**
     * 团长手机号token
     */
    private String phone_token;


    private String group_head_id;

    /**
     * company_id
     */
    private Long company_id;

    /**
     * gh_id
     */
    private Long gh_id;


    /**
     * gh_poi_id
     */
    private Long gh_poi_id;



}