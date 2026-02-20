package com.gasmax.auth.mapper;

import com.gasmax.auth.model.AppUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AppUserMapper {

    /**
     * ID + PW로 사용자 목록 조회 (동일 계정으로 다중 회사 가입 가능)
     */
    List<AppUser> findByUserIdAndPassword(
            @Param("userId") String userId,
            @Param("password") String password
    );

    /**
     * UUID + ID + PW로 사용자 목록 조회 (동일 기기로 다중 회사 가입 가능)
     */
    List<AppUser> findByUserIdPasswordAndUuid(
            @Param("userId") String userId,
            @Param("password") String password,
            @Param("uuid") String uuid
    );

    int countByUuid(@Param("uuid") String uuid);

    /**
     * 영업소 코드 목록 조회 (회사별 catalog 사용)
     * 기존: GASMAX.AppUser.Select.AreaCode
     */
    List<Map<String, String>> findAreaCodes(@Param("catalogName") String catalogName);

    /**
     * 지역번호 목록 조회 (회사별 catalog 사용)
     * 기존: GASMAX.AppUser.Select.PhoneAreaNumber
     */
    List<Map<String, String>> findPhoneAreaNumbers(@Param("catalogName") String catalogName);

    int insertAppUser(AppUser appUser);

    int updateLastLoginDate(
            @Param("macNumber") String macNumber,
            @Param("areaSeq") String areaSeq
    );
}
