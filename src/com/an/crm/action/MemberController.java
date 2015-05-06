package com.an.crm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.crm.dao.MemberDao;
import com.an.crm.entity.Member;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员管理
 *
 * @author Karas 2012-3-8
 */
@Controller
@RequestMapping("/crm")
public class MemberController {

    private static final Logger logger = LoggerFactory
            .getLogger(MemberController.class);

    @Autowired
    private MemberDao memberDao;

    @RequestMapping(value = "/member/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", memberDao.selectList(mParam));
        result.put("count", memberDao.count(mParam));
        return result;

    }

    @RequestMapping(value = "/member/{id}", method = RequestMethod.GET)
    public Member load(@PathVariable("id") int id)
            throws BadRequestException {
        return memberDao.selectOne(id);
    }


    @RequestMapping(value = "/member", method = RequestMethod.POST)
    public Member insert(@RequestBody Member member) throws BadRequestException {
        if (memberDao.insert(member) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return member;
        }
    }

    @RequestMapping(value = "/member/{id}", method = RequestMethod.PUT)
    public Member update(@RequestBody Member member) throws BadRequestException {
        if (memberDao.update(member) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return member;
        }
    }


    @RequestMapping(value = "/member/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id)
            throws BadRequestException {
        if (memberDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }

    @RequestMapping(value = "/kv/member", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> loadKV()
            throws BadRequestException {
        return memberDao.selectKV();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequestException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView requestException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
