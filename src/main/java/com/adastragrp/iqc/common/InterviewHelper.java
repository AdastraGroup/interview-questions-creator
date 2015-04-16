package com.adastragrp.iqc.common;


import com.adastragrp.iqc.entity.Question;
import com.adastragrp.iqc.entity.Interview;

import static com.adastragrp.iqc.entity.Question.QuestionType.TEXT_AREA;

public class InterviewHelper {

    public static boolean canBeValidateAutomatically(Interview interview){
        return interview.getQuestions().stream().allMatch(q -> canBeValidateAutomatically(q));
    }

    public static boolean canBeValidateAutomatically(Question question){
        if(TEXT_AREA.equals(question)){
            return false;
        }
        return true;
    }
}
