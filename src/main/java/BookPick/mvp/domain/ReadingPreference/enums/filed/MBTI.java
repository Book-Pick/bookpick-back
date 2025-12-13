package BookPick.mvp.domain.ReadingPreference.enums.filed;

public enum MBTI {


    ISTJ,
    ISFJ,
    INFJ,
    INTJ,
    ISTP,
    ISFP,
    INFP,
    INTP,
    ESTP,
    ESFP,
    ENFP,
    ENTP,
    ESTJ,
    ESFJ,
    ENFJ,
    ENTJ;

    public static boolean isValid(String value){
        try {
            MBTI.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
