package com.timss.pf.dao;

import com.timss.pf.bean.PersonInfo;
import com.yudean.mvc.dao.abstr.AbstractDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PersonInfoDao extends AbstractDao<PersonInfo> {
    void updateJobStatus(PersonInfo personInfo);

    List<PersonInfo> queryAllPersonInfo(@Param("inputStr") String inputStr);

    List<PersonInfo> queryAllPersonInfoByKw(@Param("inputStr") String inputStr);

    List<PersonInfo> queryPersonInfoByUserId(@Param("userId") String var1, @Param("siteid") String var2);

    List<PersonInfo> queryPersoninfoIdcard(String idCard);

    List<PersonInfo> queryAllPersonInfoByHire(@Param("inputStr") String inputStr);


    List<PersonInfo> queryPersonInfoByJobStatus(@Param("inputStr") String inputStr);

    List<PersonInfo> queryPersonInfoAll(@Param("inputStr") String inputStr);
}
