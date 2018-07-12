package com.noahhealth.pojo;

import com.noahhealth.util.Validator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.Map;

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

    public void copyMap2User(Map<String, String> params){
        // 新增的那一坨
        String birth = params.get("birth"); // 出生日期
        String gender = params.get("gender"); // 性别
        String bloodType = params.get("bloodType"); // 血型
        String baseTemperature = params.get("baseTemperature"); // 基础体温
        String breath = params.get("breath"); //呼吸
        String bloodPressure = params.get("bloodPressure"); // 血压
        String heartRate = params.get("heartRate"); // 心率
        String heartRate2 = params.get("heartRate2"); // 心律
        String height = params.get("height"); // 身高
        String weight = params.get("weight");//体重
        String weightRate = params.get("weightRate");//体重指数
        String waistCircum = params.get("waistCircum");//腰围
        String armCircum = params.get("armCircum");//臂围
        String waistArmRate = params.get("waistArmRate");//腰臂比
        String physicalCondition = params.get("physicalCondition"); // 身体状况
        String medicalCare = params.get("medicalCare"); // 医疗费别
        String hospital = params.get("hospital"); // 医保定点医院
        String insurance = params.get("insurance"); // 商业健康保险
        String allergyDrug = params.get("allergyDrug"); // 过敏_药物
        String surgery = params.get("surgery");//手术史
        String family = params.get("family");//家族史
        String disease = params.get("disease");//主要疾病
        String medication = params.get("medication");//主要用药
        String special = params.get("special");//特殊事项
        if (birth != null) {
            this.setBirth(birth);
        }
        if (!Validator.checkEmpty(gender)) {
            this.setGender(gender);
        }
        if (!Validator.checkEmpty(bloodType)) {
            this.setBloodType(bloodType);
        }
        if (!Validator.checkEmpty(baseTemperature)) {
            this.setBaseTemperature(baseTemperature);
        }
        if (!Validator.checkEmpty(breath)) {
            this.setBreath(breath);
        }
        if (!Validator.checkEmpty(bloodPressure)) {
            this.setBloodPressure(bloodPressure);
        }
        if (!Validator.checkEmpty(physicalCondition)) {
            this.setPhysicalCondition(physicalCondition);
        }
        if (!Validator.checkEmpty(heartRate)) {
            this.setHeartRate(heartRate);
        }
        if (!Validator.checkEmpty(heartRate2)) {
            this.setHeartRate2(heartRate2);
        }
        if (!Validator.checkEmpty(height)) {
            this.setHeight(height);
        }
        if (!Validator.checkEmpty(weight)) {
            this.setWeight(weight);
        }
        if (!Validator.checkEmpty(weightRate)) {
            this.setWeightRate(weightRate);
        }
        if (!Validator.checkEmpty(waistCircum)) {
            this.setWaistCircum(waistCircum);
        }
        if (!Validator.checkEmpty(armCircum)) {
            this.setArmCircum(armCircum);
        }
        if (!Validator.checkEmpty(waistArmRate)) {
            this.setWaistArmRate(waistArmRate);
        }
        if (!Validator.checkEmpty(medicalCare)) {
            this.setMedicalCare(medicalCare);
        }
        if (!Validator.checkEmpty(hospital)) {
            this.setHospital(hospital);
        }
        if (!Validator.checkEmpty(insurance)) {
            this.setInsurance(insurance);
        }
        if (!Validator.checkEmpty(allergyDrug)) {
            this.setAllergyDrug(allergyDrug);
        }
        if(!Validator.checkEmpty(family)) {
            this.setFamily(family);
        }
        if(!Validator.checkEmpty(disease)) {
            this.setDisease(disease);
        }
        if(!Validator.checkEmpty(special)) {
            this.setSpecial(special);
        }
        if(!Validator.checkEmpty(medication)) {
            this.setMedication(medication);
        }
        if(!Validator.checkEmpty(surgery)) {
            this.setSurgery(surgery);
        }
    }

}