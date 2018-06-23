package com.noahhealth.bean.user;

import com.noahhealth.pojo.User;
import org.springframework.beans.BeanUtils;

/**
 * Created by zlren on 2017/7/16.
 */
public class UserExtend extends User {

    public String staffName;
    public String staffMgrName;
    public String status;

    /**
     * 拓展
     *
     * @param user
     * @param staffName
     * @param staffMgrName
     */
    public UserExtend(User user, String staffName, String staffMgrName, String status) {

        BeanUtils.copyProperties(user, this);
        this.staffName = staffName;
        this.staffMgrName = staffMgrName;
        this.status = status;
    }
}
