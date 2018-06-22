package com.noahhealth.service;

import com.noahhealth.bean.input.ResultInputDetailExtend;
import com.noahhealth.pojo.CategoryThird;
import com.noahhealth.pojo.ResultInput;
import com.noahhealth.pojo.ResultInputDetail;
import com.noahhealth.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlren on 2017/6/29.
 */
@Service
public class ResultInputDetailService extends BaseService<ResultInputDetail> {

    @Autowired
    private CategoryThirdService categoryThirdService;

    @Autowired
    private ResultInputService resultInputService;

    /**
     * 将list中的数据插入数据库
     *
     * @param dataToSaveList
     */
    public void save(List<ResultInputDetail> dataToSaveList, String note) {
        dataToSaveList.forEach(detail -> this.getMapper().updateByPrimaryKeySelective(detail));
        if(!Validator.checkEmpty(note)) {
            Integer inputId = queryById(dataToSaveList.get(0).getId()).getResultInputId();
            ResultInput input = this.resultInputService.queryById(inputId);
            input.setNote(note);
            this.resultInputService.update(input);
        }
    }

    /**
     * resultInputDetailList拓展为resultInputDetailExtendList
     *
     * @param resultInputDetailList
     * @return
     */
    public List<ResultInputDetailExtend> extendFromResultInputDetailList(List<ResultInputDetail>
                                                                                 resultInputDetailList) {
        List<ResultInputDetailExtend> resultInputDetailExtendList = new ArrayList<>();

        resultInputDetailList.forEach(resultInputDetail -> {

            CategoryThird categoryThird = this.categoryThirdService.queryById(resultInputDetail.getThirdId());
            String thirdName = categoryThird.getName();
            String referenceValue = categoryThird.getReferenceValue();
            String enShort = categoryThird.getEnShort();

            String hospital = this.resultInputService.queryById(resultInputDetail.getResultInputId()).getHospital();

            resultInputDetailExtendList.add(new ResultInputDetailExtend(resultInputDetail, thirdName, referenceValue,
                    hospital, enShort));
        });

        return resultInputDetailExtendList;
    }
}
