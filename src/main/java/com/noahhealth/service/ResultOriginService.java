package com.noahhealth.service;

import com.github.pagehelper.PageHelper;
import com.noahhealth.bean.Constant;
import com.noahhealth.bean.Identity;
import com.noahhealth.bean.origin.ResultOriginExtend;
import com.noahhealth.pojo.ResultOrigin;
import com.noahhealth.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * ResultOriginService
 * Created by zlren on 2017/6/12.
 */
@Service
@Slf4j
public class ResultOriginService extends BaseService<ResultOrigin> {

    @Autowired
    private UserService userService;

    @Autowired
    private OriginCategorySecondService originCategorySecondService;

    /**
     * 条件查询
     *
     * @param identity
     * @param pageNow
     * @param pageSize
     * @param status
     * @param userName
     * @param uploaderName
     * @param checkerName
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<ResultOrigin> queryResultOriginList(Integer userId, Identity identity, Integer pageNow, Integer
            pageSize, String status, String userName, String uploaderName, String checkerName, String memberNum,
                                                    Date beginTime, Date endTime, Integer secondId) {

        String identityId = identity.getId();
        String identityRole = identity.getRole();

        Example example = new Example(ResultOrigin.class);
        Example.Criteria originCriteria = example.createCriteria();

        example.setOrderByClause("time desc");

        if (userId != null) {
            originCriteria.andEqualTo("userId", userId);
        }

        {   // 时间和状态的筛选是统一的

            // 时间
            if (beginTime != null && endTime != null) {
                originCriteria.andBetween("time", beginTime, endTime);
            }

            // 状态
            Set<String> statusSet = this.userService.getStatusSetUnderRole(identity);
            if (!Validator.checkEmpty(status)) {
                Set<String> t = new HashSet<>();
                t.add(status);
                statusSet.retainAll(t);
            }
            originCriteria.andIn(Constant.STATUS, statusSet);
        }

        // 添加对资料类别的筛选，这个应该是统一的
        if (secondId != -1) {
            originCriteria.andEqualTo("secondId", secondId);
        }

        if (this.userService.checkAdmin(identityRole)) { // 系统管理员

            if (!Validator.checkEmpty(status)) {
                originCriteria.andEqualTo("status", status);
            }

            // if (!Validator.checkEmpty(userName)) {
            originCriteria.andIn("userId", this.userService.getMemberIdSetByNameAndMemberNumLike(userName, memberNum));
            // }

            // 上传者，档案部员工或者管理员
            if (!Validator.checkEmpty(uploaderName)) {
                originCriteria.andIn("uploaderId", this.userService.getIdSetByUserNameLikeAndRole(uploaderName,
                        "档案部员工"));
            }

            // 审核者可以是管理员，也可以是档案部主管
            if (!Validator.checkEmpty(checkerName)) {

                log.info("哈哈哈 checkerName: {}", checkerName);

                Set<Integer> adminSet = this.userService.getIdSetByUserNameLikeAndRole(checkerName, "管理员");
                Set<Integer> mgrSet = this.userService.getIdSetByUserNameLikeAndRole(checkerName, "档案部主管");
                adminSet.addAll(mgrSet);

                originCriteria.andIn("checkerId", adminSet);
            }

        } else if (this.userService.checkArchiverManager(identityRole)) { // 档案部主管

            // 档案部主管对应的档案部员工
            // 根据上传者筛选后，就不用筛选审核者了，反正都是自己的员工做的
            Set<Integer> archiverIdSet = this.userService.queryArchiverIdSetByArchiveMgrId(Integer.valueOf(identityId));
            if (!Validator.checkEmpty(uploaderName)) {
                archiverIdSet.retainAll(this.userService.getIdSetByUserNameLikeAndRole(uploaderName, Constant
                        .ARCHIVER));
            }
            originCriteria.andIn("uploaderId", archiverIdSet);

            Set<Integer> memberSet = this.userService.queryMemberIdSetUnderRole(identity);
            memberSet.retainAll(this.userService.getMemberIdSetByNameAndMemberNumLike(userName, memberNum));
            originCriteria.andIn("userId", memberSet);

        } else if (this.userService.checkArchiver(identityRole)) { // 档案部员工

            // 只能看自己
            originCriteria.andEqualTo("uploaderId", identityId);

        } else if (this.userService.checkAdviseManager(identityRole)) { // 顾问部主管

            // 重在对userId的筛选，挑出是自己的顾问员工对应的会员
            Set<Integer> memberSet = this.userService.queryMemberIdSetUnderRole(identity);

            memberSet.retainAll(this.userService.getMemberIdSetByNameAndMemberNumLike(userName, memberNum));
            originCriteria.andIn("userId", memberSet);

        } else if (this.userService.checkAdviser(identityRole)) { // 顾问部员工

            // 重在对userId的筛选，挑出是自己的顾问员工对应的会员
            Set<Integer> memberSet = this.userService.queryMemberIdSetUnderRole(identity);
            memberSet.retainAll(this.userService.getMemberIdSetByNameAndMemberNumLike(userName, memberNum));
            originCriteria.andIn("userId", memberSet);

        } else if (this.userService.checkMember(identityRole)) {

            // 重在对userId的筛选，挑出是自己的顾问员工对应的会员
            Set<Integer> memberSet = this.userService.queryMemberIdSetUnderRole(identity);
            originCriteria.andIn("userId", memberSet);
        }


        PageHelper.startPage(pageNow, pageSize);
        List<ResultOrigin> resultOriginList = this.getMapper().selectByExample(example);
        return resultOriginList;
    }


    /**
     * 拓展
     *
     * @param resultOriginList
     * @return
     */
    public List<ResultOriginExtend> extendFromResultOriginList(List<ResultOrigin> resultOriginList) {

        List<ResultOriginExtend> resultOriginExtendList = new ArrayList<>();

        resultOriginList.forEach(resultOrigin -> {

            String memberNumExtend = this.userService.queryById(resultOrigin.getUserId()).getMemberNum();
            String userNameExtend = this.userService.queryById(resultOrigin.getUserId()).getName();
            String checkerNameExtend = null;
            if (resultOrigin.getCheckerId() != null) {
                checkerNameExtend = this.userService.queryById(resultOrigin.getCheckerId()).getName();
            }
            String uploaderNameExtend = this.userService.queryById(resultOrigin.getUploaderId()).getName();

            String originCategorySecondName = "";
            if (resultOrigin.getSecondId() != null) {
                originCategorySecondName = this.originCategorySecondService.queryById(resultOrigin.getSecondId())
                        .getName();
            }

            ResultOriginExtend resultOriginExtend = new ResultOriginExtend(resultOrigin, memberNumExtend,
                    userNameExtend, checkerNameExtend, uploaderNameExtend, originCategorySecondName);

            resultOriginExtendList.add(resultOriginExtend);
        });

        return resultOriginExtendList;
    }

}
