package com.example.studentmanagement.CommonObject;

import com.example.studentmanagement.Enitities.DeadlineEnitity;
import com.example.studentmanagement.Enitities.DeadlineStudentEnitity;
import com.example.studentmanagement.Enitities.GradeEnitity;
import com.example.studentmanagement.Enitities.UserEnitity;

import java.time.LocalDate;

public class ValidateDeadlineSubmitResult {
    private boolean isValidate;
    private Integer code;

    public ValidateDeadlineSubmitResult() {
    }

    public ValidateDeadlineSubmitResult(boolean isValidate, Integer code) {
        this.isValidate = isValidate;
        this.code = code;
    }

    public boolean isValidate() {
        return isValidate;
    }

    public final static ValidateDeadlineSubmitResult HAS_POINT = new ValidateDeadlineSubmitResult(false, 1);
    public final static ValidateDeadlineSubmitResult SUBMITTED = new ValidateDeadlineSubmitResult(false, 2);
    public final static ValidateDeadlineSubmitResult DEADLINE_NOT_OPEN = new ValidateDeadlineSubmitResult(false, 3);
    public final static ValidateDeadlineSubmitResult DEADLINE_EXPIRED = new ValidateDeadlineSubmitResult(false, 4);
    public final static ValidateDeadlineSubmitResult VALID_TO_MODIFY = new ValidateDeadlineSubmitResult(true, 5);

    public final static ValidateDeadlineSubmitResult isValidDeadlineToSubmit(DeadlineStudentEnitity deadlineStudent) {
        DeadlineEnitity deadline = deadlineStudent.getDeadline();
        UserEnitity deadlineStudentEnitity = deadlineStudent.getStudent();
        GradeEnitity grade = deadlineStudent.getGrade_detail();
        LocalDate currentDate = LocalDate.now();
        LocalDate deadlineDate = deadline.getDate_end();
        LocalDate deadlineStartDate = deadline.getDate_start();
        if (grade != null) {
            return ValidateDeadlineSubmitResult.HAS_POINT;
        }
        if (deadlineStudentEnitity != null) {
            return ValidateDeadlineSubmitResult.SUBMITTED;
        }
        if (currentDate.isBefore(deadlineStartDate)) {
            return ValidateDeadlineSubmitResult.DEADLINE_NOT_OPEN;
        }
        if (currentDate.isAfter(deadlineDate)) {
            return ValidateDeadlineSubmitResult.DEADLINE_EXPIRED;
        }
        return ValidateDeadlineSubmitResult.VALID_TO_MODIFY;
    }

    public String getMessage() {
        switch (code) {
            case 1:
                return "Đã có điểm";
            case 2:
                return "Đã nộp bài";
            case 3:
                return "Chưa đến thời gian nộp bài";
            case 4:
                return "Đã quá thời gian nộp bài";
            case 5:
                return "Có thể nộp bài";
            default:
                return "Lỗi không xác định";
        }
    }

    ;
}
