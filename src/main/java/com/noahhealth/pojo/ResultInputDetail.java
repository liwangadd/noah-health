package com.noahhealth.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Table(name = "result_input_detail")
public class ResultInputDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "result_input_id")
    private Integer resultInputId;

    @Column(name = "third_id")
    private Integer thirdId;

    private String value;

    /**
     * 是否异常
     */
    private Boolean normal;

    private String note;
}