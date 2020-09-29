package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Member;

import java.util.List;

public interface MemberDao {
    /**
     * 查询所有
     * @return
     */
    List<Member> findAll();

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Member> selectByCondition(String queryString);

    /**
     * 添加会员
     * @param member
     */
    void add(Member member);

    /**
     * 通过id删除
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    Member findById(Integer id);

    /**
     * 通过手机号码查询会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 修改会员
     * @param member
     */
    void edit(Member member);

    /**
     * 统计在某个日期前为止，会员总数量
     * @param date
     * @return
     */
    Integer findMemberCountBeforeDate(String date);

    /**
     * 统计某个日期的新增会员数量
     * @param date
     * @return
     */
    Integer findMemberCountByDate(String date);

    /**
     *
     * @param date
     * @return
     */
    Integer findMemberCountAfterDate(String date);

    /**
     * 会员总数量
     * @return
     */
    Integer findMemberTotalCount();
}
