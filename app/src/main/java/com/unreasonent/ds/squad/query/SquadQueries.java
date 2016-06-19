package com.unreasonent.ds.squad.query;

import com.unreasonent.ds.squad.api.Character;
import org.apache.ibatis.annotations.Param;

public interface SquadQueries {
    public void deleteSquad(@Param("userId") String userId);

    public void insertCharacter(@Param("userId") String userId, @Param("character") Character character);

    public SquadResult getSquad(@Param("userId") String userId);
}
