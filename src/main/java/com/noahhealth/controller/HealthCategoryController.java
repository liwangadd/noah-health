package com.noahhealth.controller;

import com.github.pagehelper.PageInfo;
import com.noahhealth.bean.CommonResult;
import com.noahhealth.bean.Constant;
import com.noahhealth.bean.PageResult;
import com.noahhealth.pojo.*;
import com.noahhealth.service.HealthCategoryFirstService;
import com.noahhealth.service.HealthCategorySecondService;
import com.noahhealth.service.ResultHealthService;
import com.noahhealth.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zlren on 17/7/19.
 */
@RequestMapping("health_category")
@RestController
@Slf4j
public class HealthCategoryController {

    @Autowired
    private HealthCategoryFirstService healthCategoryFirstService;

    @Autowired
    private HealthCategorySecondService healthCategorySecondService;

    @Autowired
    private ResultHealthService resultHealthService;

    /**
     * 查询所有大类
     *
     * @return
     */
    @RequestMapping(value = "first", method = RequestMethod.GET)
    public CommonResult queryCategoriesFirst() {
        List<HealthCategoryFirst> healthCategoryFirsts = this.healthCategoryFirstService.queryAll();
        return CommonResult.success("查询成功", healthCategoryFirsts);
    }

    /**
     * 根据id查询
     *
     * @param firstId 查询大类
     * @return
     */
    @RequestMapping(value = "first/{firstId}", method = RequestMethod.GET)
    public CommonResult queryCategoryFirstById(@PathVariable("firstId") Integer firstId) {

        HealthCategoryFirst categoryFirst = this.healthCategoryFirstService.queryById(firstId);

        if (categoryFirst == null) {
            return CommonResult.failure("查询失败，不存在的大类");
        }

        return CommonResult.success("查询成功", categoryFirst);
    }

    /**
     * 删除一个大类
     *
     * @param firstId
     * @return
     */
    @RequestMapping(value = "first/{firstId}", method = RequestMethod.DELETE)
    public CommonResult deleteCategoryFirst(@PathVariable("firstId") Integer firstId) {

        HealthCategorySecond categorySecond = new HealthCategorySecond();
        categorySecond.setFirstId(firstId);

        if (this.healthCategoryFirstService.queryById(firstId) == null) {
            return CommonResult.failure("删除失败，不存在的大类");
        }
        List<HealthCategorySecond> categorySeconds = this.healthCategorySecondService.queryListByWhere(categorySecond);
        if (categorySeconds != null && categorySeconds.size() > 0) {
            return CommonResult.failure("存在亚类，无法删除");
        }

        this.healthCategoryFirstService.deleteById(firstId);
        return CommonResult.success("删除成功");
    }

    /**
     * 修改一个大类
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "first/{firstId}", method = RequestMethod.PUT)
    public CommonResult updateCategoryFist(@RequestBody Map<String, Object> params, @PathVariable("firstId") Integer
            firstId) {

        if (this.healthCategoryFirstService.queryById(firstId) == null) {
            return CommonResult.failure("修改失败，不存在的大类");
        }

        String type = (String) params.get(Constant.TYPE);
        String name = (String) params.get(Constant.NAME);

        if (Validator.checkEmpty(type) || Validator.checkEmpty(name)) {
            return CommonResult.failure("添加失败，信息不完整");
        }

        HealthCategoryFirst categoryFirst = new HealthCategoryFirst();
        categoryFirst.setType(type);
        categoryFirst.setName(name);

        if (this.healthCategoryFirstService.queryOne(categoryFirst) != null) {
            return CommonResult.failure("修改失败，和其他大类重复");
        }

        // 设置id，根据id去改
        categoryFirst.setId(firstId);
        this.healthCategoryFirstService.update(categoryFirst);

        return CommonResult.success("修改成功");
    }

    /**
     * 添加一个大类
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "first", method = RequestMethod.POST)
    public CommonResult addCategoryFist(@RequestBody Map<String, String> params) {

        String type = params.get(Constant.TYPE);
        String name = params.get(Constant.NAME);

        if (Validator.checkEmpty(type) || Validator.checkEmpty(name)) {
            return CommonResult.failure("添加失败，信息不完整");
        }

        HealthCategoryFirst categoryFirst = new HealthCategoryFirst();
        categoryFirst.setName(name);
        categoryFirst.setType(type);

        if (this.healthCategoryFirstService.queryOne(categoryFirst) != null) {
            return CommonResult.failure("已经存在");
        }

        this.healthCategoryFirstService.save(categoryFirst);
        return CommonResult.success("添加成功");
    }

    /**
     * 添加一个亚类
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "second", method = RequestMethod.POST)
    public CommonResult addCategorySecond(@RequestBody Map<String, Object> params) {

        Integer firstId = (Integer) params.get(Constant.FIRST_ID);
        String name = (String) params.get(Constant.NAME);

        if (firstId == null || Validator.checkEmpty(name)) {
            return CommonResult.failure("添加失败，信息不完整");
        }

        HealthCategorySecond categorySecond = new HealthCategorySecond();
        categorySecond.setFirstId(firstId);
        categorySecond.setName(name);

        if (this.healthCategorySecondService.queryOne(categorySecond) != null) {
            return CommonResult.failure("添加失败，已经存在的亚类");
        }

        this.healthCategorySecondService.save(categorySecond);

        return CommonResult.success("添加成功");
    }

    /**
     * 根据firstId分页查询亚类
     *
     * @param params
     * @param firstId
     * @return
     */
    @RequestMapping(value = "/second/{firstId}/list", method = RequestMethod.POST)
    public CommonResult querySecondCategoryList(@RequestBody Map<String, Integer> params, @PathVariable("firstId")
            Integer firstId) {

        Integer pageNow = params.get(Constant.PAGE_NOW);
        Integer pageSize = params.get(Constant.PAGE_SIZE);

        HealthCategorySecond categorySecond = new HealthCategorySecond();
        categorySecond.setFirstId(firstId);

        PageInfo<HealthCategorySecond> categorySecondPageInfo = this.healthCategorySecondService.queryPageListByWhere(pageNow,
                pageSize, categorySecond);
        return CommonResult.success("查询成功", new PageResult(categorySecondPageInfo));
    }

