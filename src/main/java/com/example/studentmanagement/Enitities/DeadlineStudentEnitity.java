package com.example.studentmanagement.Enitities;

//CREATE TABLE deadline_student (
//        deadline_id NUMBER,
//        student_id NUMBER,
//        deadline_student_status NUMBER DEFAULT 1,
//        deadline_student_answer CLOB NOT NULL,
//        grade_course_id NUMBER,
//        PRIMARY KEY (deadline_id, student_id),
//        FOREIGN KEY (deadline_id) REFERENCES deadline (deadline_id),
//        FOREIGN KEY (student_id) REFERENCES users (user_id),
//        FOREIGN KEY (grade_course_id) REFERENCES grade (grade_id)
//        );
public class DeadlineStudentEnitity {
    private DeadlineEnitity deadline;
    private UserEnitity student;
    private String deadline_student_answer;
    private GradeEnitity grade;

    public DeadlineStudentEnitity() {
    }


    public DeadlineStudentEnitity(DeadlineEnitity deadline, UserEnitity student, int deadline_student_status, String deadline_student_answer, GradeEnitity grade) {
        this.deadline = deadline;
        this.student = student;
        this.deadline_student_answer = deadline_student_answer;
        this.grade = grade;
    }

    public DeadlineEnitity getDeadline() {
        return deadline;
    }

    public void setDeadline(DeadlineEnitity deadline) {
        this.deadline = deadline;
    }

    public UserEnitity getStudent() {
        return student;
    }

    public void setStudent(UserEnitity student) {
        this.student = student;
    }

    public String getDeadline_student_answer() {
        return deadline_student_answer;
    }

    public void setDeadline_student_answer(String deadline_student_answer) {
        this.deadline_student_answer = deadline_student_answer;
    }

    public GradeEnitity getGrade_detail() {
        return grade;
    }

    public void setGrade_detail(GradeEnitity grade) {
        this.grade = grade;
    }
}
