package com.noahhealth.controller;

import com.github.pagehelper.PageInfo;
import com.noahhealth.bean.CommonResult;
import com.noahhealth.bean.Constant;
import com.noahhealth.bean.PageResult;
import com.noahhealth.pojo.CategoryThird;
import com.noahhealth.pojo.ResultInputDetail;
import com.noahhealth.service.CategoryThirdService;
import com.noahhealth.service.ResultInputDetailService;
import com.noahhealth.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 检查项目
 * Created by zlren on 2017/6/14.
 */
@RequestMapping("third")
@RestController
@Slf4j
public class CategoryThirdController {

    @Autowired
    private CategoryThirdService categoryThirdService;

    @Autowired
    private ResultInputDetailService resultInputDetailService;

    /**
     * 根据id查询
     *
     * @param thirdId
     * @return
     */
    @RequestMapping(value = "{thirdId}", method = RequestMethod.GET)
    public CommonResult queryCategoryThirdById(@PathVariable("thirdId") Integer thirdId) {

        CategoryThird categoryThird = categoryThirdService.queryById(thirdId);
        if (categoryThird == null) {
            return CommonResult.failure("查询失败，不存在的检查项目");
        }

        return CommonResult.success("查询成功", categoryThird);
    }


    /**
     * 添加检查项目
     *
     * @param params
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public CommonResult addCategoryThird(@RequestBody Map<String, Object> params) {

        Integer secondId = (Integer) params.get(Constant.SECOND_ID);
        String name = (String) params.get(Constant.NAME);
        String referenceValue = (String) params.get(Constant.REFERENCE_VALUE);
        String enShort = (String) params.get("enShort");

        // 没有检查hospital是否为空
        if (secondId == null || Validator.checkEmpty(name) || Validator
                .checkEmpty(referenceValue)) {
            return CommonResult.failure("添加失败，信息不完整");
        }

        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setSecondId(secondId);
        categoryThird.setName(name);
        categoryThird.setEnShort(enShort);

        if (this.categoryThirdService.queryOne(categoryThird) != null) {
            return CommonResult.failure("添加失败，已经存在的检查项目");
        }

        categoryThird.setReferenceValue(referenceValue);
        this.categoryThirdService.save(categoryThird);

        return CommonResult.success("添加检查项目成功");
    }


    /**
     * 删除检查项目
     * 删掉一个检查项目，可能存在已经录入的数据
     * 比如血常规有10个检查项目，其中有红细胞个数，白细胞个数
     * 小明录入了一张血常规的检查表，其中显示红细胞个数为3
     * 有一天工作人员删掉了血常规中的红细胞个数这个检查项，小明已经录入的数据怎么办？删掉吗？？fixme
     *
     * @param thirdId
     * @return
     */
    @RequestMapping(value = "{thirdId}", method = RequestMethod.DELETE)
    public CommonResult deleteCategoryThird(@PathVariable("thirdId") Integer thirdId) {

        if (this.categoryThirdService.queryById(thirdId) == null) {
            return CommonResult.failure("删除失败，不存在的检查项目");
        }

        ResultInputDetail record = new ResultInputDetail();
        record.setThirdId(thirdId);
        if (this.resultInputDetailService.queryCountByWhere(record) > 0) {
            return CommonResult.failure("删除失败，此检查项在检查记录中存在");
        }

        this.categoryThirdService.deleteById(thirdId);
        return CommonResult.success("删除检查项目成功");
    }


    /**
     * 修改检查项目
     *
     * @param params
     * @param thirdId
     * @return
     */
    @RequestMapping(value = "{thirdId}", method = RequestMethod.PUT)
    public CommonResult updateCategoryThird(@RequestBody Map<String, Object> params, @PathVariable("thirdId") Integer
            thirdId) {

        if (this.categoryThirdService.queryById(thirdId) == null) {
            return CommonResult.failure("修改失败，不存在的检查项目");
        }

        Integer secondId = (Integer) params.get(Constant.SECOND_ID);
        String name = (String) params.get(Constant.NAME);
        String referenceValue = (String) params.get(Constant.REFERENCE_VALUE);
        String enShort = (String) params.get("enShort");

        // 没有检查hospital是否为空
        if (secondId == null || Validator.checkEmpty(name) || Validator.checkEmpty(referenceValue)) {
            return CommonResult.failure("修改失败，信息不完整");
        }

        // secondId和name可以唯一标识一个third
        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setSecondId(secondId);
        categoryThird.setName(name);
        categoryThird.setEnShort(enShort);

        // 修改非（secondId和name）的时候不能被判重
        CategoryThird exist = this.categoryThirdService.queryOne(categoryThird);
        if (exist != null && !Objects.equals(exist.getId(), thirdId)) {
            return CommonResult.failure("修改失败，已经存在的检查项目");
        }

        // 根据id去改
        categoryThird.setId(thirdId);
        categoryThird.setReferenceValue(referenceValue);

        this.categoryThirdService.update(categoryThird);

        return CommonResult.success("修改成功");
    }


    /**
     * 根据secondId分页查询亚亚类
     *
     * @param params
     * @param secondId
     * @return
     */
    @RequestMapping(value = "{secondId}/list", method = RequestMethod.POST)
    public CommonResult queryThirdCategoryListBySecondId(@RequestBody Map<String, Integer> params, @PathVariable
            ("secondId")
            Integer secondId) {

        Integer pageNow = params.get(Constant.PAGE_NOW);
        Integer pageSize = params.get(Constant.PAGE_SIZE);

        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setSecondId(secondId);

        PageInfo<CategoryThird> categoryThirdPageInfo = this.categoryThirdService.queryPageListByWhere(pageNow,
                pageSize, categoryThird);

        return CommonResult.success("查询成功", new PageResult(categoryThirdPageInfo));
    }


    /**
     * 根据亚类id查询所有的检查项目
     * 与分页查询的区别是get和post
     *
     * @param secondId
     * @return
     */
    @RequestMapping(value = "{secondId}/list", method = RequestMethod.GET)
    public CommonResult queryThirdCategoryListBySecondId(@PathVariable("secondId") Integer secondId) {
        CategoryThird record = new CategoryThird();
        record.setSecondId(secondId);

        List<CategoryThird> categoryThirdList = this.categoryThirdService.queryListByWhere(record);

        if (categoryThirdList != null && categoryThirdList.size() > 0) {
            return CommonResult.success("查询成功", categoryThirdList);
        }

        return CommonResult.failure("查询失败，不存在检查项目");
    }
}
