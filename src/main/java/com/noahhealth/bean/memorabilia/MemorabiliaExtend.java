package com.noahhealth.bean.memorabilia;

import com.noahhealth.pojo.HealthMemorabilia;
import org.springframework.beans.BeanUtils;


public class MemorabiliaExtend extends HealthMemorabilia {

    public String memberNum;
    public String userName;
    public String uploaderName;

    public MemorabiliaExtend(HealthMemorabilia memorabilia, String memberNum, String userName, String uploaderName) {
        BeanUtils.copyProperties(memorabilia, this);
        this.memberNum = memberNum;
        this.userName = userName;
        this.uploaderName = uploaderName;
    }
}