    /**
     * 根据id查询
     *
     * @param secondId
     * @return
     */
    @RequestMapping(value = "second/{secondId}", method = RequestMethod.GET)
    public CommonResult queryCategorySecondById(@PathVariable("secondId") Integer secondId) {

        HealthCategorySecond categorySecond = this.healthCategorySecondService.queryById(secondId);
        if (categorySecond == null) {
            return CommonResult.failure("查询失败，不存在的亚类");
        }

        return CommonResult.success("查询成功", categorySecond);
    }

    /**
     * 删除一个亚类
     *
     * @param secondId
     * @return
     */
    @RequestMapping(value = "second/{secondId}", method = RequestMethod.DELETE)
    public CommonResult deleteCategorySecond(@PathVariable("secondId") Integer secondId) {
        ResultHealth resultHealth = new ResultHealth();
        resultHealth.setSecondId(secondId);
        List<ResultHealth> resultHealths = resultHealthService.queryListByWhere(resultHealth);
        if(this.healthCategorySecondService.queryById(secondId)==null){
            return CommonResult.failure("删除失败，不存在的大类");
        }
        if (resultHealths != null && resultHealths.size() > 0) {
            return CommonResult.failure("存在该类别的健康摘要，无法删除");
        }
        this.healthCategorySecondService.deleteById(secondId);
        return CommonResult.success("删除成功");
    }


    /**
     * 修改亚类
     *
     * @param params
     * @param secondId
     * @return
     */
    @RequestMapping(value = "second/{secondId}", method = RequestMethod.PUT)
    public CommonResult updateCategorySecond(@RequestBody Map<String, Object> params, @PathVariable("secondId")
            Integer secondId) {

        if (this.healthCategorySecondService.queryById(secondId) == null) {
            return CommonResult.failure("修改失败，不存在的亚类");
        }

        Integer firstId = (Integer) params.get(Constant.FIRST_ID);
        String name = (String) params.get(Constant.NAME);

        HealthCategorySecond categorySecond = new HealthCategorySecond();
        if (firstId == null || Validator.checkEmpty(name)) {
            return CommonResult.failure("修改失败，信息不完整");
        }

        categorySecond.setFirstId(firstId);
        categorySecond.setName(name);

        if (this.healthCategorySecondService.queryOne(categorySecond) != null) {
            return CommonResult.failure("修改失败，已经存在的亚类");
        }

        // 设置id，根据id去改
        categorySecond.setId(secondId);

        this.healthCategorySecondService.update(categorySecond);

        return CommonResult.success("修改成功");
    }


    /**
     * 分级查询
     */
    @RequestMapping(value = "level", method = RequestMethod.GET)
    public CommonResult queryHealthCategoryLevel() {

        Example example = new Example(HealthCategoryFirst.class);
        example.orderBy("id asc");

        List<HealthCategoryFirst> healthCategoryFirstList = this.healthCategoryFirstService.getMapper().selectByExample
                (example);


        Map<String, List<HealthCategorySecond>> result = new LinkedHashMap<>();

        // 用for是保证顺序
        for (HealthCategoryFirst healthCategoryFirst : healthCategoryFirstList) {

            Integer firstId = healthCategoryFirst.getId();

            HealthCategorySecond second = new HealthCategorySecond();
            second.setFirstId(firstId);
            List<HealthCategorySecond> healthCategorySecondList = this.healthCategorySecondService.queryListByWhere
                    (second);

            // 只返回有亚类的健康摘要大类
            if (healthCategorySecondList != null && healthCategorySecondList.size() > 0) {
                result.put(healthCategoryFirst.getName(), healthCategorySecondList);
            }
        }

        return CommonResult.success("查询成功", result);
    }
}
