package com.loginbox.app.directory.internal;

import com.loginbox.app.identity.User;
import com.loginbox.app.identity.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface InternalDirectoryQueries {
    public User insertUser(
            @Param("directory") InternalDirectory directory,
            @Param("userInfo") UserInfo userInfo);
}
