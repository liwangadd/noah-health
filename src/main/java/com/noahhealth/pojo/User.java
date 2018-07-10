package com.noahhealth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_num")
    private String memberNum;

    private String username;

    private String password;

    private String avatar;

    private String name;

    private String role;

    @Column(name = "staff_id")
    private Integer staffId;

    @Column(name = "staff_mgr_id")
    private Integer staffMgrId;

    private Date valid;

    /**
     * 出生日期
     */
    private String birth;

    /**
     * 性别
     */
    private String gender;

    /**
     * 身份证号
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 身体状况
     */
    @Column(name = "physical_condition")
    private String physicalCondition;

    /**
     * 婚姻状况
     */
    @Column(name = "marital_status")
    private String maritalStatus;

    /**
     * 医疗费别
     */
    @Column(name = "medical_care")
    private String medicalCare;

    /**
     * 医保定点医院
     */
    private String hospital;

    /**
     * 商业健康保险
     */
    private String insurance;

    /**
     * 过敏_药物
     */
    @Column(name = "allergy_drug")
    private String allergyDrug;

    /**
     * 过敏_其他
     */
    @Column(name = "allergy_others")
    private String allergyOthers;

    /**
     * 血型
     */
    @Column(name = "blood_type")
    private String bloodType;

    /**
     * 基础体温
     */
    @Column(name = "base_temperature")
    private String baseTemperature;

    /**
     * 呼吸
     */
    private String breath;

    /**
     * 血压
     */
    @Column(name = "blood_pressure")
    private String bloodPressure;

    /**
     * 心率
     */
    @Column(name = "heart_rate")
    private String heartRate;

    /**
     * 心律
     */
    @Column(name = "heart_rate2")
    private String heartRate2;

    /**
     * 身高
     */
    private String height;

    /**
     * 体重
     */
    private String weight;

    /**
     * 体重指数
     */
    @Column(name = "weight_rate")
    private String weightRate;

    /**
     * 腰围
     */
    @Column(name = "waist_circum")
    private String waistCircum;

    /**
     * 臂围
     */
    @Column(name = "arm_circum")
    private String armCircum;

    private String surgery;

    /**
     * 腰臂比
     */
    @Column(name = "waist_arm_rate")
    private String waistArmRate;

    /**
     * 家族史
     */
    private String family;

    /**
     * 主要疾病
     */
    private String disease;

    /**
     * 主要用药
     */
    private String medication;

    /**
     * 特殊事项
     */
    private String special;

}