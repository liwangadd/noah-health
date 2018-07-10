package com.noahhealth.service;

import com.github.pagehelper.PageHelper;
import com.noahhealth.bean.Identity;
import com.noahhealth.bean.memorabilia.MemorabiliaExtend;
import com.noahhealth.pojo.HealthMemorabilia;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class HealthMemorabiliaService extends BaseService<HealthMemorabilia> {

    @Autowired
    private UserService userService;

    public List<HealthMemorabilia> queryMemorabiliaList(Identity identity, Integer pageStart, Integer pageSize) {
        String identityId = identity.getId();
        String identityRole = identity.getRole();

        Example example = new Example(HealthMemorabilia.class);
        Example.Criteria originCriteria = example.createCriteria();
        example.setOrderByClause("upload_time desc");

        if (this.userService.checkArchiverManager(identityRole)) { // 档案部主管

            // 档案部主管对应的档案部员工
            // 根据上传者筛选后，就不用筛选审核者了，反正都是自己的员工做的
            Set<Integer> archiverIdSet = this.userService.queryArchiverIdSetByArchiveMgrId(Integer.valueOf(identityId));

            originCriteria.andIn("uploaderId", archiverIdSet);
        } else if (this.userService.checkArchiver(identityRole)) { // 档案部员工

            // 只能看自己
            originCriteria.andEqualTo("uploaderId", identityId);

        } else  {

            // 重在对userId的筛选，挑出是自己的顾问员工对应的会员
            Set<Integer> memberSet = this.userService.queryMemberIdSetUnderRole(identity);
            originCriteria.andIn("userId", memberSet);
        }
        PageHelper.startPage(pageStart, pageSize);
        return this.getMapper().selectByExample(example);
    }

    public List<MemorabiliaExtend> extendFromMemorabiliaList(List<HealthMemorabilia> healthMemorabiliaList) {

        List<MemorabiliaExtend> resultOriginExtendList = new ArrayList<>();

        healthMemorabiliaList.forEach(memorabilia -> {
            String memberNumExtend = this.userService.queryById(memorabilia.getUserId()).getMemberNum();
            String userNameExtend = this.userService.queryById(memorabilia.getUserId()).getName();
            String uploaderNameExtend = this.userService.queryById(memorabilia.getUploadId()).getName();

            MemorabiliaExtend resultOriginExtend = new MemorabiliaExtend(memorabilia, memberNumExtend,
                    userNameExtend, uploaderNameExtend);

            resultOriginExtendList.add(resultOriginExtend);
        });

        return resultOriginExtendList;

    }
}
