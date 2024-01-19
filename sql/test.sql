SELECT
  d.deadline_id,
  d.date_start,
  d.date_end,
  d.from_course_id,
  d.by_teacher_id,
  d.title,
  DBMS_LOB.SUBSTR(d.description, 4000, 1) AS description,
  COUNT(ds.student_id) AS num_students_assigned,
  COUNT(DISTINCT CASE WHEN ds.deadline_student_answer IS NOT NULL THEN ds.student_id END) AS num_students_completed,
  COUNT(DISTINCT CASE WHEN gds.grade_detail_id IS NOT NULL THEN ds.student_id END) AS num_students_graded
FROM
    deadline d
        LEFT JOIN
    deadline_student ds ON d.deadline_id = ds.deadline_id
        LEFT JOIN
    grade_detail gds ON ds.grade_detail_id = gds.grade_detail_id
WHERE
  d.by_teacher_id = 3
GROUP BY
  d.deadline_id,
  d.date_start,
  d.date_end,
  d.from_course_id,
  d.by_teacher_id,
  d.title,
  DBMS_LOB.SUBSTR(d.description, 4000, 1);
