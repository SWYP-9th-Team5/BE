package swyp.team5.greening.mbti.domain.entity;

import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.mbti.exception.MbtiExceptionMessage;

public enum MbtiType {
    INF, ENF,
    INT, ENT,
    ISF, ESF,
    IST, EST;


    public static MbtiType of(String input) {
        try {
            return MbtiType.valueOf(input);
        } catch(IllegalArgumentException e) {
            throw new GreeningGlobalException(MbtiExceptionMessage.NOT_FOUND_MBTI_TYPE);
        }
    }
}
