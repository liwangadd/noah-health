package com.noahhealth.controller;

import com.github.pagehelper.PageInfo;
import com.noahhealth.bean.CommonResult;
import com.noahhealth.bean.Constant;
import com.noahhealth.bean.Identity;
import com.noahhealth.bean.PageResult;
import com.noahhealth.bean.memorabilia.MemorabiliaExtend;
import com.noahhealth.pojo.HealthMemorabilia;
import com.noahhealth.service.HealthMemorabiliaService;
import com.noahhealth.service.UserService;
import com.noahhealth.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("memorabilia")
@Slf4j
public class HealthMemorabiliaController {

    @Autowired
    private HealthMemorabiliaService healthMemorabiliaService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public CommonResult addMemorabilia(HttpSession session, @RequestBody Map<String, Object> params) {
        Identity identity = (Identity) session.getAttribute(Constant.IDENTITY);
        Integer uploadId = Integer.valueOf(identity.getId());

        Integer userId = (Integer) params.get(Constant.USER_ID);
        String title = (String) params.get("title");
        String content = (String) params.get("content");

        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse((String) params.get(Constant.TIME));
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonResult.failure("添加失败，日期解析错误");
        }
        if (Validator.checkEmpty(title) || Validator.checkEmpty(content)) {
            return CommonResult.failure("添加信息不完整");
        }
        HealthMemorabilia memorabilia = new HealthMemorabilia();
        memorabilia.setUserId(userId);
        memorabilia.setTitle(title);
        memorabilia.setUploadTime(date);
        memorabilia.setContent(content);
        memorabilia.setUploadId(uploadId);
        this.healthMemorabiliaService.save(memorabilia);
        return CommonResult.success("添加成功", memorabilia.getId());
    }

    @RequestMapping(method = RequestMethod.PUT)
    public CommonResult editMemorabilia(@RequestBody Map<String, Object> params) {
        Integer memorabiliaId = (Integer) params.get("memorabiliaId");
        String title = (String) params.get("title");
        String content = (String) params.get("content");

        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse((String) params.get(Constant.TIME));
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonResult.failure("修改失败，日期解析错误");
        }
        HealthMemorabilia memorabilia = this.healthMemorabiliaService.queryById(memorabiliaId);
        if (!Validator.checkEmpty(title)) {
            memorabilia.setTitle(title);
        }
        if (!Validator.checkEmpty(content)) {
            memorabilia.setContent(content);
        }
        memorabilia.setUploadTime(date);
        this.healthMemorabiliaService.update(memorabilia);
        return CommonResult.success("修改成功");
    }

    @RequestMapping(value = "{memorabiliaId}", method = RequestMethod.DELETE)
    public CommonResult deleteMemorabilia(@PathVariable("memorabiliaId") Integer memorabiliaId) {
        if (healthMemorabiliaService.queryById(memorabiliaId) == null) {
            return CommonResult.failure("删除失败，不存在的记录");
        }
        if (memorabiliaId != null && memorabiliaId > 0) {
            this.healthMemorabiliaService.deleteById(memorabiliaId);
        }
        return CommonResult.success("删除成功");
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResult listMemorabilia(@RequestBody Map<String, Object> params, HttpSession session) {
        Identity identity = (Identity) session.getAttribute(Constant.IDENTITY);
        if (!this.userService.checkValid(identity.getId())) {
            CommonResult.failure("过期无效用户");
        }
        Integer pageStart = (Integer) params.get(Constant.PAGE_NOW);
        Integer pageSize = (Integer) params.get(Constant.PAGE_SIZE);

        List<HealthMemorabilia> healthMemorabiliaList = this.healthMemorabiliaService.queryMemorabiliaList(identity, pageStart, pageSize);
        System.out.println("haha--" + healthMemorabiliaList);
        PageResult pageResult = new PageResult(new PageInfo<>(healthMemorabiliaList));

        List<MemorabiliaExtend> memorabiliaExtends = this.healthMemorabiliaService.extendFromMemorabiliaList(healthMemorabiliaList);
        pageResult.setData(memorabiliaExtends);
        return CommonResult.success("查询成功", pageResult);
    }

}
