package com.noahhealth.service;

import com.noahhealth.pojo.OriginCategorySecond;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zlren on 17/7/19.
 */
@Service
public class OriginCategorySecondService extends BaseService<OriginCategorySecond> {

    public OriginCategorySecond queryByOriginName(String name){
        OriginCategorySecond record = new OriginCategorySecond();
        record.setName(name);
        List<OriginCategorySecond> originList = queryListByWhere(record);
        if(originList!=null&&originList.size()>0){
            return originList.get(0);
        }
        return null;
    }

}
